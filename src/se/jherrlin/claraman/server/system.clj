(ns se.jherrlin.claraman.server.system
  (:require
   [chime.core :as chime]
   [com.stuartsierra.component :as component]
   [se.jherrlin.claraman.server.components.httpkit :as components.httpkit]
   [se.jherrlin.claraman.server.components.nrepl :as components.nrepl]
   [se.jherrlin.claraman.server.components.router :as components.router]
   [se.jherrlin.claraman.server.components.sente :as components.sente]
   [se.jherrlin.claraman.server.components.chime :as components.chime]
   [se.jherrlin.claraman.server.components.timbre :as components.timbre]
   [se.jherrlin.claraman.server.components.event-store :as components.event-store]
   [se.jherrlin.claraman.server.components.game-state :as components.game-state]
   [se.jherrlin.claraman.game-state :as game-state]
   [se.jherrlin.claraman.server.endpoints :as server.endpoints]
   [se.jherrlin.claraman.server.endpoints-ws :as server.endpoints-ws]
   [se.jherrlin.claraman.claraman-rules :as claraman-rules]
   [se.jherrlin.claraman.models :as models]
   [taoensso.timbre :as timbre])
  (:import [java.time Instant Duration])
  (:gen-class))

(comment
  (remove-ns 'se.jherrlin.claraman.server.system)
  )

;; TODO make a server component that holds this state
(defonce incomming-commands-state
  (atom {}))

(comment
  (reset! incomming-commands-state {})
  )

(defn started-game-player-is-not-dead-and-have-xy? [gs game-id user-id]
  (and (#{:started} (:game-state (game-state/game gs game-id)))
       (not (game-state/dead-player  gs game-id user-id))
       (game-state/player-current-xy gs game-id user-id)))

(defmulti command->engine-fact (fn [gs command] (:action command)))

(defmethod command->engine-fact :move [gs {:keys [timestamp game-id user-id direction] :as command}]
  (when (started-game-player-is-not-dead-and-have-xy? @gs game-id user-id)
    (let [user-current-xy (game-state/player-current-xy @gs game-id user-id)]
      (models/->PlayerWantsToMove timestamp game-id user-id user-current-xy direction))))

(defmethod command->engine-fact :place-bomb [gs {:keys [user-id game-id timestamp] :as command}]
  (when (started-game-player-is-not-dead-and-have-xy? @gs game-id user-id)
    (let [user-current-xy  (game-state/player-current-xy          @gs game-id user-id)
          user-fire-length    (game-state/player-fire-length         @gs game-id user-id)
          max-number-of-bombs (game-state/player-max-number-of-bombs @gs game-id user-id)]
      (models/->PlayerWantsToPlaceBomb timestamp game-id user-id user-current-xy user-fire-length max-number-of-bombs))))

(defmethod command->engine-fact :throw-bomb [gs {:keys [game-id user-id timestamp] :as command}]
  (when (started-game-player-is-not-dead-and-have-xy? @gs game-id user-id)
    (let [user-current-xy  (game-state/player-current-xy @gs game-id user-id)
          facing-direction (game-state/player-facing-direction @gs game-id user-id)]
      (models/->PlayerWantsToThrowBomb timestamp game-id user-id user-current-xy facing-direction))))

(defn incomming-actions
  "Parse incomming actions to Bomberman rule engine facts"
  [incomming-commands-state gs]
  (some->> @incomming-commands-state
           (vals)
           (map vals)
           (apply concat)
           (map vals)
           (apply concat)
           (map (partial command->engine-fact gs))))

(defn sort-events [x]
  (some->> x
           :actions
           vals
           (apply concat)))

(defn to-cloud-events [events]
  (->> events
       (map #(.toCloudEvent %))
       (sort-by :time #(compare %2 %1))))

(defn game-loop [task-execution-timestamp game-state incomming-commands-state ws-broadcast-fn! add-events-fn!]
  (timbre/trace "Game loop is now started " task-execution-timestamp)
  (try
    (let [user-action-facts   (incomming-actions incomming-commands-state game-state)
          started-game-facts  (game-state/started-games-facts @game-state)
          created-game-facts  (game-state/created-games-facts @game-state)
          shutdown-game-facts (game-state/shutdown-games-facts @game-state)
          rule-enginge-facts  (concat
                               shutdown-game-facts
                               user-action-facts
                               started-game-facts
                               created-game-facts
                               [(models/->TimestampNow (java.util.Date.))])
          actions-from-enging (claraman-rules/run-rules rule-enginge-facts)
          sorted-cloud-events (to-cloud-events (sort-events actions-from-enging))]
      (add-events-fn! sorted-cloud-events)
      (reset! incomming-commands-state {})
      (doseq [game (-> @game-state :games (vals))]
        (ws-broadcast-fn! [:new/game-state game]))
      (timbre/trace "Game loop is now done " (java.util.Date.)))

    (catch Exception e
      (timbre/error "Error in game loop: " e))))

(defn system [{:keys [scheduler logging webserver ws-handler http-handler game-state]}]
  (timbre/info "Creating system.")
  (component/system-map
   :incomming-actions incomming-commands-state
   :nrepl             (components.nrepl/create)
   :event-store       (components.event-store/create)
   :game-state        (component/using
                       (components.game-state/create (:projection-fn game-state))
                       [:event-store])
   :logging           (components.timbre/create logging)
   :scheduler         (component/using
                       (components.chime/create scheduler)
                       [:game-state :incomming-actions :websocket :event-store])
   :websocket         (component/using
                       (components.sente/create {:handler ws-handler})
                       [:game-state :incomming-actions :event-store])
   :router            (component/using
                       (components.router/create {:handler http-handler})
                       [:websocket :logging :game-state :incomming-actions :event-store])
   :webserver         (component/using
                       (components.httpkit/create webserver)
                       [:logging :router])))

(defonce production
  (system
   {:logging      {:logfile  "./logs/bomberman.log"
                   :println? true}
    :game-state   {:projection-fn game-state/the-projection}
    :http-handler #'server.endpoints/handler
    :webserver    {:port 3000}
    :ws-handler   #'server.endpoints-ws/handler
    :scheduler    {:f        #'game-loop
                   :schedule (chime/periodic-seq
                              (Instant/now)
                              (Duration/ofMillis 200))}}))

(defn -main
  "Main entry to start the server."
  [& args]
  (alter-var-root #'production component/start))

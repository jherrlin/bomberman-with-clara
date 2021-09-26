(ns se.jherrlin.server.system
  (:require
   [chime.core :as chime]
   [com.stuartsierra.component :as component]
   [se.jherrlin.server.components.httpkit :as components.httpkit]
   [se.jherrlin.server.components.nrepl :as components.nrepl]
   [se.jherrlin.server.components.router :as components.router]
   [se.jherrlin.server.components.sente :as components.sente]
   [se.jherrlin.server.components.chime :as components.chime]
   [se.jherrlin.server.components.timbre :as components.timbre]
   [se.jherrlin.server.components.event-store :as components.event-store]
   [se.jherrlin.server.components.game-state :as components.game-state]
   [se.jherrlin.claraman.game-state :as game-state]
   [se.jherrlin.server.endpoints :as server.endpoints]
   [se.jherrlin.server.endpoints-ws :as server.endpoints-ws]
   [se.jherrlin.claraman.claraman-rules :as bomberman-rules]
   [se.jherrlin.claraman.user-commands :as user-commands]
   [se.jherrlin.claraman.models :as models]
   [clojure.spec.alpha :as s]
   [clojure.core.async :as a :refer [<! go-loop timeout]]
   [taoensso.timbre :as timbre]
   [se.jherrlin.datetime :as datetime]
   [clojure.pprint :as pprint]
   [se.jherrlin.claraman.board :as board]
   [se.jherrlin.server.application-service :as application-service]
   [se.jherrlin.server.resources :as resources])
  (:import [java.time Instant Duration])
  (:gen-class))

(comment
  (remove-ns 'se.jherrlin.server.system)
  )

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

(defn game-loop [task-execution-timestamp game-state incomming-commands-state ws-broadcast-fn!
                 add-event-fn! add-events-fn!]
  (timbre/trace "Game loop is now started " task-execution-timestamp)
  (try
    (let [user-action-facts   (incomming-actions incomming-commands-state game-state)
          _                   (def user-action-facts user-action-facts)
          started-game-facts  (game-state/started-games-facts @game-state)
          created-game-facts  (game-state/created-games-facts @game-state)
          shutdown-game-facts (game-state/shutdown-games-facts @game-state)
          _                   (def created-game-facts created-game-facts)
          _                   (def started-game-facts started-game-facts)
          _                   (def shutdown-game-facts shutdown-game-facts)
          rule-enginge-facts  (concat
                               shutdown-game-facts
                               user-action-facts
                               started-game-facts
                               created-game-facts
                               [(models/->TimestampNow (java.util.Date.))])
          _                   (def rule-enginge-facts rule-enginge-facts)
          _                   (timbre/info "Rule enginge counts: " (count rule-enginge-facts))
          actions-from-enging (bomberman-rules/run-rules rule-enginge-facts)
          _                   (def actions-from-enging actions-from-enging)
          _                   (def the-sorted-events (to-cloud-events (sort-events actions-from-enging)))]
      (add-events-fn! the-sorted-events)
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
                   :schedule (chime/periodic-seq (Instant/now)
                                                 #_(Duration/ofMinutes 1)
                                                 (Duration/ofMillis 200))}}))

(defn -main
  "Main entry to start the server."
  [& args]
  (alter-var-root #'production component/start))

(comment
  (alter-var-root #'production component/start)
  (alter-var-root #'production component/stop)

  (def add-event-fn! (-> production :event-store :add-event-fn!))
  (def add-events-fn! (-> production :event-store :add-events-fn!))
  (def game-state' (-> production :game-state :game-state))
  (def event-store (-> production :event-store :store))
  (def broadcast-fn! (get-in production [:websocket :broadcast-fn!]))

  (->> @event-store
       :events
       (reverse)
       (reduce game-state/projection {})
       )

  @game-state'
  (spit "/tmp/facts-to-analyze.edn" (pr-str rule-enginge-facts))

  (def facts-to-debug
    (with-in-str (slurp "/tmp/facts-to-analyze.edn")
      (read)))

  (bomberman-rules/run-rules facts-to-debug)
  (map type facts-to-debug)

  (let [game-id #uuid "246e1ee5-48d3-47e4-b9fe-c66e648439a0"]
    (map (fn [{:keys [bot-id bot-name]}]
           (add-events-fn! [(.toCloudEvent (models/->JoinGame (java.util.Date.) game-id bot-id bot-name))]))
         [{:bot-id   #uuid "e24b0220-b98d-4319-8991-9c634da7027c"
           :bot-name "Bot 1"}
          {:bot-id   #uuid "ebc270ae-62fe-42de-90ec-a6b3875eb56e"
           :bot-name "Bot 2"}
          {:bot-id   #uuid "a9d89612-cd08-46ab-8303-89918a633193"
           :bot-name "Bot 3"}]))

  (def run-bot-commands? (atom true))
  (go-loop []
    (let [game-id #uuid "246e1ee5-48d3-47e4-b9fe-c66e648439a0"]
      (doall
       (map (fn [{:keys [bot-id]}]
              (user-commands/register-incomming-user-command!
               incomming-commands-state
               (assoc (user-commands/generate-bot-action game-id bot-id)
                      :timestamp (datetime/now))))
            [{:bot-id   #uuid "e24b0220-b98d-4319-8991-9c634da7027c"
              :bot-name "Bot 1"}
             {:bot-id   #uuid "ebc270ae-62fe-42de-90ec-a6b3875eb56e"
              :bot-name "Bot 2"}
             {:bot-id   #uuid "a9d89612-cd08-46ab-8303-89918a633193"
              :bot-name "Bot 3"}])))
    (when @run-bot-commands?
      (<! (timeout 100))
      (recur)))
  (reset! run-bot-commands? false)



  @game-state'
  @event-store
  @incomming-commands-state
  (reset! game-state' se.jherrlin.server.components.game-state/initial-game-state)
  (reset! event-store se.jherrlin.server.components.event-store/store-init)
  (reset! incomming-commands-state {})


  (->> @event-store
       :events
       count)

  (count (game-state/games-facts @game-state'))

  (java.util.UUID/randomUUID)
  (def repl-subject "JOHN-HANNAS-game")
  (def player-1-id "johns-id")
  (def player-2-id "hannahs-id")
  (def timestamp #inst "2021-09-19T21:57:59.144-00:00")


  (bomberman-rules/run-rules
   (concat
    (incomming-actions incomming-commands-state game-state')
    (game-state/games-facts @game-state')
    [(models/->TimestampNow (java.util.Date.))]))


  @game-state'
  (game-loop (java.util.Date.) game-state' incomming-commands-state broadcast-fn! add-event-fn! add-events-fn!)



  (bomberman-rules/run-rules
   (game-state/games-facts
    (game-state/the-projection {} (->> @event-store :events reverse))
    ))



  (reset! game-state' (game-state/the-projection @game-state' (->> @event-store :events reverse)))

  (game-state/the-projection {} (->> @event-store :events reverse))


  (user-commands/register-incomming-user-command!
   incomming-commands-state
   {:game-id   repl-subject
    :user-id   player-1-id
    :action    :move
    :direction :east})

  (user-commands/register-incomming-user-command!
   incomming-commands-state
   {:game-id   repl-subject
    :user-id   player-1-id
    :action    :move
    :direction :west})

  (user-commands/register-incomming-user-command!
   incomming-commands-state
   {:game-id repl-subject
    :action  :place-bomb
    :user-id player-1-id})

  (user-commands/register-incomming-user-command!
   incomming-commands-state
   {:game-id repl-subject
    :action  :throw-bomb
    :user-id player-1-id})


  (game-loop (java.util.Date.) game-state' incomming-commands-state broadcast-fn! add-event-fn! add-events-fn!)

  (def run-loop? (atom true))
  (go-loop []
    (time
     (let [game-state          game-state'
           user-action-facts   (incomming-actions incomming-commands-state game-state)
           _                   (def user-action-facts user-action-facts)
           game-state-facts    (game-state/games-facts @game-state)
           _                   (def game-state-facts game-state-facts)
           rule-enginge-facts  (concat
                                user-action-facts
                                game-state-facts
                                [(models/->TimestampNow (java.util.Date.))])
           _                   (def rule-enginge-facts rule-enginge-facts)
           actions-from-enging (bomberman-rules/run-rules rule-enginge-facts)
           _                   (def actions-from-enging actions-from-enging)
           _                   (def the-sorted (to-cloud-events (sort-events actions-from-enging)))]
       (add-events-fn! the-sorted)
       ;; (reset! game-state (game-state/the-projection @game-state' the-sorted))
       (reset! incomming-commands-state {})
       @game-state'))

    (when @run-loop?
      (<! (timeout 500))
      (recur)))
  (reset! run-loop? false)

  (user-commands/register-incomming-user-command!
   incomming-commands-state
   {:game-id   repl-subject
    :user-id   player-2-id
    :action    :move
    :direction :west})

  (user-commands/register-incomming-user-command!
   incomming-commands-state
   {:game-id repl-subject
    :action  :throw-bomb
    :user-id player-1-id})


  (def simon-id "SIMONS-id")
  (def jakob-id "JAKOBS-id")
  (def repl-subject-2 "SIMON-JAKOBS-game")
  (def timestamp #inst "2021-09-19T21:57:59.144-00:00")


  (user-commands/register-incomming-user-command!
   incomming-commands-state
   {:game-id   repl-subject-2
    :user-id   simon-id
    :action    :move
    :direction :east})

  (user-commands/register-incomming-user-command!
   incomming-commands-state
   {:game-id   repl-subject-2
    :user-id   jakob-id
    :action    :move
    :direction :west})

  (user-commands/register-incomming-user-command!
   incomming-commands-state
   {:game-id repl-subject-2
    :action  :place-bomb
    :user-id jakob-id})

  (game-loop (java.util.Date.) game-state' incomming-commands-state broadcast-fn! add-event-fn! add-events-fn!)

  )

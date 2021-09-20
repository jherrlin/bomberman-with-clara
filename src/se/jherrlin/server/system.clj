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
   [se.jherrlin.server.game-state :as game-state]
   [se.jherrlin.server.endpoints :as server.endpoints]
   [se.jherrlin.server.endpoints-ws :as server.endpoints-ws]
   [se.jherrlin.clara-labs.bomberman-rules :as bomberman-rules]
   [se.jherrlin.server.user-commands :as user-commands]
   [se.jherrlin.server.models :as models]
   [clojure.core.async :as a :refer [<! go-loop timeout]]
   [taoensso.timbre :as timbre]
   [se.jherrlin.datetime :as datetime])
  (:import [java.time Instant Duration]
           [se.jherrlin.server.models CreateGame JoinGame StartGame])
  (:gen-class))

(comment
  (remove-ns 'se.jherrlin.server.system)
  )

(defonce incomming-commands-state
  (atom {}))

(comment
  (reset! incomming-commands-state {})
  )

(defmulti command->engine-fact (fn [gs command] (:action command)))

(defmethod command->engine-fact :move [gs {:keys [timestamp game-id user-id direction] :as command}]
  (let [game-state'     @gs
        user-current-xy (game-state/player-current-xy game-state' game-id user-id)]
    (models/->PlayerWantsToMove timestamp game-id user-id user-current-xy direction)))

(defmethod command->engine-fact :place-bomb [gs {:keys [user-id game-id timestamp] :as command}]
  (let [game-state'         @gs
        user-current-xy     (game-state/player-current-xy          game-state' game-id user-id)
        user-fire-length    (game-state/player-fire-length         game-state' game-id user-id)
        max-number-of-bombs (game-state/player-max-number-of-bombs game-state' game-id user-id)]
    (models/->PlayerWantsToPlaceBomb timestamp game-id user-id user-current-xy user-fire-length max-number-of-bombs)))

(defmethod command->engine-fact :throw-bomb [gs {:keys [game-id user-id timestamp] :as command}]
  (let [game-state'      @gs
        user-current-xy  (game-state/player-current-xy       game-state' game-id user-id)
        facing-direction (game-state/player-facing-direction game-state' game-id user-id)]
    (models/->PlayerWantsToThrowBomb timestamp game-id user-id user-current-xy facing-direction)))

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
          game-state-facts    (game-state/game-state->enginge-facts @game-state)
          _                   (def game-state-facts game-state-facts)
          rule-enginge-facts  (concat
                               user-action-facts
                               game-state-facts
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


  @game-state'
  @event-store
  @incomming-commands-state
  (reset! game-state' se.jherrlin.server.components.game-state/initial-game-state)
  (reset! event-store se.jherrlin.server.components.event-store/store-init)
  (reset! incomming-commands-state {})


  (->> @event-store
       :events
       count)

  (count (game-state/game-state->enginge-facts @game-state'))

  (java.util.UUID/randomUUID)
  (def repl-subject "JOHN-HANNAS-game")
  (def player-1-id "johns-id")
  (def player-2-id "hannahs-id")
  (def timestamp #inst "2021-09-19T21:57:59.144-00:00")

  (add-events-fn! [(.toCloudEvent (CreateGame.           repl-subject "First game" "my-secret"))])
  (add-events-fn! [(.toCloudEvent (JoinGame.   timestamp repl-subject player-1-id "John"))])
  (add-events-fn! [(.toCloudEvent (JoinGame.   timestamp repl-subject player-2-id "Hannah"))])
  (add-events-fn! [(.toCloudEvent (StartGame.            repl-subject))])
  (add-events-fn! [(.toCloudEvent (command->engine-fact
                                   game-state'
                                   {:game-id   repl-subject
                                    :user-id   player-1-id
                                    :action    :move
                                    :direction :west}))])
  (bomberman-rules/run-rules
   (concat
    (incomming-actions incomming-commands-state game-state')
    (game-state/game-state->enginge-facts @game-state')
    [(models/->TimestampNow (java.util.Date.))]))


  @game-state'
  (game-loop (java.util.Date.) game-state' incomming-commands-state broadcast-fn! add-event-fn! add-events-fn!)



  (bomberman-rules/run-rules
   (game-state/game-state->enginge-facts
    (game-state/the-projection {} (->> @event-store :events reverse (take 22)))))

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
           game-state-facts    (game-state/game-state->enginge-facts @game-state)
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

  (add-events-fn! [(.toCloudEvent (CreateGame.           repl-subject-2 "Simon och Jakob spel" "my-secret"))])
  (add-events-fn! [(.toCloudEvent (JoinGame.   timestamp repl-subject-2 simon-id "Simon"))])
  (add-events-fn! [(.toCloudEvent (JoinGame.   timestamp repl-subject-2 jakob-id "Jakob"))])
  (add-events-fn! [(.toCloudEvent (StartGame.            repl-subject-2))])


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

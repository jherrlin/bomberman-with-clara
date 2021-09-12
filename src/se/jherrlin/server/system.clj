(ns se.jherrlin.server.system
  (:require
   [chime.core :as chime]
   [com.stuartsierra.component :as component]
   [se.jherrlin.server.components.http-middleware :as components.http-middleware]
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
   [se.jherrlin.server.events :as events]
   [se.jherrlin.clara-labs.board :as board]
   [taoensso.timbre :as timbre])
  (:import [java.time Instant Duration]
           [se.jherrlin.clara_labs.bomberman_rules PlayerMove StoneToRemove FireToRemove BombToRemove BombExploading FireOnBoard DeadPlayer BombOnBoard FlyingBomb])
  (:gen-class))

(comment
  (remove-ns 'se.jherrlin.server.system)
  )

{:players    {1 {:position                 [1 1]
                 :fire-length              3
                 :id                       1
                 :sign                     "1"
                 :max-nr-of-bombs-for-user 3
                 :user-facing-direction    :south
                 }}
 :stones     [#_[2 1] [3 1] [4 1] [5 1]
              [4 1] [3 3] [5 5] [5 6] [5 7] [5 8] [6 5] [7 5] [8 5] [9 5]
              [1 3]
              [1 4]
              ]
 :board      board/board2
 :dead-users {}
 :bombs      []
    :fire       []}


(defonce incomming-commands-state
  (atom {}))

(comment
  (reset! incomming-commands-state {})
  )

(defmulti command->engine-fact (fn [gs command] (:action command)))

(defmethod command->engine-fact :move [gs {:keys [game-id user-id direction] :as command}]
  (let [game-state'     @gs
        user-current-xy (game-state/player-current-xy game-state' game-id user-id)]
    (bomberman-rules/->PlayerWantsToMove game-id user-id user-current-xy direction)))

(defmethod command->engine-fact :place-bomb [gs {:keys [user-id game-id] :as command}]
  (let [game-state'         @gs
        user-current-xy     (game-state/player-current-xy          game-state' game-id user-id)
        user-fire-length    (game-state/player-fire-length         game-state' game-id user-id)
        max-number-of-bombs (game-state/player-max-number-of-bombs game-state' game-id user-id)
        now                 (java.util.Date.)]
    (bomberman-rules/->PlayerWantsToPlaceBomb game-id user-id user-current-xy user-fire-length now max-number-of-bombs)))

(defmethod command->engine-fact :throw-bomb [gs {:keys [game-id user-id] :as command}]
  (let [game-state'      @gs
        user-current-xy  (game-state/player-current-xy       game-state' game-id user-id)
        facing-direction (game-state/player-facing-direction game-state' game-id user-id)]
    (bomberman-rules/->PlayerWantsToThrowBomb game-id user-id user-current-xy facing-direction)))

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

;; (partial command->engine-fact gs k)

(defn game-state->enginge-facts [gs]
  (->> @gs
       :games
       vals
       (map (fn [{:keys [subject] :as game}]
              (concat
               [(bomberman-rules/->Board subject (game-state/board game))]
               (->> (game-state/players game)
                    (vals)
                    (map (fn [{:keys [player-id position] :as player}]
                           (bomberman-rules/->PlayerPositionOnBoard subject player-id position))))
               (->> (game-state/stones game)
                    (map (partial bomberman-rules/->Stone subject)))
               (->> (game-state/bombs game)
                    (map bomberman-rules/map->BombOnBoard))
               (->> (game-state/fires game)
                    (map bomberman-rules/map->FireOnBoard))
               ;; flying-bombs
               )))
       (apply concat)))

(defn game-loop [task-execution-timestamp game-state incomming-commands-state ws-broadcast-fn! add-event-fn!]
  (println "Game loop is now started " task-execution-timestamp)
  (try
    (->> (incomming-actions incomming-commands-state game-state)
         (map #(.toCloudEvent %))
         (sort-by :time #(compare %2 %1))
         (map add-event-fn!)
         (doall))
    (let [user-action-facts   (incomming-actions incomming-commands-state game-state)
          _                   (def user-action-facts user-action-facts)
          game-state-facts    (game-state->enginge-facts game-state)
          _                   (def game-state-facts game-state-facts)
          rule-enginge-facts  (concat
                               user-action-facts
                               game-state-facts
                               [(bomberman-rules/->TimestampNow (java.util.Date.))])
          _                   (def rule-enginge-facts rule-enginge-facts)
          actions-from-enging (bomberman-rules/run-rules rule-enginge-facts)
          _                   (def actions-from-enging actions-from-enging)]
      (->> actions-from-enging
           :actions
           vals
           (apply concat)
           (map #(.toCloudEvent %))
           (sort-by :time #(compare %2 %1))
           (map add-event-fn!)
           (doall))
      ;; (reset! game-state new-game-state)
      (reset! incomming-commands-state {})
      ;; (ws-broadcast-fn! [:new/game-state new-game-state])
      (println "Game loop is now done " (java.util.Date.)))

    (catch Exception e
      (timbre/error "Error in game loop: " e))))

(defn system [{:keys [scheduler timbre webserver ws-handler http-handler game-state]}]
  (timbre/info "Creating system.")
  (component/system-map
   :incomming-actions incomming-commands-state
   :event-store       (components.event-store/create)
   :game-state        (component/using
                       (components.game-state/create (:projection-fn game-state))
                       [:event-store])
   :logging           (components.timbre/create timbre)
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
   {:game-state   {:projection-fn game-state/projection}
    :http-handler #'server.endpoints/handler
    :webserver    {:port 3005}
    :ws-handler   #'server.endpoints-ws/handler
    :scheduler    {:f        #'game-loop
                   :schedule (chime/periodic-seq (Instant/now)
                                                 (Duration/ofMinutes 30)
                                                 #_(Duration/ofMillis 200))}}))


(comment
  (alter-var-root #'production component/start)
  (alter-var-root #'production component/stop)

  (def add-event-fn! (-> production :event-store :add-event-fn!))
  (def game-state' (-> production :game-state :game-state))
  (def event-store (-> production :event-store :store))
  (def broadcast-fn! (get-in production [:websocket :broadcast-fn!]))

  @game-state'
  @event-store
  @incomming-commands-state
  (reset! incomming-commands-state {})

  (java.util.UUID/randomUUID)
  (def repl-subject "JOHN-HANNAS-game")
  (def player-1-id "johns-id")
  (def player-2-id "hannahs-id")

  (add-event-fn! (events/create-game repl-subject "First game" "my-secret"))
  (add-event-fn! (events/join-game   repl-subject player-1-id "John"))
  (add-event-fn! (events/join-game   repl-subject player-2-id "Hannah"))
  (add-event-fn! (events/start-game  repl-subject))
  (add-event-fn! (events/player-wants-to-move repl-subject player-1-id :north))

  (user-commands/register-incomming-user-command!
   incomming-commands-state
   {:game-id   repl-subject
    :user-id   player-1-id
    :action    :move
    :direction :east})

  (user-commands/register-incomming-user-command!
   incomming-commands-state
   {:game-id   repl-subject
    :user-id   player-2-id
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


  (def simon-id "SIMONS-id")
  (def jakob-id "JAKOBS-id")
  (def repl-subject-2 "SIMON-JAKOBS-game")
  (add-event-fn! (events/create-game repl-subject-2 "Second game" "my-second-secret"))
  (add-event-fn! (events/join-game   repl-subject-2 simon-id "Simon"))
  (add-event-fn! (events/join-game   repl-subject-2 jakob-id "Jakob"))


  (game-loop (java.util.Date.) game-state' incomming-commands-state broadcast-fn! add-event-fn!)

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


  )

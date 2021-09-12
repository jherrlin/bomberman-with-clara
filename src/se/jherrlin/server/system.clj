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
  (:import [java.time Instant Duration])
  (:gen-class))

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

(defmulti command->engine-fact (fn [gs subject command] (:action command)))

(defmethod command->engine-fact :move [gs subject {:keys [user-id direction] :as command}]
  (let [game-state'     @gs
        user-current-xy (game-state/player-current-xy game-state' subject user-id)]
    (bomberman-rules/->PlayerWantsToMove user-id user-current-xy direction)))

(defmethod command->engine-fact :place-bomb [gs subject {:keys [user-id] :as command}]
  (let [game-state'         @gs
        user-current-xy     (game-state/player-current-xy          game-state' subject user-id)
        user-fire-length    (game-state/player-fire-length         game-state' subject user-id)
        max-number-of-bombs (game-state/player-max-number-of-bombs game-state' subject user-id)
        now                 (java.util.Date.)]
    (bomberman-rules/->PlayerWantsToPlaceBomb user-id user-current-xy user-fire-length now max-number-of-bombs)))

(defmethod command->engine-fact :throw-bomb [gs subject {:keys [user-id] :as command}]
  (let [game-state'      @gs
        user-current-xy  (game-state/player-current-xy       game-state' subject user-id)
        facing-direction (game-state/player-facing-direction game-state' subject user-id)]
    (bomberman-rules/->PlayerWantsToThrowBomb user-id user-current-xy facing-direction)))

(defn incomming-actions
  "Parse incomming actions to Bomberman rule engine facts"
  [incomming-state gs]
  (some->> @incomming-state
           (vals)
           (map vals)
           (apply concat)
           (map vals)
           (apply concat)
           (group-by :game-id)
           (reduce-kv (fn [m k v]
                        (assoc m k (map (partial command->engine-fact gs k) v))) {})))

(defn game-state->enginge-facts [gs subject]
  (let [gs           @gs
        board        (bomberman-rules/->Board (game-state/board gs subject))
        players      (->> (game-state/players gs subject)
                          (vals)
                          (map (fn [{:keys [position player-id]}] (bomberman-rules/->PlayerPositionOnBoard player-id position))))
        stones       (->> (game-state/stones gs subject)
                          (map bomberman-rules/->Stone))
        fire         (->> (game-state/fires gs subject)
                          (map bomberman-rules/map->FireOnBoard))
        bombs        (->> (game-state/bombs gs subject)
                          (map bomberman-rules/map->BombOnBoard))
        flying-bombs (->> (game-state/flying-bombs gs subject)
                          (map bomberman-rules/map->FlyingBomb))]
    (concat
     [board]
     players
     stones
     fire
     bombs
     flying-bombs)))

(defn remove-stones-from-gs [gs stones-to-remove]
  (let [stones-to-remove' (->> stones-to-remove (map :position-xy) (set))
        stones            (:stones gs)]
    (assoc gs :stones (remove stones-to-remove' stones))))

(defn update-bombs-to-gs [gs bombs]
  (assoc gs :bombs (->> bombs (map #(into {} %)) (set))))

(defn update-fire-to-gs [gs fires]
  (assoc gs :fire (->> fires (map #(into {} %)) (set))))

(defn update-flying-bombs-to-gs [gs flying-bombs]
  (assoc gs :flying-bombs (->> flying-bombs (map #(into {} %)) (set))))

(defn update-user-position-in-gs [gs {:keys [user-id next-position direction] :as user-move}]
  (-> gs
      (assoc-in [:players user-id :position]              next-position)
      (assoc-in [:players user-id :user-facing-direction] direction)))

(defn update-users-position-in-gs [gs user-moves]
  (reduce update-user-position-in-gs gs user-moves))

(defn update-user-direction-in-gs [gs {:keys [user-id direction] :as user-changed-direction}]
  (assoc-in gs [:players user-id :user-facing-direction] direction))

(defn update-users-direction-in-gs [gs user-moves]
  (reduce update-user-direction-in-gs gs user-moves))

(defn update-user-is-dead [gs dead-users]
  (reduce
   (fn [gs' {:keys [user-id killed-by-user-id] :as dead-user}]
     (-> gs'
         (assoc-in [:players user-id :dead?] true)
         (assoc-in [:dead-users user-id] (into {} dead-user))))
   gs
   dead-users))

(defn apply-actions-to-game-state [gs {:keys [actions]}]
  (let [{:keys [bombs-on-board fire-on-board stones-to-remove user-moves dead-users flying-bombs user-wants-to-move]} actions]
    (-> @gs
        (remove-stones-from-gs        stones-to-remove)
        (update-bombs-to-gs           bombs-on-board)
        (update-fire-to-gs            fire-on-board)
        (update-users-position-in-gs  user-moves)
        (update-user-is-dead          dead-users)
        (update-flying-bombs-to-gs    flying-bombs)
        (update-users-direction-in-gs user-wants-to-move))))

(defmulti  player-command-to-event :action)
(defmethod player-command-to-event :move       [{:keys [direction game-id user-id]}] (events/player-wants-to-move       game-id user-id direction))
(defmethod player-command-to-event :place-bomb [{:keys [game-id user-id]}]           (events/player-wants-to-place-bomb game-id user-id))
(defmethod player-command-to-event :throw-bomb [{:keys [game-id user-id]}]           (events/player-wants-to-throw-bomb game-id user-id))

(defn add-player-events! [add-event-fn! incomming-commands-state]
  (some->> @incomming-commands-state
           (vals)
           (map vals)
           (apply concat)
           (map vals)
           (apply concat)
           (map player-command-to-event)
           (map add-event-fn!)
           (doall)))

(defn game-loop [task-execution-timestamp game-state incomming-commands-state ws-broadcast-fn! add-event-fn!]
  (println "Game loop is now started " task-execution-timestamp)
  (try
    (add-player-events! add-event-fn! incomming-commands-state)
    (let [user-action-facts (incomming-actions incomming-commands-state game-state)]
      (def user-action-facts user-action-facts)
      ;; doseq
      (->> user-action-facts
           (map (fn [[subject actions]]
                  (def actions actions)
                  (let [game-state-facts    (game-state->enginge-facts game-state subject)
                        _                   (def game-state-facts game-state-facts)
                        rule-enginge-facts  (concat
                                             actions
                                             game-state-facts
                                             [(bomberman-rules/->TimestampNow (java.util.Date.))])
                        _                   (def rule-enginge-facts rule-enginge-facts)
                        actions-from-enging (bomberman-rules/run-rules rule-enginge-facts)
                        _                   (def actions-from-enging actions-from-enging)
                        _                   (def events-from-enginge nil)
                        ;; new-game-state      (apply-actions-to-game-state game-state actions-from-enging)
                        ;; _                   (def new-game-state new-game-state)
                        ]
                    ;; (reset! game-state new-game-state)
                    (reset! incomming-commands-state {})
                    ;; (ws-broadcast-fn! [:new/game-state new-game-state])
                    (println "Game loop is now done " (java.util.Date.)))))
           (doall)))

    (catch Exception e
      (timbre/error "Error in game loop: " e))))

(let [{:keys [bombs-on-board fire-on-board stones-to-remove user-moves dead-users flying-bombs user-wants-to-move]} actions-from-enging]

  )


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

  (java.util.UUID/randomUUID)
  (def repl-subject #uuid "c03e430f-2b24-4109-a923-08c986a682a8")
  (def player-1-id #uuid "e677bf82-0137-4105-940d-6d74429d31b0")
  (def player-2-id #uuid "663bd7a5-7220-40e5-b08d-597c43b89e0a")

  (add-event-fn! (events/create-game repl-subject "First game" "my-secret"))
  (add-event-fn! (events/join-game   repl-subject player-1-id "John"))
  (add-event-fn! (events/join-game   repl-subject player-2-id "Hannah"))
  (add-event-fn! (events/start-game  repl-subject))
  (add-event-fn! (events/player-wants-to-move repl-subject player-1-id :north))

  (game-loop (java.util.Date.) game-state' incomming-commands-state broadcast-fn! add-event-fn!)


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
  )

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
   [se.jherrlin.server.endpoints :as server.endpoints]
   [se.jherrlin.server.endpoints-ws :as server.endpoints-ws]
   [se.jherrlin.clara-labs.bomberman-rules :as bomberman-rules]
   [se.jherrlin.server.user-commands :as user-commands]
   [se.jherrlin.clara-labs.board :as board]
   [taoensso.timbre :as timbre])
  (:import [java.time Instant Duration])
  (:gen-class))


(defonce game-state
  (atom
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
    :fire       []}))

(comment
  (reset! game-state
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
           :fire       []})
  )

(defonce incomming-actions-state
  (atom {}))



;; gs = game state
(defn gs-player-current-xy  [game-state player-id] (get-in game-state [:players player-id :position]))
(defn gs-player-fire-length [game-state player-id] (get-in game-state [:players player-id :fire-length]))

(defn gs-player-max-number-of-bombs [game-state player-id]
  (get-in game-state [:players player-id :max-nr-of-bombs-for-user]))
(defn gs-player-facing-direction [game-state player-id]
  (get-in game-state [:players player-id :user-facing-direction]))
(defn gs-board              [game-state]           (get-in game-state [:board]))
(defn gs-stones             [game-state]           (get-in game-state [:stones]))
(defn gs-fires              [game-state]           (get-in game-state [:fire]))
(defn gs-players            [game-state]           (get-in game-state [:players]))
(defn gs-bombs              [game-state]           (get-in game-state [:bombs]))
(defn gs-flying-bombs       [game-state]           (get-in game-state [:flying-bombs]))




(defmulti command->engine-fact (fn [game-state command] (:action command)))

(defmethod command->engine-fact :move [game-state {:keys [user-id payload] :as command}]
  (let [game-state' @game-state
        user-current-xy (get-in game-state' [:players user-id :position])]
    (bomberman-rules/->UserWantsToMove user-id user-current-xy (:direction payload))))

(defmethod command->engine-fact :place-bomb [game-state {:keys [user-id] :as command}]
  (let [game-state'         @game-state
        user-current-xy     (gs-player-current-xy game-state' user-id)
        user-fire-length    (gs-player-fire-length game-state' user-id)
        max-number-of-bombs (gs-player-max-number-of-bombs game-state' user-id)
        now                 (java.util.Date.)]
    (bomberman-rules/->UserWantsToPlaceBomb user-id user-current-xy user-fire-length now max-number-of-bombs)))

(defmethod command->engine-fact :throw-bomb [game-state {:keys [user-id] :as command}]
  (let [game-state'         @game-state
        user-current-xy     (gs-player-current-xy game-state' user-id)
        facing-direction    (gs-player-facing-direction game-state' user-id)]
    (bomberman-rules/->UserWantsToThrowBomb user-id user-current-xy facing-direction)))

(defn incomming-actions
  "Parse incomming actions to Bomberman rule engine facts"
  [incomming-state game-state]
  (some->> @incomming-state
           vals
           (map vals)
           (apply concat)
           (map (partial command->engine-fact game-state))))

(defn game-state->enginge-facts [game-state]
  (let [gs           @game-state
        board        (bomberman-rules/->Board (gs-board gs))
        players      (->> (gs-players gs)
                          (vals)
                          (map (fn [{:keys [position id]}] (bomberman-rules/->UserPositionOnBoard id position))))
        stones       (->> (gs-stones gs)
                          (map bomberman-rules/->Stone))
        fire         (->> (gs-fires gs)
                          (map bomberman-rules/map->FireOnBoard))
        bombs        (->> (gs-bombs gs)
                          (map bomberman-rules/map->BombOnBoard))
        flying-bombs (->> (gs-flying-bombs gs)
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

(defn game-loop [task-execution-timestamp game-state incomming-actions-state ws-broadcast-fn]
  (println "Game loop is now started " task-execution-timestamp)
  (try
    (let [user-action-facts   (incomming-actions incomming-actions-state game-state)
          _                   (def user-action-facts user-action-facts)
          game-state-facts    (game-state->enginge-facts game-state)
          _                   (def game-state-facts game-state-facts)
          facts               (concat
                               user-action-facts
                               game-state-facts
                               [(bomberman-rules/->TimestampNow (java.util.Date.))])
          _                   (def facts facts)
          actions-from-enging (bomberman-rules/run-rules facts)
          _                   (def actions-from-enging actions-from-enging)
          new-game-state      (apply-actions-to-game-state game-state actions-from-enging)
          _                   (def new-game-state new-game-state)]
      (reset! game-state new-game-state)
      (reset! incomming-actions-state {})
      (ws-broadcast-fn [:new/game-state new-game-state])
      (println "Game loop is now done " (java.util.Date.))
      new-game-state)
    (catch Exception e
      (timbre/error "Error in game loop: " e))))

(defn system [{:keys [scheduler timbre webserver ws-handler http-handler]}]
  (timbre/info "Creating system.")
  (component/system-map
   :game-state        game-state
   :incomming-actions incomming-actions-state
   :logging           (components.timbre/create timbre)
   :scheduler         (component/using
                       (components.chime/create scheduler)
                       [:game-state :incomming-actions :websocket])
   :websocket         (component/using
                       (components.sente/create {:handler ws-handler})
                       [:game-state :incomming-actions])
   :router            (component/using
                       (components.router/create {:handler http-handler})
                       [:websocket :logging :game-state :incomming-actions])
   :webserver         (component/using
                       (components.httpkit/create webserver)
                       [:logging :router])))


(defonce production
  (system
   {:http-handler #'server.endpoints/handler
    :webserver    {:port 3005}
    :ws-handler   #'server.endpoints-ws/handler
    :scheduler    {:f        #'game-loop
                   :schedule (chime/periodic-seq (Instant/now)
                                                 #_(Duration/ofMinutes 30)
                                                 (Duration/ofMillis 200))}}))


(comment
  (alter-var-root #'production component/start)
  (alter-var-root #'production component/stop)

  (game-loop (java.util.Date.) game-state incomming-actions-state (get-in production [:websocket :broadcast-fn]))


  @game-state

  (user-commands/register-incomming-user-action!
   incomming-actions-state
   {:action  :move
    :user-id 1
    :payload {:direction :east}})

  (user-commands/register-incomming-user-action!
   incomming-actions-state
   {:action  :move
    :user-id 1
    :payload {:direction :west}})

  (user-commands/register-incomming-user-action!
   incomming-actions-state
   {:action  :place-bomb
    :user-id 1})

  (user-commands/register-incomming-user-action!
   incomming-actions-state
   {:action  :throw-bomb
    :user-id 1})
  )

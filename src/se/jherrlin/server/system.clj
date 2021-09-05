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
   [se.jherrlin.clara-labs.board :as board]
   [taoensso.timbre :as timbre])
  (:import [java.time Instant Duration])
  (:gen-class))


(defonce game-state
  (atom {:players    {1 {:position    [1 1]
                         :fire-length 3
                         :id          1
                         :sign        "1"}}
         :stones     [[4 1] [3 3]]
         :board      (board/init 6)
         :dead-users {}
         :bombs      [#_{:user-id              1
                         :bomb-position-xy     [3 2]
                         :fire-length          3
                         :bomb-added-timestamp (java.util.Date.)}]
         :fire       [#_{:user-id              6
                         :fire-position-xy     [3 2]
                         :fire-start-timestamp (java.util.Date.)}]}))

(comment
  (reset! game-state
          {:players    {1 {:position    [1 1]
                         :fire-length 3
                         :id          1
                         :sign        "1"}}
         :stones     [[4 1] [3 3]]
         :board      (board/init 6)
         :dead-users {}
         :bombs      [#_{:user-id              1
                         :bomb-position-xy     [3 2]
                         :fire-length          3
                         :bomb-added-timestamp (java.util.Date.)}]
         :fire       [#_{:user-id              6
                         :fire-position-xy     [3 2]
                         :fire-start-timestamp (java.util.Date.)}]})
  )

(defonce incomming-actions-state
  (atom {}))



;; gs = game state
(defn gs-player-current-xy  [game-state player-id] (get-in game-state [:players player-id :position]))
(defn gs-player-fire-length [game-state player-id] (get-in game-state [:players player-id :fire-length]))
(defn gs-board              [game-state]           (get-in game-state [:board]))
(defn gs-stones             [game-state]           (get-in game-state [:stones]))
(defn gs-fires              [game-state]           (get-in game-state [:fire]))
(defn gs-players            [game-state]           (get-in game-state [:players]))
(defn gs-bombs              [game-state]           (get-in game-state [:bombs]))


(defmulti command->engine-fact (fn [game-state command] (:action command)))

(defmethod command->engine-fact :move [game-state {:keys [user-id payload] :as command}]
  (let [game-state' @game-state
        user-current-xy (get-in game-state' [:players user-id :position])]
    (bomberman-rules/->UserWantsToMove user-id user-current-xy (:direction payload))))

(defmethod command->engine-fact :place-bomb [game-state {:keys [user-id] :as command}]
  (let [game-state'      @game-state
        user-current-xy  (gs-player-current-xy game-state' user-id)
        user-fire-length (gs-player-fire-length game-state' user-id)
        now              (java.util.Date.)]
    (bomberman-rules/->UserWantsToPlaceBomb user-id user-current-xy user-fire-length now)))

(defn incomming-actions
  "Parse incomming actions to Bomberman rule engine facts"
  [incomming-state game-state]
  (some->> @incomming-state
           vals
           (map vals)
           (apply concat)
           (map (partial command->engine-fact game-state))))

(defn game-state->enginge-facts [game-state]
  (let [gs      @game-state
        board   (bomberman-rules/->Board (gs-board gs))
        players (->> (gs-players gs)
                     (vals)
                     (map (fn [{:keys [position id]}] (bomberman-rules/->UserPositionOnBoard id position))))
        stones  (->> (gs-stones gs)
                     (map bomberman-rules/->Stone))
        fire    (->> (gs-fires gs)
                     (map bomberman-rules/map->FireOnBoard))
        bombs   (->> (gs-bombs gs)
                     (map bomberman-rules/map->BombOnBoard))]
    (concat
     [board]
     players
     stones
     fire
     bombs)))

(defn remove-stones-from-gs [gs stones-to-remove]
  (let [stones-to-remove' (->> stones-to-remove (map :position-xy) (set))
        stones            (:stones gs)]
    (assoc gs :stones (remove stones-to-remove' stones))))

(defn update-bombs-to-gs [gs bombs]
  (assoc gs :bombs (set bombs)))

(defn update-fire-to-gs [gs fires]
  (assoc gs :fire (set fires)))

(defn update-user-position-in-gs [gs {:keys [user-id next-position] :as user-move}]
  (assoc-in gs [:players user-id :position] next-position))

(defn update-users-position-in-gs [gs user-moves]
  (reduce update-user-position-in-gs gs user-moves))

(defn update-user-is-dead [gs dead-users]
  (reduce
   (fn [gs' {:keys [user-id killed-by-user-id] :as dead-user}]
     (-> gs'
         (assoc-in [:players user-id :dead?] true)
         (assoc-in [:dead-users user-id] dead-user)))
   gs
   dead-users))

(defn apply-actions-to-game-state [gs {:keys [actions]}]
  (let [{:keys [bombs-on-board fire-on-board stones-to-remove user-moves dead-users]} actions]
    (-> @gs
        (remove-stones-from-gs       stones-to-remove)
        (update-bombs-to-gs          bombs-on-board)
        (update-fire-to-gs           fire-on-board)
        (update-users-position-in-gs user-moves)
        (update-user-is-dead         dead-users))))

(defn game-loop [task-execution-timestamp game-state incomming-actions-state ws-broadcast-fn]
  (println "Game loop is now started " task-execution-timestamp)
  (let [user-action-facts   (incomming-actions incomming-actions-state game-state)
        _                   (def user-action-facts user-action-facts)
        game-state-facts    (game-state->enginge-facts game-state)
        _                   (def game-state-facts game-state-facts)
        actions-from-enging (bomberman-rules/run-rules
                             (concat
                              user-action-facts
                              game-state-facts
                              [(bomberman-rules/->TimestampNow (java.util.Date.))]))
        _                   (def actions-from-enging actions-from-enging)
        new-game-state      (apply-actions-to-game-state game-state actions-from-enging)
        _                   (def new-game-state new-game-state)]
    (reset! game-state new-game-state)
    (reset! incomming-actions-state {})

    #_(ws-broadcast-fn [:new/game-state new-game-state]))

  ;; (ws-broadcast-fn [:test/broadcast "hejsan"])
  (println "Game loop is now done " (java.util.Date.))
  new-game-state)


(defmulti register-incomming-user-action! :action)

(defmethod register-incomming-user-action! :move [{:keys [action user-id] :as m}]
  (swap! incomming-actions-state assoc-in [action user-id] m))

(defmethod register-incomming-user-action! :place-bomb [{:keys [action user-id] :as m}]
  (swap! incomming-actions-state assoc-in [action user-id] m))

(defmethod register-incomming-user-action! :default [m]
  (throw (Exception. (str "In dont know what to do with" m))))


(comment
  (game-loop (java.util.Date.) game-state incomming-actions-state (fn []))
  @game-state

  (register-incomming-user-action!
   {:action  :move
    :user-id 1
    :payload {:direction :east}})

  (register-incomming-user-action!
   {:action  :place-bomb
    :user-id 1})
  )


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
                                                 (Duration/ofMinutes 1)
                                                 #_(Duration/ofMillis 200))}}))


(comment
  (alter-var-root #'production component/start)
  (alter-var-root #'production component/stop)
  )

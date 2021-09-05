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
  (->> @incomming-state
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


(defn game-loop [task-execution-timestamp game-state incomming-state ws-broadcast-fn]
  (def ws-broadcast-fn ws-broadcast-fn)
  (def game-state game-state)
  (def incomming-state incomming-state)
  (println "Game loop is now started " task-execution-timestamp)
  (let [user-action-facts (incomming-actions incomming-state game-state)
        game-state-facts  (game-state->enginge-facts game-state)]
    (def user-action-facts user-action-facts)
    (def game-state-facts game-state-facts)
    (bomberman-rules/run-rules
     (concat
      user-action-facts
      game-state-facts
      [(bomberman-rules/->TimestampNow (java.util.Date.))]))
    )


  ;; (ws-broadcast-fn [:test/broadcast "hejsan"])
  (println "Game loop is now done " (java.util.Date.))
  )


(defmulti register-incomming-user-action! :action)

(defmethod register-incomming-user-action! :move [{:keys [action user-id] :as m}]
  (swap! incomming-state assoc-in [action user-id] m))

(defmethod register-incomming-user-action! :place-bomb [{:keys [action user-id] :as m}]
  (swap! incomming-state assoc-in [action user-id] m))

(defmethod register-incomming-user-action! :default [m]
  (throw (Exception. (str "In dont know what to do with" m))))





(register-incomming-user-action!
 {:action  :move
  :user-id 1
  :payload {:direction :west}})

(register-incomming-user-action!
 {:action  :place-bomb
  :user-id 1})


;; @game-state
;; (reset! incomming-state {})





(defn system [{:keys [scheduler timbre webserver ws-handler http-handler]}]
  (timbre/info "Creating system.")
  (component/system-map
   :game-state        (atom {:players {1 {:position    [1 1]
                                          :fire-length 3
                                          :id          1
                                          :sign        "1"}}
                             :stones  [[4 1] [3 3]]
                             :board   (board/init 6)
                             :bombs   [{:user-id          1
                                        :bomb-position-xy [3 2]
                                        :fire-length      3
                                        :bomb-added-timestamp (java.util.Date.)}]
                             :fire    [#_{:user-id              6
                                        :fire-position-xy     [3 2]
                                        :fire-start-timestamp (java.util.Date.)}]})
   :incomming-actions (atom {})
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


(def production
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

(ns se.jherrlin.server.game-state
  (:require [clojure.string :as str])
  (:import  [se.jherrlin.server.models
             PlayerMove StoneToRemove FireToRemove BombToRemove BombExploading FireOnBoard DeadPlayer BombOnBoard FlyingBomb
             CreateGame JoinGame StartGame EndGame PlayerWantsToPlaceBomb]))

;; Access data in game state
(defn player-current-xy
  ([game-state player-id]         (get-in game-state                [:players player-id :position]))
  ([game-state subject player-id] (get-in game-state [:games subject :players player-id :position])))

(defn player-fire-length
  ([game-state player-id]         (get-in game-state                [:players player-id :fire-length]))
  ([game-state subject player-id] (get-in game-state [:games subject :players player-id :fire-length])))

(defn player-max-number-of-bombs
  ([game-state player-id]         (get-in game-state                [:players player-id :max-nr-of-bombs-for-user]))
  ([game-state subject player-id] (get-in game-state [:games subject :players player-id :max-nr-of-bombs-for-user])))

(defn player-facing-direction
  ([game-state player-id]         (get-in game-state                [:players player-id :user-facing-direction]))
  ([game-state subject player-id] (get-in game-state [:games subject :players player-id :user-facing-direction])))

(defn board
  ([game-state]                   (get-in game-state                [:board]))
  ([game-state subject]           (get-in game-state [:games subject :board])))

(defn stones
  ([game-state]                   (get-in game-state                [:stones]))
  ([game-state subject]           (get-in game-state [:games subject :stones])))

(defn fires
  ([game-state]                   (get-in game-state                [:fire]))
  ([game-state subject]           (get-in game-state [:games subject :fire])))

(defn players
  ([game-state]                   (get-in game-state                [:players]))
  ([game-state subject]           (get-in game-state [:games subject :players])))

(defn bombs
  ([game-state]                   (get-in game-state                [:bombs]))
  ([game-state subject]           (get-in game-state [:games subject :bombs])))

(defn flying-bombs
  ([game-state]                   (get-in game-state                [:flying-bombs]))
  ([game-state subject]           (get-in game-state [:games subject :flying-bombs])))

(defn urn->qualified-keyword
  "Convert event `source` and `type` to qualified keyword."
  [source type]
  (keyword (str
            (-> source
                (str/replace #"^urn:" "")
                (str/replace #":" "."))
            "/"
            type)))

(defn event->projection-key [{:keys [source type] :as event}]
  (urn->qualified-keyword source type))

(defmulti projection (fn [game-state event] (event->projection-key event)))

(defmethod projection :se.jherrlin.bomberman.game/create-game
  [game-state {:keys [subject data] :as event}]
  (assoc-in game-state [:games subject] (assoc data :subject subject)))

(defmethod projection :se.jherrlin.bomberman.game/join-game
  [game-state {:keys [subject data] :as event}]
  (let [players-in-game    (get-in game-state [:games subject :players])
        number-of-players  (count players-in-game)
        player-gets-number (inc number-of-players)]
    (assoc-in game-state
              [:games subject :players (:player-id data)]
              (assoc data :player-nr player-gets-number))))

(defmethod projection :se.jherrlin.bomberman.game/start
  [game-state {:keys [subject data] :as event}]
  (assoc-in game-state [:games subject :game-state] :started))

(defmethod projection :se.jherrlin.bomberman.player/wants-to-move
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [direction player-id]} data]
    (assoc-in game-state [:games subject :players player-id :user-facing-direction] direction)))

(defmethod projection :se.jherrlin.bomberman.player/move
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [player-id next-position direction]} data]
    (assoc-in game-state [:games subject :players player-id :position] next-position)))

(defmethod projection :se.jherrlin.bomberman.game/bomb-on-board
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [player-id bomb-position-xy fire-length bomb-added-timestamp]} data]
    (update-in game-state [:games subject :bombs] conj data)))

(defmethod projection :se.jherrlin.bomberman.game/bomb-to-remove
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [position-xy]} data]
    (update-in game-state [:games subject :bombs] remove position-xy)))

(defmethod projection :se.jherrlin.bomberman.game/fire-on-board
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [player-id fire-position-xy fire-start-timestamp]} data]
    (update-in game-state [:games subject :fire] conj data)))

(defmethod projection :se.jherrlin.bomberman.game/fire-to-remove
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [fire-position-xy]} data]
    (update-in game-state [:games subject :fire] remove (comp #{fire-position-xy} fire-position-xy))))

(defmethod projection :default [game-state event]
  (println "Error! Could not find projection for event:")
  ;; (println game-state)
  (clojure.pprint/pprint event)
  game-state)


(comment
  (java.util.UUID/randomUUID)
  (def repl-game-id #uuid "c03e430f-2b24-4109-a923-08c986a682a8")
  (def player-1-ws-id #uuid "e677bf82-0137-4105-940d-6d74429d31b0")
  (def player-2-ws-id #uuid "663bd7a5-7220-40e5-b08d-597c43b89e0a")


  (reduce
   (fn [gs m] (projection gs (.toCloudEvent m)))
   {}
   [(CreateGame.             repl-game-id "First game" "my-secret")
    (JoinGame.               repl-game-id player-1-ws-id "John")
    (JoinGame.               repl-game-id player-2-ws-id "Hannah")
    (StartGame.              repl-game-id)
    (PlayerMove.             repl-game-id player-1-ws-id [2 1] :east)
    (PlayerWantsToPlaceBomb. repl-game-id player-1-ws-id [2 1] 3 (java.util.Date.) 3)])

  )

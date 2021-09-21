(ns se.jherrlin.server.game-state
  (:require [clojure.string :as str]
            [se.jherrlin.server.models :as models]
            [taoensso.timbre :as timbre]))

(comment
  (remove-ns 'se.jherrlin.server.game-state)
  )

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

(defn player
  ([game-state player-id]          (get-in game-state                [:players player-id]))
  ([game-state subject player-id]  (get-in game-state [:games subject :players player-id])))

(defn bombs
  ([game-state]                   (get-in game-state                [:bombs]))
  ([game-state subject]           (get-in game-state [:games subject :bombs])))

(defn items
  ([game-state]                   (get-in game-state                [:items]))
  ([game-state subject]           (get-in game-state [:games subject :items])))

(defn flying-bombs
  ([game-state]                   (get-in game-state                [:flying-bombs]))
  ([game-state subject]           (get-in game-state [:games subject :flying-bombs])))

(defn games
  [game-state]                   (-> game-state (get-in [:games]) (vals)))

(defn game
  [game-state subject] (get-in game-state [:games subject]))

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
  (let [{:keys [game-id game-name timestamp]} data]
    (-> game-state
        (assoc-in [:games subject] (assoc (into {} data)
                                          :subject               subject
                                          :game-create-timestamp timestamp
                                          :game-state            :created))
        (assoc-in [:active-games game-name] game-id))))

(def player-positions
  {1 [1  1]
   2 [17 9]
   3 [1  9]
   4 [17 1]})

(defmethod projection :se.jherrlin.bomberman.game/join-game
  [game-state {:keys [subject data] :as event}]
  (let [players-in-game    (get-in game-state [:games subject :players])
        number-of-players  (count players-in-game)
        player-gets-number (inc number-of-players)]
    (assoc-in game-state
              [:games subject :players (:player-id data)]
              (-> data
                  (assoc :player-nr player-gets-number)
                  (assoc :position (get player-positions player-gets-number))))))

(defmethod projection :se.jherrlin.bomberman.game/start
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [timestamp]} data]
    (-> game-state
        (assoc-in [:games subject :game-state] :started)
        (assoc-in [:games subject :game-started-timestamp] timestamp))))

(defmethod projection :se.jherrlin.bomberman.game/end
  [game-state {:keys [subject data] :as event}]
  (let [end-timestamp (:timestamp data)
        game  (game game-state subject)
        game' (-> game
                  (assoc-in [:game-state] :ended)
                  (assoc-in [:end-timestamp] end-timestamp))]
    (-> game-state
        (assoc-in [:games subject] (select-keys game' [:game-id :game-name :game-state :game-started-timestamp :winner :end-timestamp]))
        (assoc-in [:old-games subject] game')
        (update-in [:active-games] dissoc (:game-name game)))))

(defmethod projection :se.jherrlin.bomberman.game/created-game-inactivity-timeout
  [game-state {:keys [subject data] :as event}]
  (update game-state :games dissoc subject))

(defmethod projection :se.jherrlin.bomberman.game/started-game-inactivity-timeout
  [game-state {:keys [subject data] :as event}]
  (update game-state :games dissoc subject))

(defmethod projection :se.jherrlin.bomberman.game/winner
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [winner]} data]
    (assoc-in game-state [:games subject :winner] winner)))

(defmethod projection :se.jherrlin.bomberman.player/wants-to-move
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [direction player-id]} data]
    (assoc-in game-state [:games subject :players player-id :user-facing-direction] direction)))

(defmethod projection :se.jherrlin.bomberman.player/picks-fire-inc-item
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [direction player-id new-fire-length item-position-xy]} data]
    (-> game-state
        (assoc-in  [:games subject :players player-id :fire-length] new-fire-length)
        (update-in [:games subject :items] #(remove (comp #{item-position-xy} :item-position-xy) %)))))

(defmethod projection :se.jherrlin.bomberman.player/dies
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [player-id killed-by-player-id]} data
        p (player game-state subject player-id)
        p (assoc p
                 :player-status :dead
                 :killed-by killed-by-player-id)]
    (-> game-state
        (assoc-in  [:games subject :dead-players player-id] p)
        (update-in [:games subject :players] dissoc player-id))))

(defmethod projection :se.jherrlin.bomberman.player/move
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [player-id next-position direction]} data]
    (assoc-in game-state [:games subject :players player-id :position] next-position)))

(defmethod projection :se.jherrlin.bomberman.game/bomb-to-add
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [player-id bomb-position-xy fire-length bomb-added-timestamp]} data
        data' (into {} data)]
    (update-in game-state [:games subject :bombs] conj data')))

(defmethod projection :se.jherrlin.bomberman.game/bomb-to-remove
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [bomb-position-xy]} data]
    (update-in game-state [:games subject :bombs] #(remove (comp #{bomb-position-xy} :bomb-position-xy) %))))

(defmethod projection :se.jherrlin.bomberman.game/stone-to-remove
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [position-xy]} data]
    (update-in game-state [:games subject :stones] #(remove (comp #{position-xy}) %))))

(defmethod projection :se.jherrlin.bomberman.game/fire-to-add
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [player-id fire-position-xy fire-start-timestamp]} data
        data' (into {} data)]
    (update-in game-state [:games subject :fire] conj data')))

(defmethod projection :se.jherrlin.bomberman.game/fire-to-remove
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [fire-position-xy]} data]
    (update-in game-state [:games subject :fire] #(remove (comp #{fire-position-xy} :fire-position-xy) %))))

(defmethod projection :default [game-state event]
  (timbre/trace "Error! Could not find projection for event:" event)
  game-state)

(defn the-projection [state events]
  (reduce
   (fn [gs m] (projection gs m)) state events))


(comment
  (def repl-game-id #uuid "c03e430f-2b24-4109-a923-08c986a682a8")
  (def player-1-ws-id #uuid "e677bf82-0137-4105-940d-6d74429d31b0")
  (def player-2-ws-id #uuid "663bd7a5-7220-40e5-b08d-597c43b89e0a")

  (let [timestamp #inst "2021-09-19T21:10:59.559-00:00"]
    (reduce
     (fn [gs m] (projection gs (.toCloudEvent m)))
     {}
     [(models/->CreateGame     timestamp        repl-game-id "First game" "my-secret" board/mini '() '())
      (models/->JoinGame       timestamp        repl-game-id player-1-ws-id "John")
      (models/->JoinGame       timestamp        repl-game-id player-2-ws-id "Hannah")
      (models/->StartGame      timestamp        repl-game-id )
      (models/->PlayerDies                      repl-game-id player-1-ws-id player-2-ws-id)
      (models/->PlayerPicksFireIncItemFromBoard timestamp repl-game-id player-1-ws-id [1 1] 3)
      (models/->PlayerMove     timestamp        repl-game-id player-1-ws-id [2 1] :east)
      (models/->PlayerMove     timestamp        repl-game-id player-1-ws-id [2 1] :east)
      (models/->StoneToRemove  timestamp        repl-game-id [3 3])
      (models/->GameWinner     timestamp        repl-game-id "John")
      (models/->EndGame                         repl-game-id nil)]))


  (let [timestamp #inst "2021-09-19T21:10:59.559-00:00"]
    (reduce
     (fn [gs m] (projection gs (.toCloudEvent m)))
     {}
     [(models/->CreateGame    timestamp         repl-game-id "First game" "my-secret" board/mini '() '())
      (models/->JoinGame      timestamp         repl-game-id player-1-ws-id "John")
      (models/->JoinGame      timestamp         repl-game-id player-2-ws-id "Hannah")
      (models/->StartGame                       repl-game-id #inst "2021-09-19T15:54:31.631-00:00")

      ;; (models/->PlayerPicksFireIncItemFromBoard timestamp repl-game-id player-1-ws-id [1 1] 3)
      ;; (PlayerMove.             repl-game-id player-1-ws-id [2 1] :east)
      ;; (PlayerMove.             repl-game-id player-1-ws-id [2 1] :east)
      ;; (StoneToRemove.          repl-game-id [3 3])
      ;; (EndGame.                repl-game-id nil)
      ]))

  (.toCloudEvent (EndGame. repl-game-id nil))
  )

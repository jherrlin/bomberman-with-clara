(ns se.jherrlin.claraman.game-state
  (:require [clojure.string :as str]
            [se.jherrlin.claraman.models :as models]
            [taoensso.timbre :as timbre]))


;; Access data in game state
(defn player-current-xy
  ([game-state player-id]         (get-in game-state                [:players player-id :position]))
  ([game-state subject player-id] (get-in game-state [:games subject :players player-id :position])))

(defn dead-player
  ([game-state player-id]         (get-in game-state                [:dead-players player-id]))
  ([game-state subject player-id] (get-in game-state [:games subject :dead-players player-id])))

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

(defn game-create-timestamp
  ([game-state]                   (get-in game-state                [:game-create-timestamp]))
  ([game-state subject]           (get-in game-state [:games subject :game-create-timestamp])))

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
                                          :game-state            :created)))))

(defmethod projection :se.jherrlin.bomberman.game/join-game
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [player-id]} data]
    (assoc-in game-state [:games subject :players player-id] data)))

(defmethod projection :se.jherrlin.bomberman.game/start
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [timestamp]} data]
    (-> game-state
        (assoc-in [:games subject :game-state] :started)
        (assoc-in [:games subject :game-started-timestamp] timestamp))))

(defmethod projection :se.jherrlin.bomberman.game/shutdown
  [game-state {:keys [subject data] :as event}]
  (assoc-in game-state [:games subject :game-state] :shutdown))

(defmethod projection :se.jherrlin.bomberman.game/end
  [game-state {:keys [subject data] :as event}]
  (let [game (game game-state subject)]
    (-> game-state
        (update :games dissoc subject)
        (update :past-games (fnil conj '()) (select-keys game [:game-id :game-name :winner])))))

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
    (when (and (game-create-timestamp game-state subject)
               (not (dead-player      game-state subject player-id))
               (player-current-xy     game-state subject player-id))
      (assoc-in game-state [:games subject :players player-id :user-facing-direction] direction))))

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
    (when-not (dead-player game-state player-id)
      (assoc-in game-state [:games subject :players player-id :position] next-position))))

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
    (when (game-create-timestamp game-state subject)
      (update-in game-state [:games subject :stones] #(remove (comp #{position-xy}) %)))))

(defmethod projection :se.jherrlin.bomberman.game/fire-to-add
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [player-id fire-position-xy fire-start-timestamp]} data
        data' (into {} data)]
    (when (game-create-timestamp game-state subject)
      (update-in game-state [:games subject :fire] conj data'))))

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


(defn game-facts [{:keys [game-id game-started-timestamp game-state] :as game}]
  (concat
   (when game-started-timestamp
     [(models/->GameStartedTimestamp game-started-timestamp game-id)])
   [(models/->Board game-id (board game))
    (models/->GameState game-id game-state)]
   (->> (players game)
        (vals)
        (filter (comp not #{:dead} :player-status))
        (map (fn [{:keys [player-id position player-name] :as player}]
               (models/->PlayerOnBoardPosition game-id player-id position player-name))))
   (->> (players game)
        (vals)
        (filter (comp not #{:dead} :player-status))
        (map (fn [{:keys [player-id position fire-length] :as player}]
               (models/->PlayerOnBoardFireLength game-id player-id position fire-length))))
   (->> (stones game)
        (map (partial models/->Stone game-id)))
   (->> (bombs game)
        (map models/map->BombOnBoard))
   (->> (items game)
        (map (fn [{:keys [item-position-xy item-power]}]
               (models/->ItemOnBoard game-id item-position-xy item-power))))
   (->> (fires game)
        (map models/map->FireOnBoard))))

(defn active-game-fact [{:keys [game-name game-id password game-state] :as game}]
  (models/->ActiveGame game-id game-name password game-state))

(defn running-games [games]
  (filter (comp not #{:ended} :game-state) games))

(defn active-game-facts [gs]
  (->> gs
       (games)
       (running-games)
       (map active-game-fact)))

(defn games-facts [game-state]
  (->> game-state
       (games)
       (running-games)
       (map game-facts)
       (apply concat)))

(defn started-games-facts [game-state]
  (->> game-state
       (games)
       (filter (comp #{:started} :game-state))
       (map game-facts)
       (apply concat)))

(defn created-games-facts [game-state]
  (->> game-state
       (games)
       (filter (comp #{:created} :game-state))
       (map (fn [{:keys [game-create-timestamp game-id]}]
              (models/->GameCreatedTimestamp game-create-timestamp game-id)))))

(defn shutdown-games-facts [game-state]
  (->> game-state
       (games)
       (filter (comp #{:shutdown} :game-state))
       (map (fn [{:keys [game-id]}]
              (models/->GameIsInShutdown game-id)))))

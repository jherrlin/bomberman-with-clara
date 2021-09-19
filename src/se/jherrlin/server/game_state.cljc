(ns se.jherrlin.server.game-state
  (:require [clojure.string :as str]
            [se.jherrlin.server.models :as models]))

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

(defn game-to-active-game-facts [{:keys [game-name game-id password game-state] :as game}]
  (models/->ActiveGame game-id game-name password game-state))

(defn game-state->active-game-facts [gs]
  (->> gs
       (games)
       (map (fn [{:keys [game-name game-id password game-state]}]
              (models/->ActiveGame game-id game-name password game-state)))))

(defn game-to-facts [{:keys [stones board players game-id game-state bombs items fire] :as game}]
  (concat
   [(models/->Board game-id board)
    (models/->GameState game-id game-state)]
   (->> players
        (vals)
        (filter (comp not #{:dead} :player-status))
        (map (fn [{:keys [player-id position] :as player}]
               (models/->PlayerOnBoardPosition game-id player-id position))))
   (->> players
        (vals)
        (filter (comp not #{:dead} :player-status))
        (map (fn [{:keys [player-id position fire-length] :as player}]
               (models/->PlayerOnBoardFireLength game-id player-id position fire-length))))
   (->> stones
        (map (partial models/->Stone game-id)))
   (->> bombs
        (map models/map->BombOnBoard))
   (->> items
        (map (fn [{:keys [item-position-xy item-power]}]
               (models/->ItemOnBoard game-id item-position-xy item-power))))
   (->> fire
        (map models/map->FireOnBoard))))

(defn game-state->enginge-facts-1 [gs]
  (->> [gs]
       (filter (comp #{:started :created} :game-state))
       (map (fn [{:keys [subject game-id game-state] :as game}]
              (concat
               [(models/->Board subject (board game))
                (models/->GameState game-id game-state)]
               (->> (players game)
                    (vals)
                    (filter (comp not #{:dead} :player-status))
                    (map (fn [{:keys [player-id position] :as player}]
                           (models/->PlayerOnBoardPosition subject player-id position))))
               (->> (players game)
                    (vals)
                    (filter (comp not #{:dead} :player-status))
                    (map (fn [{:keys [player-id position fire-length] :as player}]
                           (models/->PlayerOnBoardFireLength subject player-id position fire-length))))
               (->> (stones game)
                    (map (partial models/->Stone subject)))
               (->> (bombs game)
                    (map models/map->BombOnBoard))
               (->> (items game)
                    (map (fn [{:keys [item-position-xy item-power]}]
                           (models/->ItemOnBoard game-id item-position-xy item-power))))
               (->> (fires game)
                    (map models/map->FireOnBoard)))))
       (apply concat)))

(defn game-state->enginge-facts [gs]
  (->> gs
       :games
       vals
       (filter (comp #{:started :created} :game-state))
       (map (fn [{:keys [subject game-id game-state] :as game}]
              (concat
               [(models/->Board subject (board game))
                (models/->GameState game-id game-state)]
               (->> (players game)
                    (vals)
                    (filter (comp not #{:dead} :player-status))
                    (map (fn [{:keys [player-id position] :as player}]
                           (models/->PlayerOnBoardPosition subject player-id position))))
               (->> (players game)
                    (vals)
                    (filter (comp not #{:dead} :player-status))
                    (map (fn [{:keys [player-id position fire-length] :as player}]
                           (models/->PlayerOnBoardFireLength subject player-id position fire-length))))
               (->> (stones game)
                    (map (partial models/->Stone subject)))
               (->> (bombs game)
                    (map models/map->BombOnBoard))
               (->> (items game)
                    (map (fn [{:keys [item-position-xy item-power]}]
                           (models/->ItemOnBoard game-id item-position-xy item-power))))
               (->> (fires game)
                    (map models/map->FireOnBoard)))))
       (apply concat)))

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
  (let [{:keys [game-id game-name]} data]
    (-> game-state
        (assoc-in [:games subject] (assoc (into {} data) :subject subject))
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
        (assoc-in [:games subject :started-timestamp] timestamp))))

(defmethod projection :se.jherrlin.bomberman.game/end
  [game-state {:keys [subject data] :as event}]
  (let [end-timestamp (:timestamp data)
        game  (game game-state subject)
        game' (-> game
                  (assoc-in [:game-state] :ended)
                  (assoc-in [:end-timestamp end-timestamp] :ended))]
    (-> game-state
        (assoc-in [:games subject] (select-keys game' [:game-id :game-name :game-state]))
        (assoc-in [:old-games subject] game')
        (update-in [:active-games] dissoc (:game-name game)))))

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
  (println "Error! Could not find projection for event:" event)
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
     [(models/->CreateGame                      repl-game-id "First game" "my-secret")
      (models/->JoinGame                        repl-game-id player-1-ws-id "John")
      (models/->JoinGame                        repl-game-id player-2-ws-id "Hannah")
      (models/->StartGame                       repl-game-id #inst "2021-09-19T15:54:31.631-00:00")
      (models/->PlayerDies                      repl-game-id player-1-ws-id player-2-ws-id)
      (models/->PlayerPicksFireIncItemFromBoard repl-game-id player-1-ws-id [1 1] 3)
      (models/->PlayerMove     timestamp        repl-game-id player-1-ws-id [2 1] :east)
      (models/->PlayerMove     timestamp        repl-game-id player-1-ws-id [2 1] :east)
      (models/->StoneToRemove                   repl-game-id [3 3])
      (models/->GameWinner                      repl-game-id "John")
      (models/->EndGame                         repl-game-id nil)]))


  (reduce
   (fn [gs m] (projection gs (.toCloudEvent m)))
   {}
   [(models/->CreateGame                      repl-game-id "First game" "my-secret")
    (models/->JoinGame                        repl-game-id player-1-ws-id "John")
    (models/->JoinGame                        repl-game-id player-2-ws-id "Hannah")
    (models/->StartGame                       repl-game-id #inst "2021-09-19T15:54:31.631-00:00")

    ;; (models/->PlayerPicksFireIncItemFromBoard repl-game-id player-1-ws-id [1 1] 3)
    ;; (PlayerMove.             repl-game-id player-1-ws-id [2 1] :east)
    ;; (PlayerMove.             repl-game-id player-1-ws-id [2 1] :east)
    ;; (StoneToRemove.          repl-game-id [3 3])
    ;; (EndGame.                repl-game-id nil)
    ])

  (.toCloudEvent (EndGame. repl-game-id nil))
  )

(ns se.jherrlin.server.models
  (:require [se.jherrlin.clara-labs.board :as board]
            [se.jherrlin.datetime :as datetime]))


(defn rand-uuid []
  #?(:cljs (random-uuid)
     :clj (java.util.UUID/randomUUID)))

(defn template
  ([source subject type]
   (template (datetime/now) source subject type nil))
  ([source subject type data]
   (template (datetime/now) source subject type data))
  ([time source subject type data]
   (cond-> {:id           (rand-uuid) ;; event id
            :source       source
            :subject      subject
            :type         type
            :time         time
            :content-type "application/edn"}
     data (assoc :data (into {} data)))))

(defprotocol CloudEvent
  (toCloudEvent [this]))


(defrecord TimestampNow          [now])
(defrecord Board                 [game-id board])
(defrecord FireOnBoard           [game-id player-id fire-position-xy fire-start-timestamp])
(defrecord BombOnBoard           [game-id player-id bomb-position-xy fire-length bomb-added-timestamp])
(defrecord PlayerOnBoardPosition [game-id player-id player-current-xy])
(defrecord PlayerOnBoardFireLength  [game-id player-id player-position-xy fire-length])
(defrecord ItemOnBoard              [game-id item-position-xy item-power])

(defrecord BombExploading [timestamp game-id player-id position-xy fire-length]
  CloudEvent
  (toCloudEvent [this]
    (template
     timestamp "urn:se:jherrlin:bomberman:game" game-id "bomb-exploading" this)))

(defrecord BombToAdd [timestamp game-id player-id bomb-position-xy fire-length bomb-added-timestamp]
  CloudEvent
  (toCloudEvent [this]
    (template
     timestamp "urn:se:jherrlin:bomberman:game" game-id "bomb-to-add" this)))

(defrecord PlayerDies [timestamp game-id player-id killed-by-player-id]
  CloudEvent
  (toCloudEvent [this]
    (template
     timestamp "urn:se:jherrlin:bomberman:player" game-id "dies" this)))

(defrecord FireToAdd [timestamp game-id player-id fire-position-xy fire-start-timestamp]
  CloudEvent
  (toCloudEvent [this]
    (template
     timestamp "urn:se:jherrlin:bomberman:game" game-id "fire-to-add" this)))

(defrecord FlyingBomb [timestamp game-id player-id flying-bomb-current-xy fire-length bomb-added-timestamp flying-bomb-direction]
  CloudEvent
  (toCloudEvent [this]
    (template
     timestamp "urn:se:jherrlin:bomberman:game" game-id "flying-bomb" this)))

(defrecord PlayerMove [timestamp game-id player-id next-position direction]
  CloudEvent
  (toCloudEvent [this]
    (template
     timestamp "urn:se:jherrlin:bomberman:player" game-id "move" this)))

(defrecord PlayerWantsToMove [timestamp game-id player-id current-xy direction]
  CloudEvent
  (toCloudEvent [this]
    (template
     timestamp "urn:se:jherrlin:bomberman:player" game-id "wants-to-move" this)))

(defrecord PlayerWantsToPlaceBomb [timestamp game-id player-id current-xy fire-length max-nr-of-bombs-for-player]
  CloudEvent
  (toCloudEvent [this]
    (template
     timestamp "urn:se:jherrlin:bomberman:player" game-id "wants-to-place-bomb" this)))

(defrecord PlayerWantsToThrowBomb [timestamp game-id player-id players-current-xy player-facing-direction]
  CloudEvent
  (toCloudEvent [this]
    (template
     timestamp "urn:se:jherrlin:bomberman:player" game-id "wants-to-throw-bomb" this)))

(defrecord Stone [game-id stone-position-xy]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:game" game-id "stone" this)))

(defrecord PlayerPicksFireIncItemFromBoard [timestamp game-id player-id item-position-xy new-fire-length]
  CloudEvent
  (toCloudEvent [this]
    (template
     timestamp "urn:se:jherrlin:bomberman:player" game-id "picks-fire-inc-item" this)))

(defrecord StoneToRemove [game-id position-xy]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:game" game-id "stone-to-remove" this)))

(defrecord FireToRemove [game-id fire-position-xy]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:game" game-id "fire-to-remove" this)))

(defrecord BombToRemove [game-id bomb-position-xy]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:game" game-id "bomb-to-remove" this)))

(def reserved-floors
  #{[1 1]
    [1 2]
    [2 1]

    [1 8]
    [1 9]
    [2 9]

    [16 1]
    [17 1]
    [17 2]

    [17 8]
    [17 9]
    [16 9]})

(defn generate-stones [board reserved-positions]
  (->> (flatten board)
       (filter (comp #{:floor} :type))
       (map (fn [{:keys [x y]}]
              (let [n (rand-int 10)]
                (cond
                  (#{0 1 2 3} n)     nil
                  (#{4 5 6 7 8 9} n) [x y]))))
       (remove reserved-positions)
       (remove nil?)))

(defn generate-items [stones]
  (->> stones
       (map (fn [[x y]]
              (let [n (rand-int 10)]
                (cond
                  (#{0 1}             n) {:item-position-xy [x y] :item-power :inc-fire-length}
                  (#{2 3 4 5 6 7 8 9} n) nil))))
       (remove nil?)))

(comment
  (generate-items (generate-stones board/board2 reserved-floors))
  )

(defrecord WantsToCreateGame [game-id game-name password])
(defrecord ActiveGame        [game-id game-name password game-state])
(defrecord CreateGameError   [game-id game-name message])
(defrecord CreateGame        [game-id game-name password]
  CloudEvent (toCloudEvent [this]
               (let [stones   (generate-stones board/board2 reserved-floors)
                     defaults {:game-state    :created
                               :game-name     game-name
                               :game-password password
                               :board         board/board2
                               :stones        stones
                               :fire          '()
                               :flying-bombs  '()
                               :bombs         '()
                               :items         (generate-items stones)}]
                 (template "urn:se:jherrlin:bomberman:game" game-id "create-game" (merge defaults this)))))

(defrecord GameState [game-id game-state])
(defrecord PlayerWantsToJoinGame [player-id player-name game-id password])
(defrecord JoinGameError [game-id message])
(defrecord JoinGame [game-id player-id player-name]
  CloudEvent (toCloudEvent [this]
               (let [default {:user-facing-direction    :south
                              :max-nr-of-bombs-for-user 3
                              :position                 [1 1]
                              :fire-length              2}]
                 (template "urn:se:jherrlin:bomberman:game" game-id "join-game" (merge default this)))))

(defrecord PlayerWantsToStartGame [game-id])
(defrecord StartGameError [game-id message])
(defrecord StartGame [game-id timestamp]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:game" game-id "start" this)))

(defrecord EndGame [game-id timestamp]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:game" game-id "end" this)))

(defrecord GameWinner [game-id winner]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:game" game-id "winner" this)))


#?(:clj (compile 'se.jherrlin.server.models))

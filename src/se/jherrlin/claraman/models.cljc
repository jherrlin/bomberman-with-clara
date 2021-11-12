(ns se.jherrlin.claraman.models
  (:require
   [se.jherrlin.datetime :as datetime]))


(defn rand-uuid []
  #?(:cljs (random-uuid)
     :clj (java.util.UUID/randomUUID)))

(defn to-cloud-event
  ([source subject type]
   (to-cloud-event (datetime/now) source subject type nil))
  ([source subject type data]
   (to-cloud-event (datetime/now) source subject type data))
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


(defrecord TimestampNow             [now])
(defrecord Board                    [game-id board])
(defrecord FireOnBoard              [game-id player-id fire-position-xy fire-start-timestamp])
(defrecord BombOnBoard              [game-id player-id bomb-position-xy fire-length bomb-added-timestamp])
(defrecord PlayerOnBoardPosition    [game-id player-id player-current-xy player-name])
(defrecord PlayerOnBoardFireLength  [game-id player-id player-position-xy fire-length])
(defrecord ItemOnBoard              [game-id item-position-xy item-power])

(defrecord BombExploading [timestamp game-id player-id position-xy fire-length]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:game" game-id "bomb-exploading" this)))

(defrecord BombToAdd [timestamp game-id player-id bomb-position-xy fire-length bomb-added-timestamp]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:game" game-id "bomb-to-add" this)))

(defrecord PlayerDies [timestamp game-id player-id killed-by-player-id]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:player" game-id "dies" this)))

(defrecord FireToAdd [timestamp game-id player-id fire-position-xy fire-start-timestamp]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:game" game-id "fire-to-add" this)))

(defrecord FlyingBomb [timestamp game-id player-id flying-bomb-current-xy fire-length bomb-added-timestamp flying-bomb-direction]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:game" game-id "flying-bomb" this)))

(defrecord PlayerMove [timestamp game-id player-id next-position direction]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:player" game-id "move" this)))

(defrecord PlayerWantsToMove [timestamp game-id player-id current-xy direction]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:player" game-id "wants-to-move" this)))

(defrecord PlayerWantsToPlaceBomb [timestamp game-id player-id current-xy fire-length max-nr-of-bombs-for-player]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:player" game-id "wants-to-place-bomb" this)))

(defrecord PlayerWantsToThrowBomb [timestamp game-id player-id players-current-xy player-facing-direction]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:player" game-id "wants-to-throw-bomb" this)))

(defrecord Stone [game-id stone-position-xy]
  CloudEvent (toCloudEvent [this]
               (to-cloud-event "urn:se:jherrlin:bomberman:game" game-id "stone" this)))

(defrecord PlayerPicksFireIncItemFromBoard [timestamp game-id player-id item-position-xy new-fire-length]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:player" game-id "picks-fire-inc-item" this)))

(defrecord StoneToRemove [timestamp game-id position-xy]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:game" game-id "stone-to-remove" this)))

(defrecord FireToRemove [timestamp game-id fire-position-xy]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:game" game-id "fire-to-remove" this)))

(defrecord BombToRemove [timestamp game-id bomb-position-xy]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:game" game-id "bomb-to-remove" this)))

(defrecord WantsToCreateGame [game-id game-name password board stones items])
(defrecord ActiveGame        [game-id game-name password game-state])
(defrecord CreateGameError   [game-id game-name message])

(defrecord CreateGame [timestamp game-id game-name password board stones items]
  CloudEvent
  (toCloudEvent [this]
    (let [defaults {:game-state    :created
                    :game-name     game-name
                    :game-password password
                    :board         board
                    :stones        stones
                    :fire          '()
                    :flying-bombs  '()
                    :bombs         '()
                    :items         items}]
      (to-cloud-event
       timestamp "urn:se:jherrlin:bomberman:game" game-id "create-game" (merge defaults this)))))

(defrecord GameState [game-id game-state])
(defrecord PlayerWantsToJoinGame [player-id player-name game-id password])
(defrecord JoinGameError [game-id message])
(defrecord JoinGame [timestamp game-id player-id player-name player-nr position]
  CloudEvent
  (toCloudEvent [this]
    (let [default {:user-facing-direction    :south
                   :max-nr-of-bombs-for-user 3
                   :position                 [1 1]
                   :fire-length              2}]
      (to-cloud-event
       timestamp "urn:se:jherrlin:bomberman:game" game-id "join-game" (merge default this)))))

(defrecord PlayerWantsToStartGame [game-id])
(defrecord StartGameError [game-id message])
(defrecord StartGame [timestamp game-id]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:game" game-id "start" this)))

(defrecord EndGame [timestamp game-id]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:game" game-id "end" this)))

(defrecord GameWinner [timestamp game-id winner]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:game" game-id "winner" this)))

(defrecord GameIsInShutdown [game-id])
(defrecord GameShutdown     [timestamp game-id]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:game" game-id "shutdown" this)))

(defrecord CreatedGameInactivityTimeout [timestamp game-id reason]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:game" game-id "created-game-inactivity-timeout" this)))

(defrecord StartedGameInactivityTimeout [timestamp game-id reason]
  CloudEvent
  (toCloudEvent [this]
    (to-cloud-event
     timestamp "urn:se:jherrlin:bomberman:game" game-id "started-game-inactivity-timeout" this)))

(defrecord GameStartedTimestamp [timestamp game-id])
(defrecord GameCreatedTimestamp [timestamp game-id])

#?(:clj (compile 'se.jherrlin.claraman.models))

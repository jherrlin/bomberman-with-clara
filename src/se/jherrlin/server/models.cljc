(ns se.jherrlin.server.models
  (:require [se.jherrlin.clara-labs.board :as board])
  (:gen-class))


(defn template
  ([source subject type]
   (template source subject type nil))
  ([source subject type data]
   (cond-> {:id           (java.util.UUID/randomUUID) ;; event id
            :source       source
            :subject      subject
            :type         type
            :time         (java.util.Date.)
            :content-type "application/edn"}
     data (assoc :data (into {} data)))))

(defprotocol CloudEvent
  (toCloudEvent [this]))


(defrecord TimestampNow [now])
(defrecord Board        [game-id board]
  CloudEvent
  (toCloudEvent [this]
    (template
     "urn:se:jherrlin:bomberman:game" game-id "board" this)))

(defrecord BombExploading [game-id player-id position-xy fire-length]
  CloudEvent
  (toCloudEvent [this]
    (template
     "urn:se:jherrlin:bomberman:game" game-id "bomb-exploading" this)))

(defrecord BombOnBoard [game-id player-id bomb-position-xy fire-length bomb-added-timestamp]
  CloudEvent
  (toCloudEvent [this]
    (template "urn:se:jherrlin:bomberman:game" game-id "bomb-on-board" this)))

(defrecord BombToAdd [game-id player-id bomb-position-xy fire-length bomb-added-timestamp]
  CloudEvent
  (toCloudEvent [this]
    (template "urn:se:jherrlin:bomberman:game" game-id "bomb-to-add" this)))

(defrecord DeadPlayer [game-id player-id killed-by-player-id]
  CloudEvent
  (toCloudEvent [this]
    (template "urn:se:jherrlin:bomberman:game" game-id "dead-player" this)))

(defrecord FireOnBoard [game-id player-id fire-position-xy fire-start-timestamp]
  CloudEvent
  (toCloudEvent [this]
    (template
     "urn:se:jherrlin:bomberman:game" game-id "fire-on-board" this)))

(defrecord FireToAdd [game-id player-id fire-position-xy fire-start-timestamp]
  CloudEvent
  (toCloudEvent [this]
    (template
     "urn:se:jherrlin:bomberman:game" game-id "fire-to-add" this)))

(defrecord FlyingBomb [game-id player-id flying-bomb-current-xy fire-length bomb-added-timestamp flying-bomb-direction]
  CloudEvent
  (toCloudEvent [this]
    (template
     "urn:se:jherrlin:bomberman:game" game-id "flying-bomb" this)))

(defrecord PlayerMove [game-id player-id next-position direction]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:player" game-id "move" this)))

(defrecord PlayerPositionOnBoard [game-id player-id player-current-xy]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:player" game-id "position-on-board" this)))

(defrecord PlayerWantsToMove [game-id player-id current-xy direction]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:player" game-id "wants-to-move" this)))

(defrecord PlayerWantsToPlaceBomb [game-id player-id current-xy fire-length timestamp max-nr-of-bombs-for-player]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:player" game-id "wants-to-place-bomb" this)))

(defrecord PlayerWantsToThrowBomb [game-id player-id players-current-xy       player-facing-direction]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:player" game-id "wants-to-throw-bomb" this)))

(defrecord Stone [game-id stone-position-xy]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:game" game-id "stone" this)))

(defrecord StoneToRemove [game-id position-xy]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:game" game-id "stone-to-remove" this)))

(defrecord FireToRemove [game-id fire-position-xy]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:game" game-id "fire-to-remove" this)))

(defrecord BombToRemove [game-id bomb-position-xy]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:game" game-id "bomb-to-remove" this)))

(defrecord CreateGame [game-id game-name password]
  CloudEvent (toCloudEvent [this]
               (let [defaults {:game-state    :created
                               :game-name     game-name
                               :game-password password
                               :board         board/board2
                               :stones        [[1 5] [2 5] [3 4] [3 3] [5 1]]
                               :fire         '()
                               :flying-bombs '()
                               :bombs        '()}]
                 (template "urn:se:jherrlin:bomberman:game" game-id "create-game" (merge defaults this)))))

(defrecord JoinGame [game-id player-id player-name]
  CloudEvent (toCloudEvent [this]
               (let [default {:user-facing-direction    :south
                              :max-nr-of-bombs-for-user 3
                              :position                 [1 1]
                              :fire-length              3}]
                 (template "urn:se:jherrlin:bomberman:game" game-id "join-game" (merge default this)))))

(defrecord StartGame [game-id]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:game" game-id "start")))

(defrecord EndGame [game-id winner]
  CloudEvent (toCloudEvent [this]
               (template "urn:se:jherrlin:bomberman:game" game-id "end" this)))


(compile 'se.jherrlin.server.models)

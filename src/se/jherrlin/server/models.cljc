(ns se.jherrlin.server.models
  (:require [se.jherrlin.server.events :as events])
  (:gen-class))



(defprotocol CloudEvent
  (toCloudEvent [this]))


(defrecord TimestampNow [now])
(defrecord Board        [game-id board]
  CloudEvent
  (toCloudEvent [this]
    (events/template
     "urn:se:jherrlin:bomberman:game" game-id "board" this)))

(defrecord BombExploading [game-id player-id position-xy fire-length]
  CloudEvent
  (toCloudEvent [this]
    (events/template
     "urn:se:jherrlin:bomberman:game" game-id "bomb-exploading" this)))

(defrecord BombOnBoard [game-id player-id bomb-position-xy fire-length bomb-added-timestamp]
  CloudEvent
  (toCloudEvent [this]
    (events/template "urn:se:jherrlin:bomberman:game" game-id "bomb-on-board" this)))

(defrecord DeadPlayer [game-id player-id killed-by-player-id]
  CloudEvent
  (toCloudEvent [this]
    (events/template "urn:se:jherrlin:bomberman:game" game-id "dead-player" this)))

(defrecord FireOnBoard [game-id player-id fire-position-xy fire-start-timestamp]
  CloudEvent
  (toCloudEvent [this]
    (events/template
     "urn:se:jherrlin:bomberman:game" game-id "fire-on-board" this)))

(defrecord FlyingBomb [game-id player-id flying-bomb-current-xy fire-length bomb-added-timestamp flying-bomb-direction]
  CloudEvent
  (toCloudEvent [this]
    (events/template
     "urn:se:jherrlin:bomberman:game" game-id "flying-bomb" this)))

(defrecord PlayerMove [game-id player-id next-position direction]
  CloudEvent (toCloudEvent [this]
               (events/template "urn:se:jherrlin:bomberman:player" game-id "move" this)))

(defrecord PlayerPositionOnBoard [game-id player-id player-current-xy]
  CloudEvent (toCloudEvent [this]
               (events/template "urn:se:jherrlin:bomberman:player" game-id "position-on-board" this)))

(defrecord PlayerWantsToMove [game-id player-id current-xy direction]
  CloudEvent (toCloudEvent [this]
               (events/template "urn:se:jherrlin:bomberman:player" game-id "wants-to-move" this)))

(defrecord PlayerWantsToPlaceBomb [game-id player-id current-xy fire-length timestamp max-nr-of-bombs-for-player]
  CloudEvent (toCloudEvent [this]
               (events/template "urn:se:jherrlin:bomberman:player" game-id "wants-to-place-bomb" this)))

(defrecord PlayerWantsToThrowBomb [game-id player-id players-current-xy       player-facing-direction]
  CloudEvent (toCloudEvent [this]
               (events/template "urn:se:jherrlin:bomberman:player" game-id "wants-to-throw-bomb" this)))

(defrecord Stone [game-id stone-position-xy]
  CloudEvent (toCloudEvent [this]
               (events/template "urn:se:jherrlin:bomberman:game" game-id "stone" this)))

(defrecord StoneToRemove [game-id position-xy]
  CloudEvent (toCloudEvent [this]
               (events/template "urn:se:jherrlin:bomberman:game" game-id "stone-to-remove" this)))

(defrecord FireToRemove           [game-id position-xy]
  CloudEvent (toCloudEvent [this]
               (events/template "urn:se:jherrlin:bomberman:game" game-id "fire-to-remove" this)))

(defrecord BombToRemove           [game-id position-xy]
  CloudEvent (toCloudEvent [this]
               (events/template "urn:se:jherrlin:bomberman:game" game-id "bomb-to-remove" this)))


(compile 'se.jherrlin.server.models)

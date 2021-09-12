(ns se.jherrlin.server.application-service
  (:import [se.jherrlin.server.models
            PlayerMove StoneToRemove FireToRemove BombToRemove BombExploading FireOnBoard DeadPlayer BombOnBoard FlyingBomb
            CreateGame JoinGame StartGame EndGame PlayerWantsToPlaceBomb]))



(defn create-game [add-event-fn! game-name password]
  (let [subject (java.util.UUID/randomUUID)]
    (add-event-fn! (CreateGame. subject game-name password))))

(defn join-game [add-event-fn! subject player-id player-name]
  (add-event-fn! (JoinGame. subject player-id player-name)))

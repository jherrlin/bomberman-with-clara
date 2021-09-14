(ns se.jherrlin.server.application-service
  (:import [se.jherrlin.server.models
            PlayerMove StoneToRemove FireToRemove BombToRemove BombExploading FireOnBoard DeadPlayer BombOnBoard FlyingBomb
            CreateGame JoinGame StartGame EndGame PlayerWantsToPlaceBomb]))



(defn create-game! [game-state projection-fn add-events-fn! game-name password]
  (def game-state game-state)
  (if (contains? (->> @game-state :active-games) game-name)
    {:status  :error
     :message "Game name already exists."}
    (let [subject (java.util.UUID/randomUUID)]
      (add-events-fn! [(.toCloudEvent (CreateGame. subject game-name password))])
      {:status :ok})))

(defn join-game! [add-event-fn! subject player-id player-name]
  (add-event-fn! (JoinGame. subject player-id player-name)))

(ns se.jherrlin.server.application-service
  (:require [clojure.spec.alpha :as s]
            [se.jherrlin.server.game-state :as game-state]
            [se.jherrlin.server.user-commands :as user-commands]
            [se.jherrlin.server.models :as models]
            [se.jherrlin.clara-labs.bomberman-rules :as bomberman-rules]
            [taoensso.timbre :as timbre])
  (:import [se.jherrlin.server.models
            PlayerMove StoneToRemove FireToRemove BombToRemove BombExploading FireOnBoard DeadPlayer BombOnBoard FlyingBomb
            CreateGame JoinGame StartGame EndGame PlayerWantsToPlaceBomb]))


(defn game-state->active-game-facts [gs]
  (->> gs
       (game-state/games)
       (map (fn [{:keys [game-name game-id password game-state]}]
              (models/->ActiveGame game-id game-name password game-state)))))

(defn create-game! [game-state add-events-fn! create-event-data]
  (def game-state game-state)
  (def add-events-fn! add-events-fn!)
  (def create-event-data create-event-data)
  (if-not (s/valid? ::user-commands/create-game create-event-data)
    (s/explain-data ::user-commands/create-game create-event-data)
    (let [{:keys [game-name game-password]} create-event-data
          game-id                           (java.util.UUID/randomUUID)
          player-wants-to-create-game       (models/->WantsToCreateGame game-id game-name game-password)
          {:keys [create-game-errors create-games] :as actions}
          (bomberman-rules/run-create-game-rules
           (concat (game-state->active-game-facts @game-state)
                   [player-wants-to-create-game]))]
      (cond
        (seq create-game-errors)
        (-> create-game-errors first :message)
        (seq create-games)
        (add-events-fn! [(some->> create-games first ((fn [x] (.toCloudEvent x))))])
        :else
        (timbre/error "No actions from enginge on:" create-event-data)))))

(defn join-game! [game-state add-events-fn! join-game-event-data]
  (def game-state game-state)
  (def add-events-fn! add-events-fn!)
  (def join-game-event-data join-game-event-data)
  (if-not (s/valid? ::user-commands/join-game join-game-event-data)
    (s/explain-data ::user-commands/join-game join-game-event-data)
    (let [{:keys [game-name game-password player-name]} join-game-event-data
          player-id                                     (java.util.UUID/randomUUID)
          player-wants-to-join-game                     (models/->PlayerWantsToJoinGame player-id player-name game-name game-password)
          {:keys [join-game-errors join-games] :as actions}
          (bomberman-rules/run-join-game-rules
           (concat (game-state->active-game-facts @game-state)
                   [player-wants-to-join-game]))]
      actions
      (cond
        (seq join-game-errors)
        {:status  :error
         :message (-> join-game-errors first :message)}
        (seq join-games)
        (do
          (add-events-fn! [(some->> join-games first ((fn [x] (.toCloudEvent x))))])
          {:status :ok
           :data   (->> join-games first (into {}))})
        :else
        (do (timbre/error "No actions from enginge on:" join-game-event-data)
            {:status  :error
             :message "unknown"}))
      ;;(->> join-games first (into {}))
      ))
  )

{:action        :join-game
 :game-name     "asd"
 :game-password "pwd"
 :player-name   "John"}

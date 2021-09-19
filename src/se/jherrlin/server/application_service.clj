(ns se.jherrlin.server.application-service
  (:require [clojure.spec.alpha :as s]
            [se.jherrlin.server.game-state :as game-state]
            [se.jherrlin.server.user-commands :as user-commands]
            [se.jherrlin.server.models :as models]
            [se.jherrlin.clara-labs.bomberman-rules :as bomberman-rules]
            [taoensso.timbre :as timbre]
            [se.jherrlin.datetime :as datetime]))


(defn create-game! [game-state add-events-fn! create-event-data]
  (if-not (s/valid? ::user-commands/create-game create-event-data)
    {:status :error
     :message
     (s/explain-data ::user-commands/create-game create-event-data)}
    (let [{:keys [game-name game-password]} create-event-data
          game-id                           (java.util.UUID/randomUUID)
          player-wants-to-create-game       (models/->WantsToCreateGame game-id game-name game-password)
          {:keys [create-game-errors create-games] :as actions}
          (bomberman-rules/run-create-game-rules
           (concat (game-state/game-state->active-game-facts @game-state)
                   [player-wants-to-create-game]))]
      (cond
        (seq create-game-errors)
        {:status  :error
         :message (-> create-game-errors first :message)}
        (seq create-games)
        (do
          (add-events-fn! [(some->> create-games first ((fn [x] (.toCloudEvent x))))])
          {:status :ok
           :data   (->> create-games first (into {}))})
        :else
        (do (timbre/error "No actions from enginge on:" create-event-data)
            {:status  :error
             :message "unknown"})))))

(defn start-game! [game-state add-events-fn! start-game-event-data]
  (def game-state game-state)
  (def add-events-fn! add-events-fn!)
  (def start-game-event-data start-game-event-data)
  (if-not (s/valid? ::user-commands/start-game start-game-event-data)
    {:status :error
     :message
     (s/explain-data ::user-commands/start-game start-game-event-data)}
    (let [{:keys [game-id]}          start-game-event-data
          _                          (def game-id game-id)
          player-wants-to-start-game (models/->PlayerWantsToStartGame game-id)
          _                          (def player-wants-to-start-game player-wants-to-start-game)
          game                       (game-state/game @game-state game-id)
          _                          (def game game)
          {:keys [start-game-errors start-games] :as actions}
          (bomberman-rules/run-start-game-rules
           (concat (game-state/game-to-facts game)
                   [(game-state/game-to-active-game-facts game)]
                   [(models/->TimestampNow (datetime/now))]
                   [player-wants-to-start-game]))
          _                          (def actions actions)
          _                          (def start-game-errors start-game-errors)
          _                          (def start-games start-games)]
      (cond
        (seq start-game-errors)
        {:status  :error
         :message (-> start-game-errors first :message)}
        (seq start-games)
        (do
          (add-events-fn! [(some->> start-games first ((fn [x] (.toCloudEvent x))))])
          {:status :ok
           :data   (->> start-games first (into {}))})
        :else
        (do (timbre/error "No actions from enginge on:" start-game-event-data)
            {:status  :error
             :message "unknown"})))))

(defn join-game! [game-state add-events-fn! join-game-event-data]
  (if-not (s/valid? ::user-commands/join-game join-game-event-data)
    {:status :error
     :message
     (s/explain-data ::user-commands/join-game join-game-event-data)}
    (let [{:keys [game-password game-id player-name]}       join-game-event-data
          player-id                                         (java.util.UUID/randomUUID)
          game                                              (game-state/game @game-state game-id)
          player-wants-to-join-game                         (models/->PlayerWantsToJoinGame player-id player-name game-id game-password)
          {:keys [join-game-errors join-games] :as actions} (bomberman-rules/run-join-game-rules
                                                             (concat
                                                              (game-state/game-to-facts game)
                                                              [(game-state/game-to-active-game-facts game)]
                                                              [player-wants-to-join-game]))]
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
             :message "unknown"})))))

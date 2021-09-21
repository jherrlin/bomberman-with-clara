(ns se.jherrlin.server.application-service
  (:require [clojure.spec.alpha :as s]
            [se.jherrlin.server.game-state :as game-state]
            [se.jherrlin.server.user-commands :as user-commands]
            [se.jherrlin.server.models :as models]
            [se.jherrlin.clara-labs.bomberman-rules :as bomberman-rules]
            [se.jherrlin.server.game-state2 :as game-state2]
            [taoensso.timbre :as timbre]
            [se.jherrlin.datetime :as datetime]
            [se.jherrlin.clara-labs.board :as board]))

(comment
  (remove-ns 'se.jherrlin.server.application-service)
  )


(defn create-game! [game-state add-events-fn! create-event-data]
  (def game-state game-state)
  (def add-events-fn! add-events-fn!)
  (def create-event-data create-event-data)
  (if-not (s/valid? ::user-commands/create-game create-event-data)
    {:status :error
     :message
     (s/explain-data ::user-commands/create-game create-event-data)}
    (let [{:keys [game-name game-password]} create-event-data
          _                                 (def game-name game-name)
          _                                 (def game-password game-password)
          now                               (datetime/now)
          _                                 (def now now)
          game-id                           (java.util.UUID/randomUUID)
          _                                 (def game-id game-id)
          game-board                        board/board2
          _                                 (def game-board game-board)
          stones                            (board/generate-stones board/board2 board/reserved-floors)
          _                                 (def stones stones)
          items                             (board/generate-items stones)
          _                                 (def items items)
          player-wants-to-create-game       (models/->WantsToCreateGame game-id game-name game-password game-board stones items)
          _                                 (def player-wants-to-create-game player-wants-to-create-game)
          facts                             (concat
                                             (game-state2/active-game-facts @game-state)
                                             [(models/->TimestampNow now)]
                                             [player-wants-to-create-game])
          _                                 (def facts facts)
          {:keys [create-game-errors create-games] :as actions}
          (bomberman-rules/run-create-game-rules facts)
          _                                 (def actions actions)
          _                                 (def create-game-errors create-game-errors)
          _                                 (def create-games create-games)]
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
  (if-not (s/valid? ::user-commands/start-game start-game-event-data)
    {:status :error
     :message
     (s/explain-data ::user-commands/start-game start-game-event-data)}
    (let [{:keys [game-id]}          start-game-event-data
          player-wants-to-start-game (models/->PlayerWantsToStartGame game-id)
          game                       (game-state/game @game-state game-id)
          {:keys [start-game-errors start-games] :as actions}
          (bomberman-rules/run-start-game-rules
           (concat (game-state2/game-facts game)
                   [(game-state2/active-game-fact game)]
                   [(models/->TimestampNow (datetime/now))]
                   [player-wants-to-start-game]))]
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
          now                                               (datetime/now)
          player-id                                         (java.util.UUID/randomUUID)
          game                                              (game-state/game @game-state game-id)
          player-wants-to-join-game                         (models/->PlayerWantsToJoinGame player-id player-name game-id game-password)
          {:keys [join-game-errors join-games] :as actions} (bomberman-rules/run-join-game-rules
                                                             (concat
                                                              (game-state2/game-facts game)
                                                              [(models/->TimestampNow now)]
                                                              [(game-state2/active-game-fact game)]
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

(ns se.jherrlin.server.game-state2
  (:require [se.jherrlin.server.game-state :as game-state]
            [se.jherrlin.server.models :as models]
            [taoensso.timbre :as timbre]))


(defn facts [{:keys [game-create-timestamp game-id game-started-timestamp game-state] :as game}]
  (concat
   (when game-create-timestamp
     [(models/->GameCreatedTimestamp game-id game-create-timestamp)])
   (when game-started-timestamp
     [(models/->GameStartedTimestamp game-id game-started-timestamp)])
   [(models/->Board game-id (game-state/board game))
    (models/->GameState game-id game-state)]
   (->> (game-state/players game)
        (vals)
        (filter (comp not #{:dead} :player-status))
        (map (fn [{:keys [player-id position] :as player}]
               (models/->PlayerOnBoardPosition game-id player-id position))))
   (->> (game-state/players game)
        (vals)
        (filter (comp not #{:dead} :player-status))
        (map (fn [{:keys [player-id position fire-length] :as player}]
               (models/->PlayerOnBoardFireLength game-id player-id position fire-length))))
   (->> (game-state/stones game)
        (map (partial models/->Stone game-id)))
   (->> (game-state/bombs game)
        (map models/map->BombOnBoard))
   (->> (game-state/items game)
        (map (fn [{:keys [item-position-xy item-power]}]
               (models/->ItemOnBoard game-id item-position-xy item-power))))
   (->> (game-state/fires game)
        (map models/map->FireOnBoard))))

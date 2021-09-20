(ns se.jherrlin.server.game-state2
  (:require [se.jherrlin.server.game-state :as game-state]
            [se.jherrlin.server.models :as models]
            [taoensso.timbre :as timbre]))



(defn facts [{:keys [subject game-id game-state] :as game}]
  (concat
   [(models/->Board subject (game-state/board game))
    (models/->GameState game-id game-state)]
   (->> (game-state/players game)
        (vals)
        (filter (comp not #{:dead} :player-status))
        (map (fn [{:keys [player-id position] :as player}]
               (models/->PlayerOnBoardPosition subject player-id position))))
   (->> (game-state/players game)
        (vals)
        (filter (comp not #{:dead} :player-status))
        (map (fn [{:keys [player-id position fire-length] :as player}]
               (models/->PlayerOnBoardFireLength subject player-id position fire-length))))
   (->> (game-state/stones game)
        (map (partial models/->Stone subject)))
   (->> (game-state/bombs game)
        (map models/map->BombOnBoard))
   (->> (game-state/items game)
        (map (fn [{:keys [item-position-xy item-power]}]
               (models/->ItemOnBoard game-id item-position-xy item-power))))
   (->> (game-state/fires game)
        (map models/map->FireOnBoard))))

(ns se.jherrlin.server.game-state2
  (:require [se.jherrlin.server.game-state :as game-state]
            [se.jherrlin.server.models :as models]
            [taoensso.timbre :as timbre]))


(defn game-facts [{:keys [game-create-timestamp game-id game-started-timestamp game-state] :as game}]
  (concat
   (when game-create-timestamp
     [(models/->GameCreatedTimestamp game-create-timestamp game-id)])
   (when game-started-timestamp
     [(models/->GameStartedTimestamp game-started-timestamp game-id)])
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

(defn active-game-fact [{:keys [game-name game-id password game-state] :as game}]
  (models/->ActiveGame game-id game-name password game-state))

(defn running-games [games]
  (filter (comp not #{:ended} :game-state) games))

(defn active-game-facts [gs]
  (->> gs
       (game-state/games)
       (running-games)
       (map active-game-fact)))

(defn games-facts [game-state]
  (->> game-state
       (game-state/games)
       (running-games)
       (map game-facts)
       (apply concat)))

(defn started-games-facts [game-state]
  (->> game-state
       (game-state/games)
       (filter (comp #{:started} :game-state))
       (map game-facts)
       (apply concat)))

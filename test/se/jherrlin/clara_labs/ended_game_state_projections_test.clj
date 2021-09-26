(ns se.jherrlin.clara-labs.ended-game-state-projections-test
  (:require  [clojure.test :as t]
             [se.jherrlin.claraman.models :as models]
             [se.jherrlin.server.game-state :as game-state]
             [se.jherrlin.clara-labs.board :as board]))

(t/deftest game-state-projection-after-game-ends
  (t/testing "Game state projection after game ends."
    (t/is
     (=
      (->> (let [game-id     1
                 player-1-id 1
                 player-2-id 2]
             [(models/->CreateGame   #inst "2021-09-20T20:15:34.000-00:00" game-id "GameName" "pwd" board/mini '() '())
              (models/->JoinGame     #inst "2021-09-20T20:15:40.000-00:00" game-id player-1-id "Player 1" 1 [1 1])
              (models/->JoinGame     #inst "2021-09-20T20:15:42.000-00:00" game-id player-2-id "Player 2" 2 [1 1])
              (models/->StartGame    #inst "2021-09-20T20:15:47.000-00:00" game-id)
              (models/->GameWinner   #inst "2021-09-20T20:15:49.100-00:00" game-id "leo")
              (models/->GameShutdown #inst "2021-09-20T20:15:49.100-00:00" game-id)
              (models/->EndGame      #inst "2021-09-20T20:15:49.100-00:00" game-id)])
           (map #(.toCloudEvent %))
           (game-state/the-projection {}))
      {:games {}, :past-games '({:game-id 1, :game-name "GameName", :winner "leo"})}))))

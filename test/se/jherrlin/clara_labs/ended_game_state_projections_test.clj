(ns se.jherrlin.clara-labs.ended-game-state-projections-test
  (:require  [clojure.test :as t]
             [se.jherrlin.server.resources :as resources]
             [se.jherrlin.server.models :as models]
             [clojure.pprint :as pprint]
             [se.jherrlin.server.game-state :as game-state]
             [se.jherrlin.clara-labs.board :as board]))

(t/deftest game-state-projection-after-game-ends
  (t/testing "Game state projection after game ends."
    (t/is
     (=
      (->> (let [game-id 1
                 player-1-id 1
                 player-2-id 2
                 ;; player-3-id 3
                 ]
             [(models/->CreateGame             #inst "2021-09-20T20:15:34.000-00:00" game-id "GameName" "pwd" board/mini '() '())
              (models/->JoinGame               #inst "2021-09-20T20:15:40.000-00:00" game-id player-1-id "Player 1")
              (models/->JoinGame               #inst "2021-09-20T20:15:42.000-00:00" game-id player-2-id "Player 2")
              ;; (models/->JoinGame               #inst "2021-09-20T20:15:44.000-00:00" game-id player-3-id "Player 3")
              (models/->StartGame              #inst "2021-09-20T20:15:47.000-00:00" game-id)
              ;; (models/->PlayerWantsToMove      #inst "2021-09-20T20:15:48.000-00:00" game-id player-1-id [1 1] :west)
              ;; (models/->PlayerMove             #inst "2021-09-20T20:15:48.100-00:00" game-id player-1-id [2 1] :west)
              ;; (models/->PlayerWantsToPlaceBomb #inst "2021-09-20T20:15:49.000-00:00" game-id player-1-id [2 1] 3 3)
              ;; (models/->BombToAdd              #inst "2021-09-20T20:15:49.100-00:00" game-id player-1-id [2 1] 3 #inst "2021-09-20T20:15:49.100-00:00")
              (models/->EndGame                game-id #inst "2021-09-20T20:15:49.100-00:00")
              ])
           (map #(.toCloudEvent %))
           (game-state/the-projection {}))
      {:games
       {1
        {:game-id 1,
         :game-name "GameName",
         :game-state :ended,
         :game-started-timestamp #inst "2021-09-20T20:15:47.000-00:00",
         :end-timestamp #inst "2021-09-20T20:15:49.100-00:00"}},
       :active-games {},
       :old-games
       {1
        {:game-id 1,
         :fire '(),
         :game-started-timestamp #inst "2021-09-20T20:15:47.000-00:00",
         :end-timestamp #inst "2021-09-20T20:15:49.100-00:00",
         :stones '(),
         :password "pwd",
         :game-create-timestamp #inst "2021-09-20T20:15:34.000-00:00",
         :game-name "GameName",
         :game-state :ended,
         :flying-bombs '(),
         :game-password "pwd",
         :players
         {1
          {:game-id 1,
           :fire-length 2,
           :max-nr-of-bombs-for-user 3,
           :player-id 1,
           :user-facing-direction :south,
           :player-nr 1,
           :position [1 1],
           :timestamp #inst "2021-09-20T20:15:40.000-00:00",
           :player-name "Player 1"},
          2
          {:game-id 1,
           :fire-length 2,
           :max-nr-of-bombs-for-user 3,
           :player-id 2,
           :user-facing-direction :south,
           :player-nr 2,
           :position [17 9],
           :timestamp #inst "2021-09-20T20:15:42.000-00:00",
           :player-name "Player 2"}},
         :items '(),
         :timestamp #inst "2021-09-20T20:15:34.000-00:00",
         :bombs '(),
         :subject 1,
         :board
         [[{:type :wall, :x 0, :y 0}
           {:type :wall, :x 1, :y 0}
           {:type :wall, :x 2, :y 0}]
          [{:type :wall, :x 0, :y 1}
           {:type :floor, :x 1, :y 1}
           {:type :wall, :x 2, :y 1}]
          [{:type :wall, :x 0, :y 2}
           {:type :wall, :x 1, :y 2}
           {:type :wall, :x 2, :y 2}]]}}}))))

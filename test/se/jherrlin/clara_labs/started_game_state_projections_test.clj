(ns se.jherrlin.clara-labs.started-game-state-projections-test
  (:require  [clojure.test :as t]
             [se.jherrlin.claraman.server.resources :as resources]
             [se.jherrlin.claraman.models :as models]
             [clojure.pprint :as pprint]
             [se.jherrlin.claraman.game-state :as game-state]
             [se.jherrlin.claraman.board :as board]))

(comment
  (->> (let [game-id 1]
         [(models/->CreateGame #inst "2021-09-20T20:15:34.000-00:00" game-id "GameName" "pwd" board/mini '() '())
          (models/->JoinGame   #inst "2021-09-20T20:15:40.000-00:00" game-id 1 "Player 1" 1 [1 1])
          (models/->JoinGame   #inst "2021-09-20T20:15:42.000-00:00" game-id 2 "Player 2" 2 [1 1])
          (models/->JoinGame   #inst "2021-09-20T20:15:44.000-00:00" game-id 3 "Player 3" 3 [1 1])
          (models/->StartGame  #inst "2021-09-20T20:15:47.000-00:00" game-id)])
       (map #(.toCloudEvent %))
       (game-state/the-projection {})
       (pprint/pprint)
       (with-out-str)
       (spit "/home/john/git/clara-labs-frontend/resources/test-data/started-game-state.edn"))
  )


(t/deftest started-game-state-projection
  (t/testing "Make sure the game state projection stays the same"
    (t/is
     (=
      (resources/read-edn-file "test-data/started-game-state.edn")
      (->> (let [game-id 1]
             [(models/->CreateGame #inst "2021-09-20T20:15:34.000-00:00" game-id "GameName" "pwd" board/mini '() '())
              (models/->JoinGame   #inst "2021-09-20T20:15:40.000-00:00" game-id 1 "Player 1" 1 [1 1])
              (models/->JoinGame   #inst "2021-09-20T20:15:42.000-00:00" game-id 2 "Player 2" 2 [1 1])
              (models/->JoinGame   #inst "2021-09-20T20:15:44.000-00:00" game-id 3 "Player 3" 3 [1 1])
              (models/->StartGame  #inst "2021-09-20T20:15:47.000-00:00" game-id)])
           (map #(.toCloudEvent %))
           (game-state/the-projection {}))))))

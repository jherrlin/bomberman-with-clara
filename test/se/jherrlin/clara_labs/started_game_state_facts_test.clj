(ns se.jherrlin.clara-labs.started-game-state-facts-test
  (:require [clojure.test :as t]
            [se.jherrlin.server.resources :as resources]
            [se.jherrlin.claraman.models :as models]
            [se.jherrlin.claraman.game-state :as game-state]
            [se.jherrlin.clara-labs.board :as board]))


(comment
  (->> (let [game-id 1]
         [(models/->CreateGame #inst "2021-09-20T20:15:34.000-00:00" game-id "GameName" "pwd" board/mini '() '())
          (models/->JoinGame   #inst "2021-09-20T20:15:40.000-00:00" game-id 1 "Player 1" 1 [1 1])
          (models/->JoinGame   #inst "2021-09-20T20:15:42.000-00:00" game-id 2 "Player 2" 2 [1 1])
          (models/->JoinGame   #inst "2021-09-20T20:15:44.000-00:00" game-id 3 "Player 3" 3 [1 1])
          (models/->StartGame  #inst "2021-09-20T20:15:47.000-00:00" game-id)])
       (map #(.toCloudEvent %))
       (game-state/the-projection {})
       :games
       (vals)
       (first)
       (game-state/game-facts)
       (resources/output "test-data/started-game-facts.edn"))
  )



(t/deftest game-state-facts
  (t/testing "Make sure that events projections create the same facts."
    (t/is
     (=
      (->> (let [game-id 1]
             [(models/->CreateGame #inst "2021-09-20T20:15:34.000-00:00" game-id "GameName" "pwd" board/mini '() '())
              (models/->JoinGame   #inst "2021-09-20T20:15:40.000-00:00" game-id 1 "Player 1" 1 [1 1])
              (models/->JoinGame   #inst "2021-09-20T20:15:42.000-00:00" game-id 2 "Player 2" 2 [1 1])
              (models/->JoinGame   #inst "2021-09-20T20:15:44.000-00:00" game-id 3 "Player 3" 3 [1 1])
              (models/->StartGame  #inst "2021-09-20T20:15:47.000-00:00" game-id)])
           (map #(.toCloudEvent %))
           (game-state/the-projection {})
           :games
           (vals)
           (first)
           (game-state/game-facts)
           ;; (map (fn [f] [(type f) f]))
           (map #(into {} %))
           (set))
      (->> (resources/pull "test-data/started-game-facts.edn")
           (map #(into {} %))
           (set))))))

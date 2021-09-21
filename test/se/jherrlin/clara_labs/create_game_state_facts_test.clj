(ns se.jherrlin.clara-labs.create-game-state-facts-test
  (:require  [clojure.test :as t]
             [se.jherrlin.server.resources :as resources]
             [se.jherrlin.server.models :as models]
             [clojure.pprint :as pprint]
             [clojure.java.io :as io]
             [se.jherrlin.server.game-state2 :as game-state2]
             [se.jherrlin.server.game-state :as game-state]
             [se.jherrlin.clara-labs.board :as board]))




(comment
  (->> (let [game-id 1]
         [(models/->CreateGame #inst "2021-09-20T20:15:34.000-00:00" game-id "GameName" "pwd" board/mini '() '())])
       (map #(.toCloudEvent %))
       (game-state/the-projection {})
       :games
       (vals)
       (first)
       (game-state2/facts)
       (resources/output "/home/john/git/clara-labs-frontend/resources/test-data/create-game-facts.edn"))
  )

(t/deftest game-state-facts
  (t/testing "Make sure that events projections create the same facts."
    (t/is
     (=
      (->> (let [game-id 1]
             [(models/->CreateGame #inst "2021-09-20T20:15:34.000-00:00" game-id "GameName" "pwd" board/mini '() '())])
           (map #(.toCloudEvent %))
           (game-state/the-projection {})
           :games
           (vals)
           (first)
           (game-state2/facts)
           (map #(into {} %))
           (set))
      (->> (resources/pull "test-data/create-game-facts.edn")
           (map #(into {} %))
           (set))))))

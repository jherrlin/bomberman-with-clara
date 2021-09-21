(ns se.jherrlin.clara-labs.create-game-state-facts-test
  (:require  [clojure.test :as t]
             [se.jherrlin.server.resources :as resources]
             [se.jherrlin.server.models :as models]
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
       (game-state2/game-facts)
       (resources/output "test-data/create-game-facts.edn"))
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
           (game-state2/game-facts)
           (map #(into {} %))
           (set))
      (->> (resources/pull "test-data/create-game-facts.edn")
           (map #(into {} %))
           (set))))))

(comment
  (->> (game-state2/game-facts
        {:game-id 1,
         :fire (),
         :stones (),
         :password "pwd",
         :game-name "GameName",
         :game-state :created,
         :flying-bombs (),
         :game-password "pwd",
         :items (),
         :timestamp #inst "2021-09-20T20:15:34.000-00:00",
         :bombs (),
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
           {:type :wall, :x 2, :y 2}]]})
       (map (fn [f] [(type f) f])))
  )

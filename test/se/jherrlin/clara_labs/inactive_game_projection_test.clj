(ns se.jherrlin.clara-labs.inactive-game-projection-test
  (:require  [clojure.test :as t]
             [se.jherrlin.claraman.models :as models]
             [se.jherrlin.claraman.game-state :as game-state]
             [se.jherrlin.claraman.board :as board]))


(t/deftest game-state-projection-after-game-inactivity-timeout
  (t/is
   (=
    (->> (let [game-id 1]
           [(models/->CreateGame                   #inst "2021-09-20T20:15:34.000-00:00" game-id "GameName" "pwd" board/mini '() '())
            (models/->CreatedGameInactivityTimeout #inst "2021-09-20T21:00:34.000-00:00" game-id :inactivity)])
         (map #(.toCloudEvent %))
         (game-state/the-projection {}))
    {:games {}}))

  (t/is
   (=
    (->> (let [game-id 1]
           [(models/->CreateGame                   #inst "2021-09-20T20:15:34.000-00:00" game-id "GameName" "pwd" board/mini '() '())
            (models/->StartedGameInactivityTimeout #inst "2021-09-20T21:00:34.000-00:00" game-id :inactivity)])
         (map #(.toCloudEvent %))
         (game-state/the-projection {}))
    {:games {}})))

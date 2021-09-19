(ns se.jherrlin.clara-labs.bomberman-rules-test
  (:require
   [clara.rules :refer [defquery defrule defsession fire-rules insert insert! insert-all insert-unconditional! query retract!]]
   [clara.rules.accumulators :as acc]
   [se.jherrlin.clara-labs.bomberman-rules :as bomberman]
   [se.jherrlin.datetime :as datetime]
   [se.jherrlin.server.models :as models]
   [clara.tools.inspect :as inspect]
   [clojure.test :as t]))

(comment
  (remove-ns 'se.jherrlin.clara-labs.bomberman-rules-test)
  )

(def repl-game-id #uuid "c03e430f-2b24-4109-a923-08c986a682a8")

(def board
  [[{:type :wall :x 0 :y 0} {:type :wall  :x 1 :y 0} {:type :wall  :x 2 :y 0} {:type :wall  :x 3 :y 0} {:type :wall  :x 4 :y 0} {:type :wall  :x 5 :y 0}]
   [{:type :wall :x 0 :y 1} {:type :floor :x 1 :y 1} {:type :floor :x 2 :y 1} {:type :floor :x 3 :y 1} {:type :floor :x 4 :y 1} {:type :wall  :x 5 :y 1}]
   [{:type :wall :x 0 :y 2} {:type :floor :x 1 :y 2} {:type :wall  :x 2 :y 2} {:type :floor :x 3 :y 2} {:type :wall  :x 4 :y 2} {:type :floor :x 5 :y 2}]
   [{:type :wall :x 0 :y 3} {:type :floor :x 1 :y 3} {:type :floor :x 2 :y 3} {:type :floor :x 3 :y 3} {:type :floor :x 4 :y 3} {:type :wall  :x 5 :y 3}]
   [{:type :wall :x 0 :y 4} {:type :wall  :x 1 :y 4} {:type :wall  :x 2 :y 4} {:type :wall  :x 3 :y 4} {:type :wall  :x 4 :y 4} {:type :wall  :x 5 :y 4}]])

(defsession bomberman-session 'se.jherrlin.clara-labs.bomberman-rules)

(t/deftest picks-fire-inc-item-from-board
  (t/testing "When player walks on fire inc item, fire length incs."
    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->ItemOnBoard             1   [1 1] :inc-fire-length)
                                  (models/->PlayerOnBoardFireLength 1 2 [1 1] 2)])
            session' (fire-rules session)]
        {:picks-fire-inc-item-from-board
         (->> (query session' bomberman/picks-fire-inc-item-from-board?)
              (map (comp #(into {} %) :?picks-fire-inc-item-from-board))
              (set))})
      {:picks-fire-inc-item-from-board
       #{{:game-id 1, :player-id 2, :item-position-xy [1 1], :new-fire-length 3}}})))

  (t/testing "If xy positions doesnt match, dont pick item"
    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->PlayerOnBoardFireLength 1 2 [1 1] 2)
                                  (models/->ItemOnBoard             1   [1 2] :inc-fire-length)
                                  (models/->ItemOnBoard             1   [1 3] :inc-fire-length)
                                  (models/->ItemOnBoard             0   [1 3] :inc-fire-length)])
            session' (fire-rules session)]
        {:picks-fire-inc-item-from-board
         (->> (query session' bomberman/picks-fire-inc-item-from-board?)
              (map (comp #(into {} %) :?picks-fire-inc-item-from-board))
              (set))})
      {:picks-fire-inc-item-from-board #{}}))))

(t/deftest game-ends
  (t/testing "Only one player left in the game, it wins."
    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->TimestampNow                               #inst "2021-08-28T15:03:02.000-00:00")
                                  (models/->GameState repl-game-id :started)
                                  (models/->FireOnBoard           repl-game-id 1 [1 1] #inst "2021-08-28T15:03:02.000-00:00")
                                  (models/->PlayerOnBoardPosition repl-game-id 1 [1 1])
                                  (models/->FireOnBoard           repl-game-id 1 [2 1] #inst "2021-08-28T15:03:02.000-00:00")
                                  (models/->PlayerOnBoardPosition repl-game-id 2 [2 1])
                                  (models/->PlayerOnBoardPosition repl-game-id 3 [3 1])])
            session' (fire-rules session)]
        {:dead-players
         (->> (query session' bomberman/dead-players?)
              (map (comp #(into {} %) :?dead-players))
              (set))
         :end-games
         (->> (query session' bomberman/end-game?)
              (map (comp #(into {} %) :?end-game))
              (set))})
      {:dead-players
       #{{:timestamp #inst "2021-08-28T15:03:02.000-00:00"
          :game-id #uuid "c03e430f-2b24-4109-a923-08c986a682a8",
          :player-id 1,
          :killed-by-player-id 1}
         {:timestamp #inst "2021-08-28T15:03:02.000-00:00"
          :game-id #uuid "c03e430f-2b24-4109-a923-08c986a682a8",
          :player-id 2,
          :killed-by-player-id 1}},
       :end-games
       #{{:game-id #uuid "c03e430f-2b24-4109-a923-08c986a682a8", :timestamp #inst "2021-08-28T15:03:02.000-00:00"}}})))

  (t/testing "If no player is left, end the game."
    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->TimestampNow           #inst "2021-08-28T15:03:02.000-00:00")
                                  (models/->GameState repl-game-id :started)])
            session' (fire-rules session)]
        {:dead-players
         (->> (query session' bomberman/dead-players?)
              (map (comp #(into {} %) :?dead-players))
              (set))
         :end-games
         (->> (query session' bomberman/end-game?)
              (map (comp #(into {} %) :?end-game))
              (set))})
      {:dead-players #{},
       :end-games
       #{{:game-id #uuid "c03e430f-2b24-4109-a923-08c986a682a8",
          :timestamp #inst "2021-08-28T15:03:02.000-00:00"}}}))))

(t/deftest player-join-game
  (t/testing "Users can join game is state and password is correct"
    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->ActiveGame            2 "first-game" "pwd" :created)
                                  (models/->PlayerWantsToJoinGame 1 "John"  2 "pwd")
                                  (models/->PlayerWantsToJoinGame 3 "Kalle" 2 "pwd")])
            session' (fire-rules session)]

        {:errors (->> (query session' bomberman/join-game-error?)
                      (map (comp #(into {} %) :?join-game-error))
                      (set))
         :joins  (->> (query session' bomberman/join-game?)
                      (map (comp #(into {} %) :?join-game))
                      (set))})
      {:errors #{},
       :joins
       #{{:game-id 2, :player-id 3, :player-name "Kalle"}
         {:game-id 2, :player-id 1, :player-name "John"}}})))

  (t/testing "Player cant join game cause it's full."
    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->ActiveGame            1 "first-game" "pwd" :created)
                                  (models/->PlayerOnBoardPosition 1 1  [1 1])
                                  (models/->PlayerOnBoardPosition 1 2  [2 1])
                                  (models/->PlayerOnBoardPosition 1 3  [3 1])
                                  (models/->PlayerOnBoardPosition 1 4  [4 1])
                                  (models/->PlayerWantsToJoinGame 5 "Preben" 1 "pwd")])
            session' (fire-rules session)]

        {:errors (->> (query session' bomberman/join-game-error?)
                      (map (comp #(into {} %) :?join-game-error))
                      (set))
         :joins  (->> (query session' bomberman/join-game?)
                      (map (comp #(into {} %) :?join-game))
                      (set))})
      {:errors #{{:game-id 1, :message "Game is full!"}},
       :joins #{}})))

  (t/testing "User cant join game if password is wrong"
    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->ActiveGame            2 "first-game" "pwd" :created)
                                  (models/->PlayerWantsToJoinGame 1 "John"  2 "WRONG")
                                  (models/->PlayerWantsToJoinGame 3 "Kalle" 2 "pwd")])
            session' (fire-rules session)]

        {:errors (->> (query session' bomberman/join-game-error?)
                      (map (comp #(into {} %) :?join-game-error))
                      (set))
         :joins  (->> (query session' bomberman/join-game?)
                      (map (comp #(into {} %) :?join-game))
                      (set))})
      {:errors
       #{{:game-id 2,
          :message "Password to game is wrong!"}},
       :joins #{{:game-id 2, :player-id 3, :player-name "Kalle"}}}))
    )

  (t/testing "Users cant join game if state is wrong"
    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->ActiveGame            2 "first-game" "pwd" :started)
                                  (models/->PlayerWantsToJoinGame 1 "John"  2 "WRONG")
                                  (models/->PlayerWantsToJoinGame 3 "Kalle" 2 "pwd")])
            session' (fire-rules session)]

        {:errors (->> (query session' bomberman/join-game-error?)
                      (map (comp #(into {} %) :?join-game-error))
                      (set))
         :joins  (->> (query session' bomberman/join-game?)
                      (map (comp #(into {} %) :?join-game))
                      (set))})
      {:errors
       #{{:game-id 2,
          :message "Password is correct but game is noy lobby any more."}},
       :joins #{}}))))

(t/deftest create-new-games
  (t/testing "When no games exists, create one."
    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->WantsToCreateGame 1 "first-game" "game-password")])
            session' (fire-rules session)]
        {:errors (->> (query session' bomberman/create-game-error?)
                      (map (comp #(into {} %) :?create-game-error))
                      (set))
         :games  (->> (query session' bomberman/create-game?)
                      (map (comp #(into {} %) :?create-game))
                      (set))})
      {:errors #{},
       :games #{{:game-id 1, :game-name "first-game", :password "game-password"}}})))

  (t/testing "Two player cant start a game with the same name at the same time."
    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->WantsToCreateGame 1 "first-game" "game-password")
                                  (models/->WantsToCreateGame 2 "first-game" "my-second-game")])
            session' (fire-rules session)]

        {:errors (->> (query session' bomberman/create-game-error?)
                      (map (comp #(into {} %) :?create-game-error))
                      (set))
         :games  (->> (query session' bomberman/create-game?)
                      (map (comp #(into {} %) :?create-game))
                      (set))})
      {:errors
       #{{:game-id 2,
          :game-name "first-game",
          :message "Game with that name already exists!"}
         {:game-id 1,
          :game-name "first-game",
          :message "Game with that name already exists!"}},
       :games #{}})))

  (t/testing "A new game cant be started with the same name as an active game."
    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->WantsToCreateGame 1 "first-game" "game-password")
                                  (models/->ActiveGame        2 "first-game" "pwd" :created)])
            session' (fire-rules session)]

        {:errors (->> (query session' bomberman/create-game-error?)
                      (map (comp #(into {} %) :?create-game-error))
                      (set))
         :games  (->> (query session' bomberman/create-game?)
                      (map (comp #(into {} %) :?create-game))
                      (set))})
      {:errors
       #{{:game-id 1,
          :game-name "first-game",
          :message "Game with that name already exists!"}},
       :games #{}})))

  (t/testing "If there is no name collisions, games can be started."
    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->WantsToCreateGame 1 "first-game" "game-password")
                                  (models/->WantsToCreateGame 2 "second-game" "my-second-game")
                                  (models/->ActiveGame        3 "some-active-game" "pwd" :created)])
            session' (fire-rules session)]
        {:errors (->> (query session' bomberman/create-game-error?)
                      (map (comp #(into {} %) :?create-game-error))
                      (set))
         :games  (->> (query session' bomberman/create-game?)
                      (map (comp #(into {} %) :?create-game))
                      (set))})
      {:errors #{},
       :games
       #{{:game-id 2, :game-name "second-game", :password "my-second-game"}
         {:game-id 1, :game-name "first-game", :password "game-password"}}}))))

(t/deftest start-new-game
  (t/testing "A game can be started"
    (t/is
     (=
      (let [game-id  1
            session  (insert-all bomberman-session
                                 [(models/->TimestampNow           #inst "2021-08-28T15:03:02.000-00:00")
                                  (models/->PlayerWantsToStartGame game-id)
                                  (models/->GameState              game-id :created)
                                  (models/->PlayerOnBoardPosition  game-id 1 [1 1])
                                  (models/->PlayerOnBoardPosition  game-id 2 [1 2])])
            session' (fire-rules session)]
        {:errors      (->> (query session' bomberman/start-game-error?)
                           (map (comp #(into {} %) :?start-game-error))
                           (set))
         :start-games (->> (query session' bomberman/start-game?)
                           (map (comp #(into {} %) :?start-game))
                           (set))})
      {:errors      #{},
       :start-games #{{:game-id 1, :timestamp #inst "2021-08-28T15:03:02.000-00:00"}}})))

  (t/testing "To start a game a minimum of 2 players"
    (t/is
     (=
      (let [game-id  1
            session  (insert-all bomberman-session
                                 [(models/->TimestampNow           #inst "2021-08-28T15:03:02.000-00:00")
                                  (models/->PlayerWantsToStartGame game-id)
                                  (models/->GameState              game-id :created)
                                  (models/->PlayerOnBoardPosition  game-id 1 [1 1])])
            session' (fire-rules session)]
        {:errors      (->> (query session' bomberman/start-game-error?)
                           (map (comp #(into {} %) :?start-game-error))
                           (set))
         :start-games (->> (query session' bomberman/start-game?)
                           (map (comp #(into {} %) :?start-game))
                           (set))})
      {:errors      #{{:game-id 1, :message "Not enough players! Minimum is 2."}},
       :start-games #{}}))))

(t/deftest player-wants-to-move
  (t/is
   (=
    (let [timestamp #inst "2021-09-19T21:14:00.026-00:00"
          session   (insert-all bomberman-session
                                [(models/->TimestampNow      timestamp)
                                 (models/->Board                       repl-game-id board)
                                 (models/->GameState                   repl-game-id :started)
                                 (models/->PlayerWantsToMove timestamp repl-game-id 1 [1 1] :east)
                                 (models/->PlayerWantsToMove timestamp repl-game-id 2 [1 1] :south)
                                 (models/->PlayerWantsToMove timestamp repl-game-id 3 [1 1] :west)
                                 (models/->PlayerWantsToMove timestamp repl-game-id 4 [1 1] :north)
                                 (models/->PlayerWantsToMove timestamp repl-game-id 5 [2 1] :east)
                                 (models/->PlayerWantsToMove timestamp repl-game-id 6 [2 1] :south)
                                 (models/->PlayerWantsToMove timestamp repl-game-id 7 [2 1] :west)
                                 (models/->PlayerWantsToMove timestamp repl-game-id 8 [2 1] :north)])
          session'  (fire-rules session)]
      (->> (query session' bomberman/player-move?)
           (map (comp #(into {} %) :?player-move))
           (set)))
    #{{:timestamp #inst "2021-09-19T21:14:00.026-00:00"
       :game-id   repl-game-id :player-id 7, :next-position [1 1], :direction :west}
      {:timestamp #inst "2021-09-19T21:14:00.026-00:00"
       :game-id   repl-game-id :player-id 5, :next-position [3 1], :direction :east}
      {:timestamp #inst "2021-09-19T21:14:00.026-00:00"
       :game-id   repl-game-id :player-id 2, :next-position [1 2], :direction :south}
      {:timestamp #inst "2021-09-19T21:14:00.026-00:00"
       :game-id   repl-game-id :player-id 1, :next-position [2 1], :direction :east}}))

  (t/is
   (=
    (let [timestamp #inst "2021-09-19T21:14:00.026-00:00"
          session   (insert-all bomberman-session
                                [(models/->TimestampNow      timestamp)
                                 (models/->Board                       repl-game-id board)
                                 (models/->GameState                   repl-game-id :started)
                                 (models/->PlayerWantsToMove timestamp repl-game-id 1 [3 1] :south)
                                 (models/->PlayerWantsToMove timestamp repl-game-id 2 [3 3] :east)])
          session'  (fire-rules session)]
      (->> (query session' bomberman/player-move?)
           (map (comp #(into {} %) :?player-move))
           (set)))
    #{{:timestamp #inst "2021-09-19T21:14:00.026-00:00"
       :game-id   repl-game-id :player-id 1, :next-position [3 2], :direction :south}
      {:timestamp #inst "2021-09-19T21:14:00.026-00:00"
       :game-id   repl-game-id :player-id 2, :next-position [4 3], :direction :east}})))

(t/deftest player-cant-walk-on-bomb
  (t/testing "Player cant walk west as there is a bomb there"
    (t/is
     (=
      (let [timestamp #inst "2021-09-19T21:16:40.856-00:00"
            session   (insert-all bomberman-session
                                  [(models/->TimestampNow      timestamp)
                                   (models/->GameState                   repl-game-id :started)
                                   (models/->Board                       repl-game-id board)
                                   (models/->PlayerWantsToMove timestamp repl-game-id 1 [1 1] :east)
                                   (models/->PlayerWantsToMove timestamp repl-game-id 2 [2 1] :west)
                                   (models/->BombOnBoard                 repl-game-id 2 [2 1] 3 (datetime/now))])
            session'  (fire-rules session)]
        (->> (query session' bomberman/player-move?)
             (map (comp #(into {} %) :?player-move))
             (set)))
      #{{:timestamp #inst "2021-09-19T21:16:40.856-00:00"
         :game-id   repl-game-id :player-id 2, :next-position [1 1], :direction :west}}))))

(t/deftest player-wants-to-place-bomb-on-board
  (t/testing "should result in bomb on board."
    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->TimestampNow         #inst "2021-09-05T19:16:52.292-00:00")
                                  (models/->Board                repl-game-id board)
                                  (models/->GameState            repl-game-id :started)
                                  (models/->PlayerWantsToPlaceBomb #inst "2021-09-05T19:16:52.292-00:00" repl-game-id 1 [1 1] 3 3)])
            session' (fire-rules session)]
        {:bombs-on-board (->> (query session' bomberman/bomb-on-board?)
                              (map (comp #(into {} %) :?bomb-on-board))
                              (set))})
      {:bombs-on-board
       #{{:game-id repl-game-id
          :player-id 1,
          :bomb-position-xy [1 1],
          :fire-length 3,
          :bomb-added-timestamp #inst "2021-09-05T19:16:52.292-00:00"}}}))))

(t/deftest fire-expload-bombs
  (t/testing "When fire hits a bomb, it exploads."
    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->TimestampNow   #inst "2021-08-28T15:10:47.100-00:00")
                                  (models/->Board          repl-game-id board)
                                  (models/->BombOnBoard    repl-game-id  1 [1 1] 2 #inst "2021-08-28T15:03:47.100-00:00")
                                  (models/->BombOnBoard    repl-game-id  1 [3 1] 2 #inst "2021-08-28T15:10:47.100-00:00")
                                  (models/->BombOnBoard    repl-game-id  1 [1 3] 2 #inst "2021-08-28T15:10:47.100-00:00")
                                  (models/->BombOnBoard    repl-game-id  1 [3 3] 2 #inst "2021-08-28T15:10:47.100-00:00")])
            session' (fire-rules session)]
        {:fire-on-board    (->> (query session' bomberman/fire-on-board?)
                                (map (comp #(into {} %) :?fire-on-board))
                                (set))
         :exploading-bombs (->> (query session' bomberman/exploading-bombs?)
                                (map (comp #(into {} %) :?exploading-bombs))
                                (set))})
      {:fire-on-board
       #{{:game-id repl-game-id,
          :player-id 1,
          :fire-position-xy [3 2],
          :fire-start-timestamp #inst "2021-08-28T15:10:47.100-00:00"}
         {:game-id repl-game-id,
          :player-id 1,
          :fire-position-xy [2 3],
          :fire-start-timestamp #inst "2021-08-28T15:10:47.100-00:00"}
         {:game-id repl-game-id,
          :player-id 1,
          :fire-position-xy [1 2],
          :fire-start-timestamp #inst "2021-08-28T15:10:47.100-00:00"}
         {:game-id repl-game-id,
          :player-id 1,
          :fire-position-xy [2 1],
          :fire-start-timestamp #inst "2021-08-28T15:10:47.100-00:00"}
         {:game-id repl-game-id,
          :player-id 1,
          :fire-position-xy [4 1],
          :fire-start-timestamp #inst "2021-08-28T15:10:47.100-00:00"}
         {:game-id repl-game-id,
          :player-id 1,
          :fire-position-xy [3 3],
          :fire-start-timestamp #inst "2021-08-28T15:10:47.100-00:00"}
         {:game-id repl-game-id,
          :player-id 1,
          :fire-position-xy [1 3],
          :fire-start-timestamp #inst "2021-08-28T15:10:47.100-00:00"}
         {:game-id repl-game-id,
          :player-id 1,
          :fire-position-xy [4 3],
          :fire-start-timestamp #inst "2021-08-28T15:10:47.100-00:00"}
         {:game-id repl-game-id,
          :player-id 1,
          :fire-position-xy [1 1],
          :fire-start-timestamp #inst "2021-08-28T15:10:47.100-00:00"}
         {:game-id repl-game-id,
          :player-id 1,
          :fire-position-xy [3 1],
          :fire-start-timestamp #inst "2021-08-28T15:10:47.100-00:00"}},
       :exploading-bombs
       #{{:timestamp #inst "2021-08-28T15:10:47.100-00:00",
          :game-id repl-game-id,
          :player-id 1,
          :position-xy [3 1],
          :fire-length 2}
         {:timestamp #inst "2021-08-28T15:10:47.100-00:00",
          :game-id repl-game-id,
          :player-id 1,
          :position-xy [3 3],
          :fire-length 2}
         {:timestamp #inst "2021-08-28T15:10:47.100-00:00",
          :game-id repl-game-id,
          :player-id 1,
          :position-xy [1 3],
          :fire-length 2}
         {:timestamp #inst "2021-08-28T15:10:47.100-00:00",
          :game-id repl-game-id,
          :player-id 1,
          :position-xy [1 1],
          :fire-length 2}}}))))

(t/deftest exploading-bombs-create-fire-and-fire-destroys-stone
  (t/is
   (=
    (let [session  (insert-all bomberman-session
                               [(models/->TimestampNow               #inst "2021-08-28T15:03:50.100-00:00")
                                (models/->Board          repl-game-id board)
                                (models/->BombOnBoard    repl-game-id  1 [1 1] 10 #inst "2021-08-25T15:03:47.100-00:00")
                                (models/->Stone          repl-game-id    [2 1])
                                (models/->Stone          repl-game-id    [3 1])
                                (models/->Stone          repl-game-id    [1 2])
                                (models/->Stone          repl-game-id    [1 3])])
          session' (fire-rules session)]
      {:fire-on-board    (->> (query session' bomberman/fire-on-board?)
                              (map (comp #(into {} %) :?fire-on-board))
                              (set))
       :exploading-bombs (->> (query session' bomberman/exploading-bombs?)
                              (map (comp #(into {} %) :?exploading-bombs))
                              (set))
       :stones-to-remove (->> (query session' bomberman/stones-to-remove?)
                              (map (comp #(into {} %) :?stones-to-remove))
                              (set))})
    {:fire-on-board
     #{{:game-id repl-game-id,
        :player-id 1,
        :fire-position-xy [1 1],
        :fire-start-timestamp #inst "2021-08-28T15:03:50.100-00:00"}
       {:game-id repl-game-id,
        :player-id 1,
        :fire-position-xy [1 2],
        :fire-start-timestamp #inst "2021-08-28T15:03:50.100-00:00"}
       {:game-id repl-game-id,
        :player-id 1,
        :fire-position-xy [2 1],
        :fire-start-timestamp #inst "2021-08-28T15:03:50.100-00:00"}},
     :exploading-bombs
     #{{:timestamp #inst "2021-08-28T15:03:50.100-00:00",
        :game-id repl-game-id,
        :player-id 1,
        :position-xy [1 1],
        :fire-length 10}},
     :stones-to-remove
     #{{:game-id repl-game-id, :position-xy [1 2]}
       {:game-id repl-game-id, :position-xy [2 1]}}})))

(t/deftest fire-burns-out
  (t/testing "When fire burns out it's removed from the board."
    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->TimestampNow         #inst "2021-08-28T15:03:04.000-00:00")
                                  (models/->Board        repl-game-id board)
                                  (models/->FireOnBoard  repl-game-id 1 [1 1] #inst "2021-08-28T15:03:02.000-00:00")
                                  (models/->FireOnBoard  repl-game-id 1 [1 2] #inst "2021-08-28T15:03:02.300-00:00")
                                  (models/->FireOnBoard  repl-game-id 1 [2 1] #inst "2021-08-28T15:03:03.000-00:00")])
            session' (fire-rules session)]
        (->> (query session' bomberman/fire-on-board?)
             (map (comp #(into {} %) :?fire-on-board))
             (set)))
      #{{:game-id repl-game-id
         :player-id 1,
         :fire-position-xy [2 1],
         :fire-start-timestamp #inst "2021-08-28T15:03:03.000-00:00"}}))))

(t/deftest player-dies-if-hit-by-fire
  (t/is
   (=
    (let [session  (insert-all bomberman-session
                               [(models/->TimestampNow                #inst "2021-08-28T15:03:02.000-00:00")
                                (models/->Board                 repl-game-id board)
                                (models/->FireOnBoard           repl-game-id 2 [1 1] #inst "2021-08-28T15:03:02.000-00:00")
                                (models/->PlayerOnBoardPosition repl-game-id 1 [1 1])])
          session' (fire-rules session)]
      (->> (query session' bomberman/dead-players?)
           (map (comp #(into {} %) :?dead-players))
           (set)))
    #{{:timestamp #inst "2021-08-28T15:03:02.000-00:00"
       :game-id repl-game-id :player-id 1, :killed-by-player-id 2}})))

(t/deftest two-bombs-cant-be-place-in-the-same-xy
  (t/is
   (=
    (let [session  (insert-all bomberman-session
                               [(models/->PlayerWantsToPlaceBomb #inst "2021-08-28T15:04:47.100-00:00" repl-game-id  1 [1 1] 10  3)
                                (models/->BombOnBoard          repl-game-id  1 [1 1] 10 #inst "2021-08-28T15:03:47.100-00:00")])
          session' (fire-rules session)]
      {:bombs-on-board (->> (query session' bomberman/bomb-on-board?)
                            (map (comp #(into {} %) :?bomb-on-board))
                            (set))})
    {:bombs-on-board
     #{{:game-id repl-game-id
        :player-id 1,
        :bomb-position-xy [1 1],
        :fire-length 10,
        :bomb-added-timestamp #inst "2021-08-28T15:03:47.100-00:00"}}})))

(t/deftest player-cant-put-to-many-bombs
  (t/testing "Player can't put more bombs than it limit."
    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->TimestampNow                                   #inst "2021-08-28T15:03:02.000-00:00")
                                  (models/->PlayerWantsToPlaceBomb #inst "2021-08-28T15:03:47.100-00:00" repl-game-id 1 [1 1] 10  3)
                                  (models/->GameState              repl-game-id :started)
                                  (models/->BombOnBoard            repl-game-id 1 [2 1] 10 #inst "2021-08-28T15:03:47.100-00:00")
                                  (models/->BombOnBoard            repl-game-id 1 [3 1] 10 #inst "2021-08-28T15:03:47.100-00:00")])
            session' (fire-rules session)]
        {:bombs-on-board (->> (query session' bomberman/bomb-on-board?)
                              (map (comp #(into {} %) :?bomb-on-board))
                              (set))})
      {:bombs-on-board
       #{{:game-id              repl-game-id
          :player-id            1,
          :bomb-position-xy     [2 1],
          :fire-length          10,
          :bomb-added-timestamp #inst "2021-08-28T15:03:47.100-00:00"}
         {:game-id              repl-game-id
          :player-id            1,
          :bomb-position-xy     [1 1],
          :fire-length          10,
          :bomb-added-timestamp #inst "2021-08-28T15:03:47.100-00:00"}
         {:game-id              repl-game-id
          :player-id            1,
          :bomb-position-xy     [3 1],
          :fire-length          10,
          :bomb-added-timestamp #inst "2021-08-28T15:03:47.100-00:00"}}}))

    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->PlayerWantsToPlaceBomb #inst "2021-08-28T15:03:47.100-00:00" repl-game-id 1 [1 1] 10 3)
                                  (models/->BombOnBoard          repl-game-id 1 [2 1] 10 #inst "2021-08-28T15:03:47.100-00:00")
                                  (models/->BombOnBoard          repl-game-id 1 [3 1] 10 #inst "2021-08-28T15:03:47.100-00:00")
                                  (models/->BombOnBoard          repl-game-id 1 [4 1] 10 #inst "2021-08-28T15:03:47.100-00:00")])
            session' (fire-rules session)]
        {:bombs-on-board (->> (query session' bomberman/bomb-on-board?)
                              (map (comp #(into {} %) :?bomb-on-board))
                              (set))})
      {:bombs-on-board
       #{{:game-id              repl-game-id
          :player-id            1,
          :bomb-position-xy     [2 1],
          :fire-length          10,
          :bomb-added-timestamp #inst "2021-08-28T15:03:47.100-00:00"}
         {:game-id              repl-game-id
          :player-id            1,
          :bomb-position-xy     [4 1],
          :fire-length          10,
          :bomb-added-timestamp #inst "2021-08-28T15:03:47.100-00:00"}
         {:game-id              repl-game-id
          :player-id            1,
          :bomb-position-xy     [3 1],
          :fire-length          10,
          :bomb-added-timestamp #inst "2021-08-28T15:03:47.100-00:00"}}}))

    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->PlayerWantsToPlaceBomb #inst "2021-08-28T15:03:47.100-00:00" repl-game-id 1 [1 1] 10 1)
                                  (models/->BombOnBoard          repl-game-id 1 [2 1] 10 #inst "2021-08-28T15:03:47.100-00:00")])
            session' (fire-rules session)]
        {:bombs-on-board (->> (query session' bomberman/bomb-on-board?)
                              (map (comp #(into {} %) :?bomb-on-board))
                              (set))})
      {:bombs-on-board
       #{{:game-id              repl-game-id
          :player-id            1,
          :bomb-position-xy     [2 1],
          :fire-length          10,
          :bomb-added-timestamp #inst "2021-08-28T15:03:47.100-00:00"}}}))

    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(models/->TimestampNow                                   #inst "2021-08-28T15:03:02.000-00:00")
                                  (models/->GameState              repl-game-id :started)
                                  (models/->PlayerWantsToPlaceBomb #inst "2021-08-28T15:03:47.100-00:00" repl-game-id 1 [1 1] 10 1)])
            session' (fire-rules session)]
        {:bombs-on-board (->> (query session' bomberman/bomb-on-board?)
                              (map (comp #(into {} %) :?bomb-on-board))
                              (set))})
      {:bombs-on-board
       #{{:game-id              repl-game-id
          :player-id            1,
          :bomb-position-xy     [1 1],
          :fire-length          10,
          :bomb-added-timestamp #inst "2021-08-28T15:03:47.100-00:00"}}}))))

(t/deftest flying-bombs
  (t/is
   (=
    (let [session  (insert-all bomberman-session
                               [(models/->TimestampNow                      #inst "2021-08-28T15:03:02.000-00:00")
                                (models/->PlayerWantsToThrowBomb repl-game-id 1 [1 1] :east)
                                (models/->BombOnBoard            repl-game-id 1 [2 1] 3 #inst "2021-09-07T19:50:17.258-00:00")
                                (models/->BombOnBoard            repl-game-id 1 [1 2] 3 #inst "2021-09-07T19:50:17.258-00:00")])
          session' (fire-rules session)]
      {:bombs-on-board (->> (query session' bomberman/bomb-on-board?)
                            (map (comp #(into {} %) :?bomb-on-board))
                            (set))
       :flying-bombs   (->> (query session' bomberman/flying-bombs?)
                            (map (comp #(into {} %) :?flying-bombs))
                            (set))})
    {:bombs-on-board
     #{{:game-id              repl-game-id
        :player-id            1,
        :bomb-position-xy     [1 2],
        :fire-length          3,
        :bomb-added-timestamp #inst "2021-09-07T19:50:17.258-00:00"}},
     :flying-bombs
     #{{:timestamp              #inst "2021-08-28T15:03:02.000-00:00"
        :game-id                repl-game-id
        :player-id              1,
        :flying-bomb-current-xy [2 1],
        :fire-length            3,
        :bomb-added-timestamp   #inst "2021-09-07T19:50:17.258-00:00",
        :flying-bomb-direction  :east}}}))

  (t/is
   (=
    (let [timestamp #inst "2021-09-19T21:02:56.110-00:00"
          session   (insert-all bomberman-session
                                [(models/->TimestampNow                      #inst "2021-08-28T15:03:02.000-00:00")
                                 (models/->Board                repl-game-id board)
                                 (models/->FlyingBomb timestamp repl-game-id 1 [2 1] 3 #inst "2021-09-07T19:50:17.258-00:00" :east)])
          session'  (fire-rules session)]
      {:bombs-on-board (->> (query session' bomberman/bomb-on-board?)
                            (map (comp #(into {} %) :?bomb-on-board))
                            (set))
       :flying-bombs   (->> (query session' bomberman/flying-bombs?)
                            (map (comp #(into {} %) :?flying-bombs))
                            (set))})
    {:bombs-on-board
     #{{:game-id              repl-game-id
        :player-id            1,
        :bomb-position-xy     [3 1],
        :fire-length          3,
        :bomb-added-timestamp #inst "2021-09-07T19:50:17.258-00:00"}},
     :flying-bombs #{}}))

  (t/is
   (=
    (let [timestamp #inst "2021-09-19T21:02:56.110-00:00"
          session   (insert-all bomberman-session
                                [(models/->TimestampNow                      #inst "2021-08-28T15:03:02.000-00:00")
                                 (models/->Board                repl-game-id board)
                                 (models/->FlyingBomb timestamp repl-game-id 1 [2 1] 3 #inst "2021-09-07T19:50:17.258-00:00" :east)
                                 (models/->Stone                repl-game-id   [3 1])])
          session'  (fire-rules session)]
      {:bombs-on-board (->> (query session' bomberman/bomb-on-board?)
                            (map (comp #(into {} %) :?bomb-on-board))
                            (set))
       :flying-bombs   (->> (query session' bomberman/flying-bombs?)
                            (map (comp #(into {} %) :?flying-bombs))
                            (set))})
    {:bombs-on-board
     #{{:game-id              repl-game-id
        :player-id            1,
        :bomb-position-xy     [4 1],
        :fire-length          3,
        :bomb-added-timestamp #inst "2021-09-07T19:50:17.258-00:00"}},
     :flying-bombs #{}})))

(t/deftest flying-to-the-other-side
    (t/is
     (=
      (let [timestamp #inst "2021-09-19T21:02:56.110-00:00"
            session   (insert-all bomberman-session
                                  [(models/->TimestampNow                      #inst "2021-08-28T15:03:02.000-00:00")
                                   (models/->Board                repl-game-id board)
                                   (models/->FlyingBomb timestamp repl-game-id 1 [2 1] 3 #inst "2021-09-07T19:50:17.258-00:00" :west)
                                   (models/->Stone                repl-game-id   [1 1])])
            session'  (fire-rules session)]
        {:bombs-on-board (->> (query session' bomberman/bomb-on-board?)
                              (map (comp #(into {} %) :?bomb-on-board))
                              (set))
         :flying-bombs   (->> (query session' bomberman/flying-bombs?)
                              (map (comp #(into {} %) :?flying-bombs))
                              (set))})
      {:bombs-on-board
       #{{:game-id              repl-game-id
          :player-id            1,
          :bomb-position-xy     [4 1],
          :fire-length          3,
          :bomb-added-timestamp #inst "2021-09-07T19:50:17.258-00:00"}},
       :flying-bombs #{}})))

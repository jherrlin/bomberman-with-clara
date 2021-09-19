(ns se.jherrlin.clara-labs.bomberman-rules
  (:require [clara.rules :refer [defquery defrule defsession fire-rules insert insert! insert-all insert-unconditional! query retract!]]
            [clara.rules.accumulators :as acc]
            [clara.tools.inspect :as inspect]
            [se.jherrlin.clara-labs.board :as board]
            [se.jherrlin.clara-labs.fire-spread :as fire-spread]
            [se.jherrlin.datetime :as datetime]
            [clojure.set :as set]
            #?(:clj se.jherrlin.server.models)
            #?(:cljs [se.jherrlin.server.models
                      :refer [ActiveGame Board BombExploading BombOnBoard BombToAdd BombToRemove CreateGame CreateGameError
                              EndGame FireOnBoard FireToAdd FireToRemove
                              FlyingBomb GameState ItemOnBoard JoinGame JoinGameError
                              PlayerDies PlayerMove PlayerOnBoardFireLength PlayerOnBoardPosition PlayerPicksFireIncItemFromBoard
                              PlayerWantsToJoinGame PlayerWantsToMove GameWinner
                              PlayerWantsToPlaceBomb PlayerWantsToStartGame PlayerWantsToThrowBomb StartGame StartGameError
                              Stone StoneToRemove TimestampNow WantsToCreateGame]]))
  #?(:clj
     (:import [se.jherrlin.server.models
               ActiveGame Board BombExploading BombOnBoard BombToAdd BombToRemove CreateGame CreateGameError
               EndGame FireOnBoard FireToAdd FireToRemove GameWinner
               FlyingBomb GameState ItemOnBoard JoinGame JoinGameError
               PlayerDies PlayerMove PlayerOnBoardFireLength PlayerOnBoardPosition PlayerPicksFireIncItemFromBoard
               PlayerWantsToJoinGame PlayerWantsToMove
               PlayerWantsToPlaceBomb PlayerWantsToStartGame PlayerWantsToThrowBomb StartGame StartGameError
               Stone StoneToRemove TimestampNow WantsToCreateGame])))


(comment
  (remove-ns 'se.jherrlin.clara-labs.bomberman-rules)
  )


(defrule player-throws-bomb
  "A player can throw a bomb if facing direction points to it."
  [?player-wants-to-throw-bomb <- PlayerWantsToThrowBomb
   (= ?game-id game-id)
   (= ?player-id player-id)
   (= ?players-current-xy players-current-xy)
   (= ?player-facing-direction player-facing-direction)]
  [?bomb-on-board            <- BombOnBoard
   (= ?game-id game-id)
   (= ?bomb-position-xy bomb-position-xy)
   (= ?fire-length fire-length)
   (= ?bomb-added-timestamp bomb-added-timestamp)]
  [:test (= ?bomb-position-xy (board/next-xy-position ?players-current-xy ?player-facing-direction))]
  =>
  (retract! ?bomb-on-board)
  (insert-unconditional! (BombToRemove. ?game-id ?bomb-position-xy))
  (insert-unconditional! (FlyingBomb.
                          ?game-id
                          ?player-id
                          (board/next-xy-position ?players-current-xy ?player-facing-direction)
                          ?fire-length
                          ?bomb-added-timestamp
                          ?player-facing-direction)))

(defrule flying-bomb-lands-on-empty-floor
  ""
  [TimestampNow (= ?now now)]
  [Board
   (= ?game-id game-id)
   (= ?board board)]
  [?flying-bomb <- FlyingBomb
   (= ?game-id game-id)
   (= ?player-id player-id)
   (= ?flying-bomb-current-xy flying-bomb-current-xy)
   (= ?fire-length fire-length)
   (= ?bomb-added-timestamp bomb-added-timestamp)
   (= ?flying-bomb-direction flying-bomb-direction)]
  [:not [Stone       (= ?game-id game-id) (= stone-position-xy (board/next-xy-position ?flying-bomb-current-xy ?flying-bomb-direction))]]
  [:not [BombOnBoard (= ?game-id game-id) (= bomb-position-xy  (board/next-xy-position ?flying-bomb-current-xy ?flying-bomb-direction))]]
  [:test (#{:floor} (board/target-position-type ?board ?flying-bomb-current-xy ?flying-bomb-direction))]
  =>
  (retract! ?flying-bomb)
  (insert-unconditional! (BombToAdd.
                          ?now
                          ?game-id
                          ?player-id
                          (board/next-xy-position ?flying-bomb-current-xy ?flying-bomb-direction)
                          ?fire-length
                          ?bomb-added-timestamp))
  (insert-unconditional! (BombOnBoard.
                          ?game-id
                          ?player-id
                          (board/next-xy-position ?flying-bomb-current-xy ?flying-bomb-direction)
                          ?fire-length
                          ?bomb-added-timestamp)))

(defrule flying-bomb-keeps-on-flying
  ""
  [Board
   (= ?game-id game-id)
   (= ?board board)]
  [?flying-bomb <- FlyingBomb
   (= ?game-id game-id)
   (= ?player-id player-id)
   (= ?flying-bomb-current-xy flying-bomb-current-xy)
   (= ?fire-length fire-length)
   (= ?bomb-added-timestamp bomb-added-timestamp)
   (= ?flying-bomb-direction flying-bomb-direction)]
  [:or
   [Stone       (= ?game-id game-id) (= stone-position-xy (board/next-xy-position ?flying-bomb-current-xy ?flying-bomb-direction))]
   [BombOnBoard (= ?game-id game-id) (= bomb-position-xy  (board/next-xy-position ?flying-bomb-current-xy ?flying-bomb-direction))]
   [:test (not (#{:floor} (board/target-position-type ?board ?flying-bomb-current-xy ?flying-bomb-direction)))]]
  =>
  (retract! ?flying-bomb)
  (insert-unconditional! (FlyingBomb.
                          ?game-id
                          ?player-id
                          (board/next-xy-board-position ?board ?flying-bomb-current-xy ?flying-bomb-direction)
                          ?fire-length
                          ?bomb-added-timestamp
                          ?flying-bomb-direction)))

(defrule player-move
  "Player move"
  [Board (= ?game-id game-id) (= ?board board)]
  [GameState (= ?game-id game-id) (= :started game-state)]
  [?player-wants-to-move <- PlayerWantsToMove (= ?game-id game-id) (= ?player-id player-id) (= ?current-xy current-xy) (= ?direction direction)
   (#{:floor} (board/target-position-type ?board current-xy direction))]
  [:not [Stone       (= ?game-id game-id) (= stone-position-xy (board/next-xy-position ?current-xy ?direction))]]
  [:not [BombOnBoard (= ?game-id game-id) (= bomb-position-xy  (board/next-xy-position ?current-xy ?direction))]]
  =>
  (insert-unconditional! (PlayerMove. ?game-id ?player-id (board/next-xy-position ?current-xy ?direction) ?direction)))

(defrule place-bomb
  "Player place bomb in her current location."
  [TimestampNow (= ?now now)]
  [PlayerWantsToPlaceBomb
   (= ?game-id game-id)
   (= ?place-bomb-player-id player-id)
   (= ?fire-length fire-length)
   (= ?player-current-xy current-xy)
   (= ?timestamp timestamp)
   (= ?max-nr-of-bombs-for-player max-nr-of-bombs-for-player)]
  [GameState (= ?game-id game-id) (= :started game-state)]
  [:not [BombOnBoard
         (= ?game-id game-id)
         (= bomb-position-xy ?player-current-xy)]]
  [?bombs-placed-by-player <- (acc/count) from [BombOnBoard (= ?game-id game-id) (= player-id ?place-bomb-player-id)]]
  [:test (< ?bombs-placed-by-player ?max-nr-of-bombs-for-player)]
  =>
  (insert-unconditional! (BombToAdd.   ?now ?game-id ?place-bomb-player-id ?player-current-xy ?fire-length ?timestamp))
  (insert-unconditional! (BombOnBoard. ?game-id ?place-bomb-player-id ?player-current-xy ?fire-length ?timestamp)))

(defrule player-dies
  "Player dies if she gets hit by fire."
  [?dead-player <- PlayerOnBoardPosition (= ?game-id game-id) (= ?player-id player-id)      (= ?player-current-xy player-current-xy)]
  [FireOnBoard                           (= ?game-id game-id) (= ?fire-player-id player-id) (= ?fire-current-xy fire-position-xy)]
  [:test (= ?fire-current-xy ?player-current-xy)]
  =>
  (retract! ?dead-player)
  (insert-unconditional! (PlayerDies. ?game-id ?player-id ?fire-player-id)))

(defrule game-ends-if-there-is-only-one-player-left
  [TimestampNow (= ?now now)]
  [GameState (= ?game-id game-id) (= :started game-state)]
  [?players-alive <- (acc/all) :from [PlayerOnBoardPosition (= ?game-id game-id)]]
  [:test (= (count ?players-alive) 1)]
  =>
  (let [end-game-id     (-> ?players-alive first :game-id)
        alive-player-id (-> ?players-alive first :player-id)]
    (insert-unconditional! (GameWinner. ?game-id alive-player-id))
    (insert-unconditional! (EndGame. end-game-id ?now))))

(defrule game-ends-if-there-is-no-player-left
  [TimestampNow (= ?now now)]
  [GameState (= ?game-id game-id) (= :started game-state)]
  [?player-alive <- (acc/all) :from [PlayerOnBoardPosition (= ?game-id game-id)]]
  [:test (empty? ?player-alive)]
  =>
  (insert-unconditional! (GameWinner. ?game-id nil))
  (insert-unconditional! (EndGame. ?game-id ?now)))

(defrule exploading-bomb-throws-fire-flames
  "When a bomb exploads, fire is created in all four directions.
When fire hits a wall it stops.
When fire huts a stone it saves the fire to that stone but discard the rest in that latitude."
  [Board (= ?game-id game-id) (= ?board board)]
  [TimestampNow (= ?now now)]
  [BombExploading (= ?game-id game-id) (= ?player-id player-id) (= ?bomb-position-xy position-xy) (= ?fire-length fire-length)]
  [?stones <- (acc/all) :from [Stone (= ?game-id game-id)]]
  =>
  (let [fire-on-board (->> (fire-spread/fire-after-it-hit-objects ?bomb-position-xy ?fire-length ?board ?stones)
                           (mapv (fn [[x y]]
                                   [(FireOnBoard. ?game-id ?player-id [x y] ?now)
                                    (FireToAdd.   ?game-id ?player-id [x y] ?now)]))
                           (apply concat))]
    (apply insert! fire-on-board)))

(defrule bomb-exploding-after-timeout
  "Bomb that been on board for a time threshold should expload."
  [TimestampNow (= ?now now)]
  [?bomb <- BombOnBoard (= ?game-id game-id) (= ?player-id player-id) (= ?bomb-added-timestamp bomb-added-timestamp) (= ?bomb-position-xy bomb-position-xy) (= ?fire-length fire-length)]
  [:test (< 3000 (datetime/milliseconds-between ?bomb-added-timestamp ?now))]
  =>
  (retract! ?bomb)
  (insert-unconditional! (BombToRemove. ?game-id ?bomb-position-xy))
  (insert-unconditional! (BombExploading. ?now ?game-id ?player-id ?bomb-position-xy ?fire-length)))

(defrule bomb-exploading-when-hit-by-fire
  "Bomb is exploading if it's hit by fire."
  [TimestampNow (= ?now now)]
  [FireOnBoard (= ?game-id game-id) (= ?player-id player-id) (= ?current-fire-xy fire-position-xy)]
  [?bomb <- BombOnBoard (= ?game-id game-id) (= ?player-id player-id) (= ?bomb-position-xy bomb-position-xy) (= ?fire-length fire-length)]
  [:test (= ?bomb-position-xy ?current-fire-xy)]
  =>
  (retract! ?bomb)
  (insert-unconditional! (BombToRemove. ?game-id ?bomb-position-xy))
  (insert-unconditional! (BombExploading. ?now ?game-id ?player-id ?bomb-position-xy ?fire-length)))

(defrule remove-stones-hit-by-fire
  "If a stone is hit by fire, remove it."
  [FireOnBoard (= ?game-id game-id) (= ?fire-position-xy fire-position-xy)]
  [?stone <- Stone       (= ?game-id game-id) (= ?stone-position-xy stone-position-xy)]
  [:test (= ?fire-position-xy ?stone-position-xy)]
  =>
  (insert-unconditional! (StoneToRemove. ?game-id ?stone-position-xy)))

(defrule fire-burns-out
  "Fire on board burns out after some time."
  [TimestampNow (= ?now now)]
  [?fire <- FireOnBoard (= ?game-id game-id) (= ?fire-position-xy fire-position-xy) (= ?fire-start-timestamp fire-start-timestamp)]
  [:test (< 1500 (datetime/milliseconds-between ?fire-start-timestamp ?now))]
  =>
  (retract! ?fire)
  (insert-unconditional! (FireToRemove. ?game-id ?fire-position-xy)))

(defrule warn-player-about-game-name-collision
  [?wants-to-create-game <- WantsToCreateGame (= ?game-name game-name) (= ?game-id game-id)]
  [                         ActiveGame        (= ?game-name game-name)]
  =>
  (retract! ?wants-to-create-game)
  (insert-unconditional! (CreateGameError. ?game-id ?game-name "Game with that name already exists!")))

(defrule warn-player-about-game-name-collision-in-the-same-time
  [?game-1 <- WantsToCreateGame (= ?game-name-1 game-name) (= ?game-id-1 game-id)]
  [?game-2 <- WantsToCreateGame (= ?game-name-2 game-name) (= ?game-id-2 game-id)]
  [:test (= ?game-name-1 ?game-name-2) (not= ?game-id-1 ?game-id-2)]
  =>
  (retract! ?game-1)
  (retract! ?game-2)
  (insert-unconditional! (CreateGameError. ?game-id-1 ?game-name-1 "Game with that name already exists!"))
  (insert-unconditional! (CreateGameError. ?game-id-2 ?game-name-2 "Game with that name already exists!")))

(defrule create-game
  [?wants-to-create-game <- WantsToCreateGame
   (= ?game-name game-name)
   (= ?game-id game-id)
   (= ?password password)]
  =>
  (insert! (CreateGame. ?game-id ?game-name ?password)))

(defrule player-wants-to-start-game
  [TimestampNow (= ?now now)]
  [?player-wants-to-start-game <- PlayerWantsToStartGame (= ?game-id game-id)]
  [GameState                                             (= ?game-id game-id) (= :created game-state)]
  [?players-alive <- (acc/all) :from [PlayerOnBoardPosition (= ?game-id game-id)]]
  [:test (<= 2 (count ?players-alive))]
  =>
  (retract! ?player-wants-to-start-game)
  (insert-unconditional! (StartGame. ?game-id ?now)))

(defrule player-wants-to-start-game-but-not-enough-player-have-joined
  [?player-wants-to-start-game <- PlayerWantsToStartGame (= ?game-id game-id)]
  [GameState                                             (= ?game-id game-id) (= :created game-state)]
  [?players-alive <- (acc/all) :from [PlayerOnBoardPosition (= ?game-id game-id)]]
  [:test (< (count ?players-alive) 2)]
  =>
  (retract! ?player-wants-to-start-game)
  (insert-unconditional! (StartGameError. ?game-id "Not enough players! Minimum is 2.")))

(defrule player-wants-to-join-game
  [?player-wants-to-join-game <- PlayerWantsToJoinGame (= ?game-id game-id) (= ?player-password password) (= ?player-name player-name) (= ?player-id player-id)]
  [ActiveGame                  (= :created game-state) (= ?game-id game-id) (= ?game-password password)]
  [:test (= ?player-password ?game-password)]
  =>
  (insert! (JoinGame. ?game-id ?player-id ?player-name)))

(defrule player-wants-to-join-game-but-password-is-wrong
  [?player-wants-to-join-game <- PlayerWantsToJoinGame     (= ?game-id game-id) (= ?player-password password)]
  [ActiveGame                      (= :created game-state) (= ?game-id game-id) (= ?game-password password)]
  [:test (not= ?player-password ?game-password)]
  =>
  (retract! ?player-wants-to-join-game)
  (insert-unconditional! (JoinGameError. ?game-id "Password to game is wrong!")))

(defrule player-wants-to-join-game-but-game-is-full
  [?player-wants-to-join-game <- PlayerWantsToJoinGame (= ?game-id game-id) (= ?player-password password)]
  [ActiveGame                                          (= ?game-id game-id)]
  [?players-joined <- (acc/all) :from [PlayerOnBoardPosition (= ?game-id game-id)]]
  [:test (= (count ?players-joined) 4)]
  =>
  (retract! ?player-wants-to-join-game)
  (insert-unconditional! (JoinGameError. ?game-id "Game is full!")))

(defrule player-wants-to-join-game-but-game-state-is-wrong
  [?player-wants-to-join-game <- PlayerWantsToJoinGame  (= ?game-id game-id) (= ?player-password password)]
  [ActiveGame  (not= :created game-state)               (= ?game-id game-id) (= ?game-password password)]
  [:test (not= ?player-password ?game-password)]
  =>
  (retract! ?player-wants-to-join-game)
  (insert-unconditional! (JoinGameError. ?game-id "Password is correct but game is noy lobby any more.")))

(defrule player-picks-up-fire-length-inc-item-from-board
  [ItemOnBoard             (= ?game-id game-id) (= ?item-position-xy item-position-xy) (= item-power :inc-fire-length)]
  [PlayerOnBoardFireLength (= ?game-id game-id) (= ?player-id player-id) (= ?player-position-xy player-position-xy) (= ?fire-length fire-length)]
  [:test (= ?item-position-xy ?player-position-xy)]
  =>
  (insert! (PlayerPicksFireIncItemFromBoard. ?game-id ?player-id ?item-position-xy (inc ?fire-length))))

;; For the frontend
(defrule start-game-but-not-enough-player-have-joined
  "Used by frontend to determine if a game can be started."
  [GameState                                             (= ?game-id game-id) (= :created game-state)]
  [?players-alive <- (acc/all) :from [PlayerOnBoardPosition (= ?game-id game-id)]]
  [:test (< (count ?players-alive) 2)]
  =>
  (insert-unconditional! (StartGameError. ?game-id "Not enough players! Minimum is 2.")))

;; (defrule game-can-be-started
;;   "Used by frontend to determine if a game can be started."
;;   [GameState                                                (= ?game-id game-id) (= :created game-state)]
;;   [?players-alive <- (acc/all) :from [PlayerOnBoardPosition (= ?game-id game-id)]]
;;   [:test (<= 2 (count ?players-alive))]
;;   =>
;;   (insert-unconditional! (StartGame. ?game-id #inst "2021-09-19T15:54:31.631-00:00" ;; Time is not used here.
;;                                      )))

;; TODO
;; (defrule player-wants-to-join-game-but-player-name-is-already-three
;;   =>
;;   )

;; Queries
(defquery picks-fire-inc-item-from-board?
  []
  [?picks-fire-inc-item-from-board <- PlayerPicksFireIncItemFromBoard])

(defquery join-game-error?
  []
  [?join-game-error <- JoinGameError])

(defquery join-game?
  []
  [?join-game <- JoinGame])

(defquery start-game-error?
  []
  [?start-game-error <- StartGameError])

(defquery start-game?
  []
  [?start-game <- StartGame])


(defquery create-game-error?
  []
  [?create-game-error <- CreateGameError])

(defquery create-game?
  []
  [?create-game <- CreateGame])

(defquery player-move?
  []
  [?player-move <- PlayerMove])

(defquery stones-to-remove?
  []
  [?stones-to-remove <- StoneToRemove])

(defquery exploading-bombs?
  []
  [?exploading-bombs <- BombExploading])

(defquery fire-on-board?
  []
  [?fire-on-board <- FireOnBoard])

(defquery fire-to-remove?
  []
  [?fire-to-remove <- FireToRemove])

(defquery bomb-to-remove?
  []
  [?bomb-to-remove <- BombToRemove])

(defquery dead-players?
  []
  [?dead-players <- PlayerDies])

(defquery bomb-on-board?
  []
  [?bomb-on-board <- BombOnBoard])

(defquery flying-bombs?
  []
  [?flying-bombs <- FlyingBomb])

(defquery player-wants-to-move?
  []
  [?player-wants-to-move <- PlayerWantsToMove])

(defquery player-wants-to-place-bomb?
  []
  [?player-wants-to-place-bomb <- PlayerWantsToPlaceBomb])

(defquery player-wants-to-throw-bomb?
  []
  [?player-wants-to-throw-bomb <- PlayerWantsToThrowBomb])

(defquery fire-to-add?
  []
  [?fire-to-add <- FireToAdd])

(defquery end-game?
  []
  [?end-game <- EndGame])

(defquery bomb-to-add?
  []
  [?bomb-to-add <- BombToAdd])

(defquery game-winner?
  []
  [?game-winner <- GameWinner])


(defsession bomberman-session 'se.jherrlin.clara-labs.bomberman-rules)



(defn run-rules [facts]
  (let [session  (insert-all bomberman-session facts)
        session' (fire-rules session)]
    {:actions
     {:player-moves         (map :?player-move           (query session' player-move?))
      :exploading-bombs     (map :?exploading-bombs      (query session' exploading-bombs?))
      ;; :bombs-on-board       (map :?bomb-on-board         (query session' bomb-on-board?))      ;; Should not be turned into events, they are facts
      ;; :fire-on-board        (map :?fire-on-board         (query session' fire-on-board?))      ;; Should not be turned into events, they are facts
      :stones-to-remove     (map :?stones-to-remove      (query session' stones-to-remove?))
      :dead-players         (map :?dead-players          (query session' dead-players?))
      :flying-bombs         (map :?flying-bombs          (query session' flying-bombs?))
      :fire-to-remove       (map :?fire-to-remove        (query session' fire-to-remove?))
      :bomb-to-remove       (map :?bomb-to-remove        (query session' bomb-to-remove?))
      :game-winners         (map :?game-winner           (query session' game-winner?))

      :player-wants-to-move        (map :?player-wants-to-move        (query session' player-wants-to-move?))
      :player-wants-to-place-bomb  (map :?player-wants-to-place-bomb  (query session' player-wants-to-place-bomb?))
      :player-wants-to-throw-bomb  (map :?player-wants-to-throw-bomb  (query session' player-wants-to-throw-bomb?))

      :fire-to-add                 (map :?fire-to-add                 (query session' fire-to-add?))
      :bomb-to-add                 (map :?bomb-to-add                 (query session' bomb-to-add?))

      :end-games                   (map :?end-game             (query session' end-game?))

      :picks-fire-inc-item-from-board (map :?picks-fire-inc-item-from-board (query session' picks-fire-inc-item-from-board?))}}))

(defn run-create-game-rules [facts]
  (let [session  (insert-all bomberman-session facts)
        session' (fire-rules session)]
    {:create-game-errors (map :?create-game-error (query session' create-game-error?))
     :create-games       (map :?create-game       (query session' create-game?))}))

(defn run-join-game-rules [facts]
  (let [session  (insert-all bomberman-session facts)
        session' (fire-rules session)]
    {:join-game-errors (map :?join-game-error (query session' join-game-error?))
     :join-games       (map :?join-game       (query session' join-game?))}))

(defn run-start-game-rules [facts]
  (let [session  (insert-all bomberman-session facts)
        session' (fire-rules session)]
    {:start-game-errors (map :?start-game-error (query session' start-game-error?))
     :start-games       (map :?start-game       (query session' start-game?))}))

(comment
  (def repl-game-id #uuid "c03e430f-2b24-4109-a923-08c986a682a8")
  (def player-1-ws-id #uuid "e677bf82-0137-4105-940d-6d74429d31b0")
  (def player-2-ws-id #uuid "663bd7a5-7220-40e5-b08d-597c43b89e0a")

  (run-start-game-rules
   [(PlayerWantsToStartGame. repl-game-id)
    (GameState.              repl-game-id :created)
    (PlayerOnBoardPosition.  repl-game-id player-1-ws-id [1 1])
    (PlayerOnBoardPosition.  repl-game-id player-2-ws-id [1 1])])

  (run-start-game-rules
   [(PlayerWantsToStartGame. repl-game-id)
    (GameState.              repl-game-id :created)
    (PlayerOnBoardPosition.  repl-game-id player-1-ws-id [1 1])])

  (run-create-game-rules
   [(WantsToCreateGame. 1 "first-game" "game-password")])

  (run-create-game-rules
   [(WantsToCreateGame. 1 "first-game" "game-password")
    (WantsToCreateGame. 2 "first-game" "my-second-game")])

  (run-create-game-rules
   [(WantsToCreateGame. 1 "first-game" "game-password")
    (ActiveGame.        2 "first-game" "pwd" :created)])


  (run-rules
   [(CreateGame.             repl-game-id "First game" "my-secret")
    (JoinGame.               repl-game-id player-1-ws-id "John")
    (JoinGame.               repl-game-id player-2-ws-id "Hannah")
    (StartGame.              repl-game-id #inst "2021-09-19T15:54:31.631-00:00")
    (PlayerMove.             repl-game-id player-1-ws-id [2 1] :east)
    (PlayerWantsToPlaceBomb. repl-game-id player-1-ws-id [2 1] 3 (java.util.Date.) 3)])

  (run-rules
   [(->Board      repl-game-id board/board2)
    (FlyingBomb. repl-game-id 1 [2 1] 3 #inst "2021-09-07T19:50:17.258-00:00" :east)
    (->Stone      repl-game-id [3 1])])
  (run-rules
   [(->Board        repl-game-id (board/init 6))
    (->TimestampNow                           #inst "2021-08-28T15:04:00.100-00:00")
    (BombOnBoard.  repl-game-id   1 [1 1] 10 #inst "2021-08-28T15:03:47.100-00:00")
    (BombOnBoard.  repl-game-id   1 [1 3] 10 #inst "2021-08-28T15:03:49.100-00:00")
    (->Stone        repl-game-id     [3 1])
    (->Stone        repl-game-id     [3 3])
    (->Stone        666              [5 5])
    (->Stone        666              [6 6])])
  (let [session (insert-all bomberman-session
                            [(->TimestampNow              #inst "2021-08-28T15:03:50.100-00:00")
                             (->Board         repl-game-id (board/init 6))
                             (BombOnBoard.   repl-game-id  1 [1 1] 10 #inst "2021-08-28T15:03:47.100-00:00")
                             (BombOnBoard.   repl-game-id  1 [1 3] 10 #inst "2021-08-28T15:03:49.100-00:00")
                             (->Stone         repl-game-id    [3 1])
                             (->Stone         repl-game-id    [3 3])])
        session' (fire-rules session)]
    {:actions
     {:player-moves         (map :?player-move (query session' player-move?))
      :exploading-bombs   (map :?exploading-bombs (query session' exploading-bombs?))
      :dead-players         (map :?dead-players (query session' dead-players?))}}
    (query session)
    )
  )

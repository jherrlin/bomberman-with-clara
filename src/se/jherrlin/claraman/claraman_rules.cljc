(ns se.jherrlin.claraman.claraman-rules
  (:require [clara.rules :refer [defquery defrule defsession fire-rules insert insert! insert-all insert-unconditional! query retract!]]
            [clara.rules.accumulators :as acc]
            [clara.tools.inspect :as inspect]
            [se.jherrlin.claraman.board :as board]
            [se.jherrlin.claraman.fire-spread :as fire-spread]
            [se.jherrlin.datetime :as datetime]
            [clojure.set :as set]
            #?(:clj se.jherrlin.claraman.models)
            #?(:cljs [se.jherrlin.claraman.models
                      :refer [ActiveGame Board BombExploading BombOnBoard BombToAdd BombToRemove CreateGame CreateGameError
                              EndGame FireOnBoard FireToAdd FireToRemove
                              FlyingBomb GameState ItemOnBoard JoinGame JoinGameError
                              PlayerDies PlayerMove PlayerOnBoardFireLength PlayerOnBoardPosition PlayerPicksFireIncItemFromBoard
                              PlayerWantsToJoinGame PlayerWantsToMove GameWinner
                              PlayerWantsToPlaceBomb PlayerWantsToStartGame PlayerWantsToThrowBomb StartGame StartGameError
                              Stone StoneToRemove TimestampNow WantsToCreateGame
                              CreatedGameInactivityTimeout StartedGameInactivityTimeout GameStartedTimestamp GameCreatedTimestamp
                              GameIsInShutdown GameShutdown]]))
  #?(:clj
     (:import [se.jherrlin.claraman.models
               ActiveGame Board BombExploading BombOnBoard BombToAdd BombToRemove CreateGame CreateGameError
               EndGame FireOnBoard FireToAdd FireToRemove GameWinner
               FlyingBomb GameState ItemOnBoard JoinGame JoinGameError
               PlayerDies PlayerMove PlayerOnBoardFireLength PlayerOnBoardPosition PlayerPicksFireIncItemFromBoard
               PlayerWantsToJoinGame PlayerWantsToMove
               PlayerWantsToPlaceBomb PlayerWantsToStartGame PlayerWantsToThrowBomb StartGame StartGameError
               Stone StoneToRemove TimestampNow WantsToCreateGame
               CreatedGameInactivityTimeout StartedGameInactivityTimeout GameStartedTimestamp GameCreatedTimestamp
               GameIsInShutdown GameShutdown])))


(comment
  (remove-ns 'se.jherrlin.claraman.claraman-rules)
  )


(defrule player-throws-bomb
  "A player can throw a bomb if facing direction points to it."
  [TimestampNow (= ?now now)]
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
  (insert-unconditional! (BombToRemove. ?now ?game-id ?bomb-position-xy))
  (insert-unconditional! (FlyingBomb.
                          ?now
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
  [:or
   [Stone       (= ?game-id game-id) (= stone-position-xy (board/next-xy-position ?flying-bomb-current-xy ?flying-bomb-direction))]
   [BombOnBoard (= ?game-id game-id) (= bomb-position-xy  (board/next-xy-position ?flying-bomb-current-xy ?flying-bomb-direction))]
   [:test (not (#{:floor} (board/target-position-type ?board ?flying-bomb-current-xy ?flying-bomb-direction)))]]
  =>
  (retract! ?flying-bomb)
  (insert-unconditional! (FlyingBomb.
                          ?now
                          ?game-id
                          ?player-id
                          (board/next-xy-board-position ?board ?flying-bomb-current-xy ?flying-bomb-direction)
                          ?fire-length
                          ?bomb-added-timestamp
                          ?flying-bomb-direction)))

(defrule player-move
  "Player move"
  [TimestampNow (= ?now now)]
  [Board (= ?game-id game-id) (= ?board board)]
  [GameState (= ?game-id game-id) (= :started game-state)]
  [?player-wants-to-move <- PlayerWantsToMove (= ?game-id game-id) (= ?player-id player-id) (= ?current-xy current-xy) (= ?direction direction)
   (#{:floor} (board/target-position-type ?board current-xy direction))]
  [:not [Stone       (= ?game-id game-id) (= stone-position-xy (board/next-xy-position ?current-xy ?direction))]]
  [:not [BombOnBoard (= ?game-id game-id) (= bomb-position-xy  (board/next-xy-position ?current-xy ?direction))]]
  =>
  (insert-unconditional! (PlayerMove. ?now ?game-id ?player-id (board/next-xy-position ?current-xy ?direction) ?direction)))

(defrule place-bomb
  "Player place bomb in her current location."
  [TimestampNow (= ?now now)]
  [?player-wants-to-place-bomb <- PlayerWantsToPlaceBomb
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
  (retract! ?player-wants-to-place-bomb)
  (insert-unconditional! (BombToAdd.   ?now ?game-id ?place-bomb-player-id ?player-current-xy ?fire-length ?timestamp))
  (insert-unconditional! (BombOnBoard. ?game-id ?place-bomb-player-id ?player-current-xy ?fire-length ?timestamp)))

(defrule player-dies
  "Player dies if she gets hit by fire."
  [TimestampNow (= ?now now)]
  [?dead-player <- PlayerOnBoardPosition (= ?game-id game-id) (= ?player-id player-id)      (= ?player-current-xy player-current-xy)]
  [FireOnBoard                           (= ?game-id game-id) (= ?fire-player-id player-id) (= ?fire-current-xy fire-position-xy)]
  [:test (= ?fire-current-xy ?player-current-xy)]
  =>
  (retract! ?dead-player)
  (insert-unconditional! (PlayerDies. ?now ?game-id ?player-id ?fire-player-id)))

(defrule only-on-player-left-thats-the-winner
  [TimestampNow (= ?now now)]
  [GameState (= ?game-id game-id) (= :started game-state)]
  [?players-alive <- (acc/all) :from [PlayerOnBoardPosition (= ?game-id game-id)]]
  [:test (= (count ?players-alive) 1)]
  =>
  (let [alive-player-name (-> ?players-alive first :player-name)]
    (insert-unconditional! (GameWinner. ?now ?game-id alive-player-name))
    (insert-unconditional! (GameShutdown. ?now ?game-id))))

(defrule no-player-left-is-a-knockout
  [TimestampNow (= ?now now)]
  [GameState (= ?game-id game-id) (= :started game-state)]
  [?player-alive <- (acc/all) :from [PlayerOnBoardPosition (= ?game-id game-id)]]
  [:test (empty? ?player-alive)]
  =>
  (insert-unconditional! (GameWinner.   ?now ?game-id "K.O."))
  (insert-unconditional! (GameShutdown. ?now ?game-id)))

(defrule game-is-in-shutdown-stage
  "If game is in shutdown, end it."
  [TimestampNow (= ?now now)]
  [GameIsInShutdown (= ?game-id game-id)]
  =>
  (insert! (EndGame. ?now ?game-id)))

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
                                    (FireToAdd.   ?now ?game-id ?player-id [x y] ?now)]))
                           (apply concat))]
    (apply insert! fire-on-board)))

(defrule bomb-exploding-after-timeout
  "Bomb that been on board for a time threshold should expload."
  [TimestampNow (= ?now now)]
  [?bomb <- BombOnBoard (= ?game-id game-id) (= ?player-id player-id) (= ?bomb-added-timestamp bomb-added-timestamp) (= ?bomb-position-xy bomb-position-xy) (= ?fire-length fire-length)]
  [:test (< 3000 (datetime/milliseconds-between ?bomb-added-timestamp ?now))]
  =>
  (retract! ?bomb)
  (insert-unconditional! (BombToRemove.   ?now ?game-id ?bomb-position-xy))
  (insert-unconditional! (BombExploading. ?now ?game-id ?player-id ?bomb-position-xy ?fire-length)))

(defrule bomb-exploading-when-hit-by-fire
  "Bomb is exploading if it's hit by fire."
  [TimestampNow (= ?now now)]
  [FireOnBoard (= ?game-id game-id) (= ?player-id player-id) (= ?current-fire-xy fire-position-xy)]
  [?bomb <- BombOnBoard (= ?game-id game-id) (= ?player-id player-id) (= ?bomb-position-xy bomb-position-xy) (= ?fire-length fire-length)]
  [:test (= ?bomb-position-xy ?current-fire-xy)]
  =>
  (retract! ?bomb)
  (insert-unconditional! (BombToRemove.   ?now ?game-id ?bomb-position-xy))
  (insert-unconditional! (BombExploading. ?now ?game-id ?player-id ?bomb-position-xy ?fire-length)))

(defrule remove-stones-hit-by-fire
  "If a stone is hit by fire, remove it."
  [TimestampNow (= ?now now)]
  [FireOnBoard (= ?game-id game-id) (= ?fire-position-xy fire-position-xy)]
  [?stone <- Stone       (= ?game-id game-id) (= ?stone-position-xy stone-position-xy)]
  [:test (= ?fire-position-xy ?stone-position-xy)]
  =>
  (insert-unconditional! (StoneToRemove. ?now ?game-id ?stone-position-xy)))

(defrule fire-burns-out
  "Fire on board burns out after some time."
  [TimestampNow (= ?now now)]
  [?fire <- FireOnBoard (= ?game-id game-id) (= ?fire-position-xy fire-position-xy) (= ?fire-start-timestamp fire-start-timestamp)]
  [:test (< 1500 (datetime/milliseconds-between ?fire-start-timestamp ?now))]
  =>
  (retract! ?fire)
  (insert-unconditional! (FireToRemove. ?now ?game-id ?fire-position-xy)))

(defrule warn-player-about-game-name-collision
  [TimestampNow (= ?now now)]
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
  [TimestampNow (= ?now now)]
  [?wants-to-create-game <- WantsToCreateGame
   (= ?game-name game-name)
   (= ?game-id game-id)
   (= ?password password)
   (= ?board board)
   (= ?stones stones)
   (= ?items items)]
  =>
  (insert! (CreateGame. ?now ?game-id ?game-name ?password ?board ?stones ?items)))

(defrule player-wants-to-start-game
  [TimestampNow (= ?now now)]
  [?player-wants-to-start-game <- PlayerWantsToStartGame (= ?game-id game-id)]
  [GameState                                             (= ?game-id game-id) (= :created game-state)]
  [?players-alive <- (acc/all) :from [PlayerOnBoardPosition (= ?game-id game-id)]]
  [:test (<= 2 (count ?players-alive))]
  =>
  (retract! ?player-wants-to-start-game)
  (insert-unconditional! (StartGame. ?now ?game-id)))

(defrule player-wants-to-start-game-but-not-enough-player-have-joined
  [?player-wants-to-start-game <- PlayerWantsToStartGame (= ?game-id game-id)]
  [GameState                                             (= ?game-id game-id) (= :created game-state)]
  [?players-alive <- (acc/all) :from [PlayerOnBoardPosition (= ?game-id game-id)]]
  [:test (< (count ?players-alive) 2)]
  =>
  (retract! ?player-wants-to-start-game)
  (insert-unconditional! (StartGameError. ?game-id "Not enough players! Minimum is 2.")))

(def player-positions
  {1 [1  1]
   2 [17 9]
   3 [1  9]
   4 [17 1]})

(defrule players-wants-to-join-game
  [TimestampNow               (= ?now now)]
  [ActiveGame                 (= :created game-state) (= ?game-id game-id) (= ?game-password password)]
  [?players-wants-to-join-game <- (acc/all) :from [PlayerWantsToJoinGame (= ?game-id game-id) (= password ?game-password)]]
  [?players-joined             <- (acc/all) :from [PlayerOnBoardPosition (= ?game-id game-id)]]
  [:test (seq ?players-wants-to-join-game)]
  =>
  (let [number-of-player-already-joined (count ?players-joined)]
    (doseq [[idx {:keys [player-id player-name] :as player}] (some->> ?players-wants-to-join-game
                                                                      (sort-by :player-name)
                                                                      (map-indexed list))]
      (let [idx       (inc idx)
            player-nr (+ number-of-player-already-joined idx)
            position  (get player-positions player-nr)]
        (insert! (JoinGame. ?now ?game-id player-id player-name player-nr position))))))

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
  [TimestampNow            (= ?now now)]
  [ItemOnBoard             (= ?game-id game-id) (= ?item-position-xy item-position-xy) (= item-power :inc-fire-length)]
  [PlayerOnBoardFireLength (= ?game-id game-id) (= ?player-id player-id) (= ?player-position-xy player-position-xy) (= ?fire-length fire-length)]
  [:test (= ?item-position-xy ?player-position-xy)]
  =>
  (insert! (PlayerPicksFireIncItemFromBoard. ?now ?game-id ?player-id ?item-position-xy (inc ?fire-length))))

(defrule created-game-timeout
  "A game left in lobby will be removed after a certain amount of time.
300000ms = 5min"
  [TimestampNow         (= ?now now)]
  [GameCreatedTimestamp (= ?game-created-timestamp timestamp) (= ?game-id game-id)]
  [:test (< 300000 (datetime/milliseconds-between ?game-created-timestamp ?now))]
  =>
  (insert! (CreatedGameInactivityTimeout. ?now ?game-id :inactivity)))

(defrule started-game-timeout
  "A running game will be removed after a certain amount of time.
600000ms = 10min"
  [TimestampNow         (= ?now now)]
  [GameStartedTimestamp (= ?game-created-timestamp timestamp) (= ?game-id game-id)]
  [:test (< 600000 (datetime/milliseconds-between ?game-created-timestamp ?now))]
  =>
  (insert! (StartedGameInactivityTimeout. ?now ?game-id :inactivity)))



;; For the frontend
(defrule start-game-but-not-enough-player-have-joined
  "Used by frontend to determine if a game can be started."
  [GameState                                             (= ?game-id game-id) (= :created game-state)]
  [?players-alive <- (acc/all) :from [PlayerOnBoardPosition (= ?game-id game-id)]]
  [:test (< (count ?players-alive) 2)]
  =>
  (insert-unconditional! (StartGameError. ?game-id "Not enough players! Minimum is 2.")))

;; (defrule created-game-ends-automatically-if-timeout-is-reached
;;   "If game is in lobby more that a certain amount of time. It's removed."

;;   )

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
(defquery created-game-inactivity-timeout?
  []
  [?created-game-inactivity-timeout <- CreatedGameInactivityTimeout])

(defquery started-game-inactivity-timeout?
  []
  [?started-game-inactivity-timeout <- StartedGameInactivityTimeout])

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

(defquery game-shutdown?
  []
  [?game-shutdown <- GameShutdown])

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



(defsession bomberman-session 'se.jherrlin.claraman.claraman-rules)



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
      :game-shutdowns              (map :?game-shutdown        (query session' game-shutdown?))

      :picks-fire-inc-item-from-board (map :?picks-fire-inc-item-from-board (query session' picks-fire-inc-item-from-board?))

      :inactive-created-games (map :?created-game-inactivity-timeout (query session' created-game-inactivity-timeout?))
      :inactive-started-games (map :?started-game-inactivity-timeout (query session' started-game-inactivity-timeout?))}}))

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

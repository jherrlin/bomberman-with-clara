(ns se.jherrlin.clara-labs.bomberman-rules
  (:require [clara.rules :refer [defquery defrule defsession fire-rules insert insert! insert-all insert-unconditional! query retract!]]
            [clara.rules.accumulators :as acc]
            [clara.tools.inspect :as inspect]
            [se.jherrlin.server.events :as events]
            [se.jherrlin.clara-labs.board :as board]
            [se.jherrlin.clara-labs.fire-spread :as fire-spread]
            [se.jherrlin.clara-labs.datetime :as datetime]
            [clojure.set :as set]))

(comment
  (remove-ns 'se.jherrlin.clara-labs.bomberman-rules)
  )


(defrecord Board                  [game-id board])
(defrecord BombExploading         [game-id player-id position-xy fire-length])
(defrecord BombOnBoard            [game-id player-id bomb-position-xy       fire-length bomb-added-timestamp])
(defrecord DeadPlayer             [game-id player-id killed-by-player-id])
(defrecord FireOnBoard            [game-id player-id fire-position-xy fire-start-timestamp])
(defrecord FlyingBomb             [game-id player-id flying-bomb-current-xy fire-length bomb-added-timestamp flying-bomb-direction])
(defrecord PlayerMove             [game-id player-id next-position direction])
(defrecord PlayerPositionOnBoard  [game-id player-id player-current-xy])
(defrecord PlayerWantsToMove      [game-id player-id current-xy direction])
(defrecord PlayerWantsToPlaceBomb [game-id player-id current-xy fire-length timestamp max-nr-of-bombs-for-player])
(defrecord PlayerWantsToThrowBomb [game-id player-id players-current-xy       player-facing-direction])
(defrecord Stone                  [game-id stone-position-xy])
(defrecord StoneToRemove          [game-id position-xy])
(defrecord TimestampNow           [now])

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
  (retract! ?player-wants-to-throw-bomb)
  (retract! ?bomb-on-board)
  (insert-unconditional! (->FlyingBomb
                          ?game-id
                          ?player-id
                          (board/next-xy-position ?players-current-xy ?player-facing-direction)
                          ?fire-length
                          ?bomb-added-timestamp
                          ?player-facing-direction)))

(defrule flying-bomb-lands-on-empty-floor
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
  [:not [Stone       (= ?game-id game-id) (= stone-position-xy (board/next-xy-position ?flying-bomb-current-xy ?flying-bomb-direction))]]
  [:not [BombOnBoard (= ?game-id game-id) (= bomb-position-xy  (board/next-xy-position ?flying-bomb-current-xy ?flying-bomb-direction))]]
  [:test (#{:floor} (board/target-position-type ?board ?flying-bomb-current-xy ?flying-bomb-direction))]
  =>
  (retract! ?flying-bomb)
  (insert-unconditional! (->BombOnBoard
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
  (insert-unconditional! (->FlyingBomb
                          ?game-id
                          ?player-id
                          (board/next-xy-board-position ?board ?flying-bomb-current-xy ?flying-bomb-direction)
                          ?fire-length
                          ?bomb-added-timestamp
                          ?flying-bomb-direction)))

(defrule player-move
  "Player move"
  [Board (= ?game-id game-id) (= ?board board)]
  [?player-wants-to-move <- PlayerWantsToMove (= ?game-id game-id) (= ?player-id player-id) (= ?current-xy current-xy) (= ?direction direction)
   (#{:floor} (board/target-position-type ?board current-xy direction))]
  [:not [Stone       (= ?game-id game-id) (= stone-position-xy (board/next-xy-position ?current-xy ?direction))]]
  [:not [BombOnBoard (= ?game-id game-id) (= bomb-position-xy  (board/next-xy-position ?current-xy ?direction))]]
  =>
  (insert-unconditional! (->PlayerMove ?game-id ?player-id (board/next-xy-position ?current-xy ?direction) ?direction)))

(defrule place-bomb
  "Player place bomb in her current location."
  [PlayerWantsToPlaceBomb
   (= ?game-id game-id)
   (= ?place-bomb-player-id player-id)
   (= ?fire-length fire-length)
   (= ?player-current-xy current-xy)
   (= ?timestamp timestamp)
   (= ?max-nr-of-bombs-for-player max-nr-of-bombs-for-player)]
  [:not [BombOnBoard
         (= ?game-id game-id)
         (= bomb-position-xy ?player-current-xy)]]
  [?bombs-placed-by-player <- (acc/count) from [BombOnBoard (= ?game-id game-id) (= player-id ?place-bomb-player-id)]]
  [:test (< ?bombs-placed-by-player ?max-nr-of-bombs-for-player)]
  =>
  (insert-unconditional! (->BombOnBoard ?game-id ?place-bomb-player-id ?player-current-xy ?fire-length ?timestamp)))

(defrule player-dies
  "Player dies if she gets hit by fire."
  [PlayerPositionOnBoard (= ?game-id game-id) (= ?player-id player-id)      (= ?player-current-xy player-current-xy)]
  [FireOnBoard         (= ?game-id game-id) (= ?fire-player-id player-id) (= ?fire-current-xy fire-position-xy)
   (= ?fire-current-xy ?player-current-xy)]
  =>
  (insert! (->DeadPlayer ?game-id ?player-id ?fire-player-id)))

(defrule exploading-bomb-throws-fire-flames
  "When a bomb exploads, fire is created in all four directions.
When fire hits a wall it stops.
When fire huts a stone it saves the fire to that stone but discard the rest in that latitude."
  [Board (= ?game-id game-id) (= ?board board)]
  [TimestampNow (= ?now now)]
  [BombExploading (= ?game-id game-id) (= ?player-id player-id) (= ?bomb-position-xy position-xy) (= ?fire-length fire-length)]
  [?stones <- (acc/all) :from [Stone (= ?game-id game-id)]]
  =>
  (let [fire-on-board (mapv (fn [[x y]] (->FireOnBoard ?game-id ?player-id [x y] ?now))
                            (fire-spread/fire-after-it-hit-objects ?bomb-position-xy ?fire-length ?board ?stones))]
    (apply insert! fire-on-board)))

(defrule bomb-exploding-after-timeout
  "Bomb that been on board for a time threshold should expload."
  [TimestampNow (= ?now now)]
  [?bomb <- BombOnBoard (= ?game-id game-id) (= ?player-id player-id) (= ?bomb-added-timestamp bomb-added-timestamp) (= ?bomb-position-xy bomb-position-xy) (= ?fire-length fire-length)]
  [:test (< 3000 (datetime/milliseconds-between ?bomb-added-timestamp ?now))]
  =>
  (retract! ?bomb)
  (insert-unconditional! (->BombExploading ?game-id ?player-id ?bomb-position-xy ?fire-length)))

(defrule bomb-exploading-when-hit-by-fire
  "Bomb is exploading if it's hit by fire."
  [FireOnBoard (= ?game-id game-id) (= ?player-id player-id) (= ?current-fire-xy fire-position-xy)]
  [?bomb <- BombOnBoard (= ?game-id game-id) (= ?player-id player-id) (= ?bomb-position-xy bomb-position-xy) (= ?fire-length fire-length)]
  [:test (= ?bomb-position-xy ?current-fire-xy)]
  =>
  (retract! ?bomb)
  (insert-unconditional! (->BombExploading ?game-id ?player-id ?bomb-position-xy ?fire-length)))

(defrule remove-stones-hit-by-fire
  "If a stone is hit by fire, remove it."
  [FireOnBoard (= ?game-id game-id) (= ?fire-position-xy fire-position-xy)]
  [Stone       (= ?game-id game-id) (= ?stone-position-xy stone-position-xy)]
  [:test (= ?fire-position-xy ?stone-position-xy)]
  =>
  (insert! (->StoneToRemove ?game-id ?stone-position-xy)))

(defrule fire-burns-out
  "Fire on board burns out after some time."
  [TimestampNow (= ?now now)]
  [?fire <- FireOnBoard (= ?game-id game-id) (= ?fire-start-timestamp fire-start-timestamp)]
  [:test (< 1500 (datetime/milliseconds-between ?fire-start-timestamp ?now))]
  =>
  (retract! ?fire))



;; Queries
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

(defquery dead-players?
  []
  [?dead-players <- DeadPlayer])

(defquery bomb-on-board?
  []
  [?bomb-on-board <- BombOnBoard])

(defquery flying-bombs?
  []
  [?flying-bombs <- FlyingBomb])

(defquery player-wants-to-move?
  []
  [?player-wants-to-move <- PlayerWantsToMove])


(defsession bomberman-session 'se.jherrlin.clara-labs.bomberman-rules)




(defn run-rules [facts]
  (let [session  (insert-all bomberman-session facts)
        session' (fire-rules session)]
    {:actions
     {:player-moves         (map :?player-move           (query session' player-move?))
      :exploading-bombs     (map :?exploading-bombs      (query session' exploading-bombs?))
      :bombs-on-board       (map :?bomb-on-board         (query session' bomb-on-board?))
      :fire-on-board        (map :?fire-on-board         (query session' fire-on-board?))
      :stones-to-remove     (map :?stones-to-remove      (query session' stones-to-remove?))
      :dead-players         (map :?dead-players          (query session' dead-players?))
      :flying-bombs         (map :?flying-bombs          (query session' flying-bombs?))
      :player-wants-to-move (map :?player-wants-to-move  (query session' player-wants-to-move?))}}))

(comment
  (def repl-game-id #uuid "c03e430f-2b24-4109-a923-08c986a682a8")

  (run-rules
   [(->Board      repl-game-id board/board2)
    (->FlyingBomb repl-game-id 1 [2 1] 3 #inst "2021-09-07T19:50:17.258-00:00" :east)
    (->Stone      repl-game-id [3 1])])
  (run-rules
   [(->Board        repl-game-id (board/init 6))
    (->TimestampNow              #inst "2021-08-28T15:03:50.100-00:00")
    (->BombOnBoard  repl-game-id   1 [1 1] 10 #inst "2021-08-28T15:03:47.100-00:00")
    (->BombOnBoard  repl-game-id   1 [1 3] 10 #inst "2021-08-28T15:03:49.100-00:00")
    (->Stone        repl-game-id     [3 1])
    (->Stone        repl-game-id     [3 3])])
  (let [session (insert-all bomberman-session
                            [(->TimestampNow              #inst "2021-08-28T15:03:50.100-00:00")
                             (->Board         repl-game-id (board/init 6))
                             (->BombOnBoard   repl-game-id  1 [1 1] 10 #inst "2021-08-28T15:03:47.100-00:00")
                             (->BombOnBoard   repl-game-id  1 [1 3] 10 #inst "2021-08-28T15:03:49.100-00:00")
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

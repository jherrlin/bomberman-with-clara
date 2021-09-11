(ns se.jherrlin.clara-labs.bomberman-rules
  (:require [clara.rules :refer [defquery defrule defsession fire-rules insert insert! insert-all insert-unconditional! query retract!]]
            [clara.rules.accumulators :as acc]
            [clara.tools.inspect :as inspect]
            [se.jherrlin.clara-labs.board :as board]
            [se.jherrlin.clara-labs.fire-spread :as fire-spread]
            [se.jherrlin.clara-labs.datetime :as datetime]
            [clojure.set :as set]))

(comment
  (remove-ns 'se.jherrlin.clara-labs.bomberman-rules)
  )


(defrecord TimestampNow         [now])
(defrecord Board                [game-id board])
(defrecord BombExploading       [game-id user-id position-xy fire-length])
(defrecord DeadUser             [game-id user-id killed-by-user-id])
(defrecord FireOnBoard          [game-id user-id fire-position-xy fire-start-timestamp])
(defrecord Stone                [game-id stone-position-xy])
(defrecord StoneToRemove        [game-id position-xy])

(defrecord UserPositionOnBoard  [game-id user-id user-current-xy])

(defrecord UserWantsToMove      [game-id user-id current-xy direction])
(defrecord UserMove             [game-id user-id next-position direction])

(defrecord UserWantsToPlaceBomb [game-id user-id current-xy fire-length timestamp max-nr-of-bombs-for-user])
(defrecord BombOnBoard          [game-id user-id bomb-position-xy       fire-length bomb-added-timestamp])

(defrecord UserWantsToThrowBomb [game-id user-id users-current-xy       user-facing-direction])
(defrecord FlyingBomb           [game-id user-id flying-bomb-current-xy fire-length bomb-added-timestamp flying-bomb-direction])


(defrule user-throws-bomb
  "A user can throw a bomb if facing direction points to it."
  [?user-wants-to-throw-bomb <- UserWantsToThrowBomb
   (= ?game-id game-id)
   (= ?user-id user-id)
   (= ?users-current-xy users-current-xy)
   (= ?user-facing-direction user-facing-direction)]
  [?bomb-on-board            <- BombOnBoard
   (= ?game-id game-id)
   (= ?bomb-position-xy bomb-position-xy)
   (= ?fire-length fire-length)
   (= ?bomb-added-timestamp bomb-added-timestamp)]
  [:test (= ?bomb-position-xy (board/next-xy-position ?users-current-xy ?user-facing-direction))]
  =>
  (retract! ?user-wants-to-throw-bomb)
  (retract! ?bomb-on-board)
  (insert-unconditional! (->FlyingBomb
                          ?game-id
                          ?user-id
                          (board/next-xy-position ?users-current-xy ?user-facing-direction)
                          ?fire-length
                          ?bomb-added-timestamp
                          ?user-facing-direction)))

(defrule flying-bomb-lands-on-empty-floor
  ""
  [Board
   (= ?game-id game-id)
   (= ?board board)]
  [?flying-bomb <- FlyingBomb
   (= ?game-id game-id)
   (= ?user-id user-id)
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
                          ?user-id
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
   (= ?user-id user-id)
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
                          ?user-id
                          (board/next-xy-board-position ?board ?flying-bomb-current-xy ?flying-bomb-direction)
                          ?fire-length
                          ?bomb-added-timestamp
                          ?flying-bomb-direction)))

(defrule user-move
  "User move"
  [Board (= ?game-id game-id) (= ?board board)]
  [?user-wants-to-move <- UserWantsToMove (= ?game-id game-id) (= ?user-id user-id) (= ?current-xy current-xy) (= ?direction direction)
   (#{:floor} (board/target-position-type ?board current-xy direction))]
  [:not [Stone       (= ?game-id game-id) (= stone-position-xy (board/next-xy-position ?current-xy ?direction))]]
  [:not [BombOnBoard (= ?game-id game-id) (= bomb-position-xy  (board/next-xy-position ?current-xy ?direction))]]
  =>
  (insert-unconditional! (->UserMove ?game-id ?user-id (board/next-xy-position ?current-xy ?direction) ?direction)))

(defrule place-bomb
  "User place bomb in her current location."
  [UserWantsToPlaceBomb
   (= ?game-id game-id)
   (= ?place-bomb-user-id user-id)
   (= ?fire-length fire-length)
   (= ?user-current-xy current-xy)
   (= ?timestamp timestamp)
   (= ?max-nr-of-bombs-for-user max-nr-of-bombs-for-user)]
  [:not [BombOnBoard
         (= ?game-id game-id)
         (= bomb-position-xy ?user-current-xy)]]
  [?bombs-placed-by-user <- (acc/count) from [BombOnBoard (= ?game-id game-id) (= user-id ?place-bomb-user-id)]]
  [:test (< ?bombs-placed-by-user ?max-nr-of-bombs-for-user)]
  =>
  (insert-unconditional! (->BombOnBoard ?game-id ?place-bomb-user-id ?user-current-xy ?fire-length ?timestamp)))

(defrule user-dies
  "User dies if she gets hit by fire."
  [UserPositionOnBoard (= ?game-id game-id) (= ?user-id user-id)      (= ?user-current-xy user-current-xy)]
  [FireOnBoard         (= ?game-id game-id) (= ?fire-user-id user-id) (= ?fire-current-xy fire-position-xy)
   (= ?fire-current-xy ?user-current-xy)]
  =>
  (insert! (->DeadUser ?game-id ?user-id ?fire-user-id)))

(defrule exploading-bomb-throws-fire-flames
  "When a bomb exploads, fire is created in all four directions.
When fire hits a wall it stops.
When fire huts a stone it saves the fire to that stone but discard the rest in that latitude."
  [Board (= ?game-id game-id) (= ?board board)]
  [TimestampNow (= ?now now)]
  [BombExploading (= ?game-id game-id) (= ?user-id user-id) (= ?bomb-position-xy position-xy) (= ?fire-length fire-length)]
  [?stones <- (acc/all) :from [Stone (= ?game-id game-id)]]
  =>
  (let [fire-on-board (mapv (fn [[x y]] (->FireOnBoard ?game-id ?user-id [x y] ?now))
                            (fire-spread/fire-after-it-hit-objects ?bomb-position-xy ?fire-length ?board ?stones))]
    (apply insert! fire-on-board)))

(defrule bomb-exploding-after-timeout
  "Bomb that been on board for a time threshold should expload."
  [TimestampNow (= ?now now)]
  [?bomb <- BombOnBoard (= ?game-id game-id) (= ?user-id user-id) (= ?bomb-added-timestamp bomb-added-timestamp) (= ?bomb-position-xy bomb-position-xy) (= ?fire-length fire-length)]
  [:test (< 3000 (datetime/milliseconds-between ?bomb-added-timestamp ?now))]
  =>
  (retract! ?bomb)
  (insert-unconditional! (->BombExploading ?game-id ?user-id ?bomb-position-xy ?fire-length)))

(defrule bomb-exploading-when-hit-by-fire
  "Bomb is exploading if it's hit by fire."
  [FireOnBoard (= ?game-id game-id) (= ?user-id user-id) (= ?current-fire-xy fire-position-xy)]
  [?bomb <- BombOnBoard (= ?game-id game-id) (= ?user-id user-id) (= ?bomb-position-xy bomb-position-xy) (= ?fire-length fire-length)]
  [:test (= ?bomb-position-xy ?current-fire-xy)]
  =>
  (retract! ?bomb)
  (insert-unconditional! (->BombExploading ?game-id ?user-id ?bomb-position-xy ?fire-length)))

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
(defquery user-move?
  []
  [?user-move <- UserMove])

(defquery stones-to-remove?
  []
  [?stones-to-remove <- StoneToRemove])

(defquery exploading-bombs?
  []
  [?exploading-bombs <- BombExploading])

(defquery fire-on-board?
  []
  [?fire-on-board <- FireOnBoard])

(defquery dead-users?
  []
  [?dead-users <- DeadUser])

(defquery bomb-on-board?
  []
  [?bomb-on-board <- BombOnBoard])

(defquery flying-bombs?
  []
  [?flying-bombs <- FlyingBomb])

(defquery user-wants-to-move?
  []
  [?user-wants-to-move <- UserWantsToMove])


(defsession bomberman-session 'se.jherrlin.clara-labs.bomberman-rules)




(defn run-rules [facts]
  (let [session  (insert-all bomberman-session facts)
        session' (fire-rules session)]
    {:actions
     {:user-moves                (map :?user-move                 (query session' user-move?))
      :exploading-bombs          (map :?exploading-bombs          (query session' exploading-bombs?))
      :bombs-on-board            (map :?bomb-on-board             (query session' bomb-on-board?))
      :fire-on-board             (map :?fire-on-board             (query session' fire-on-board?))
      :stones-to-remove          (map :?stones-to-remove          (query session' stones-to-remove?))
      :dead-users                (map :?dead-users                (query session' dead-users?))
      :flying-bombs              (map :?flying-bombs              (query session' flying-bombs?))
      :user-wants-to-move        (map :?user-wants-to-move        (query session' user-wants-to-move?))}}))

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
     {:user-moves         (map :?user-move (query session' user-move?))
      :exploading-bombs   (map :?exploading-bombs (query session' exploading-bombs?))
      :dead-users         (map :?dead-users (query session' dead-users?))}}
    (query session)
    )
  )

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



(defrecord Board                [board])
(defrecord BombExploading       [user-id position-xy fire-length])
(defrecord BombOnBoard          [user-id bomb-position-xy fire-length bomb-added-timestamp])
(defrecord DeadUser             [user-id killed-by-user-id])
(defrecord FireOnBoard          [user-id fire-position-xy fire-start-timestamp])
(defrecord Stone                [stone-position-xy]) ;; Object on the board that can be removed by fire
(defrecord StoneToRemove        [position-xy])
(defrecord TimestampNow         [now])
(defrecord UserMove             [user-id next-position])
(defrecord UserPositionOnBoard  [user-id user-current-xy])
(defrecord UserWantsToMove      [user-id current-xy direction])
(defrecord UserWantsToPlaceBomb [user-id current-xy fire-length timestamp max-nr-of-bombs-for-user])


(defrule user-move
  "User move"
  [Board (= ?board board)]
  [?user-wants-to-move <- UserWantsToMove (= ?user-id user-id) (= ?current-xy current-xy) (= ?direction direction)
   (#{:floor} (board/target-position-type ?board current-xy direction))]
  [:not [Stone       (= stone-position-xy (board/next-xy-position ?current-xy ?direction))]]
  [:not [BombOnBoard (= bomb-position-xy  (board/next-xy-position ?current-xy ?direction))]]
  =>
  (insert! (->UserMove ?user-id (board/next-xy-position ?current-xy ?direction))))

(defrule place-bomb
  "User place bomb in her current location."
  [UserWantsToPlaceBomb (= ?place-bomb-user-id user-id) (= ?fire-length fire-length) (= ?user-current-xy current-xy) (= ?timestamp timestamp)
   (= ?max-nr-of-bombs-for-user max-nr-of-bombs-for-user)]
  [:not [BombOnBoard (= bomb-position-xy ?user-current-xy)]]
  [?bombs-placed-by-user <- (acc/count) from [BombOnBoard (= user-id ?place-bomb-user-id)]]
  [:test (< ?bombs-placed-by-user ?max-nr-of-bombs-for-user)]
  =>
  (insert-unconditional! (->BombOnBoard ?place-bomb-user-id ?user-current-xy ?fire-length ?timestamp)))

(defrule user-dies
  "User dies if she gets hit by fire."
  [UserPositionOnBoard (= ?user-id user-id)      (= ?user-current-xy user-current-xy)]
  [FireOnBoard         (= ?fire-user-id user-id) (= ?fire-current-xy fire-position-xy)
   (= ?fire-current-xy ?user-current-xy)]
  =>
  (insert! (->DeadUser ?user-id ?fire-user-id)))

(defrule exploading-bomb-throws-fire-flames
  "When a bomb exploads, fire is created in all four directions.
When fire hits a wall it stops.
When fire huts a stone it saves the fire to that stone but discard the rest in that latitude."
  [Board (= ?board board)]
  [TimestampNow (= ?now now)]
  [BombExploading (= ?user-id user-id) (= ?bomb-position-xy position-xy) (= ?fire-length fire-length)]
  [?stones <- (acc/all) :from [Stone]]
  =>
  (let [fire-on-board (mapv (fn [[x y]] (->FireOnBoard ?user-id [x y] ?now))
                            (fire-spread/remove-fires-that-meet-obstacles ?bomb-position-xy ?fire-length ?board ?stones))]
    (apply insert! fire-on-board)))

(defrule bomb-exploding-after-timeout
  "Bomb that been on board for a time threshold should expload."
  [TimestampNow (= ?now now)]
  [?bomb <- BombOnBoard (= ?user-id user-id) (= ?bomb-added-timestamp bomb-added-timestamp) (= ?bomb-position-xy bomb-position-xy) (= ?fire-length fire-length)]
  [:test (< 2000 (datetime/milliseconds-between ?bomb-added-timestamp ?now))]
  =>
  (retract! ?bomb)
  (insert-unconditional! (->BombExploading ?user-id ?bomb-position-xy ?fire-length)))

(defrule bomb-exploading-when-hit-by-fire
  "Bomb is exploading if it's hit by fire."
  [FireOnBoard (= ?user-id user-id) (= ?current-fire-xy fire-position-xy)]
  [?bomb <- BombOnBoard (= ?user-id user-id) (= ?bomb-position-xy bomb-position-xy) (= ?fire-length fire-length)]
  [:test (= ?bomb-position-xy ?current-fire-xy)]
  =>
  (retract! ?bomb)
  (insert! (->BombExploading ?user-id ?bomb-position-xy ?fire-length)))

(defrule remove-stones-hit-by-fire
  "If a stone is hit by fire, remove it."
  [FireOnBoard (= ?fire-position-xy fire-position-xy)]
  [Stone       (= ?stone-position-xy stone-position-xy)]
  [:test (= ?fire-position-xy ?stone-position-xy)]
  =>
  (insert! (->StoneToRemove ?stone-position-xy)))

(defrule fire-burns-out
  "Fire on board burns out after some time."
  [TimestampNow (= ?now now)]
  [?fire <- FireOnBoard (= ?fire-start-timestamp fire-start-timestamp)]
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
      :dead-users                (map :?dead-users                (query session' dead-users?))}}))


(comment
  ;; Buggy case!
  (run-rules
   [(map->Board {:board board/board2})
    (map->UserWantsToPlaceBomb {:user-id                  1,
                                :current-xy               [7 9],
                                :fire-length              3,
                                :timestamp                #inst "2021-09-07T10:17:10.456-00:00",
                                :max-nr-of-bombs-for-user 3})
    (map->UserWantsToMove {:user-id 1, :current-xy [7 9], :direction :west})
    (map->FireOnBoard {:user-id              1,
                       :fire-position-xy     [7 9],
                       :fire-start-timestamp #inst "2021-09-07T10:17:10.255-00:00"})
    ])
  )

(comment
  (run-rules
   [(->Board (board/init 6))
    (->TimestampNow              #inst "2021-08-28T15:03:50.100-00:00")
    (->BombOnBoard     1 [1 1] 10 #inst "2021-08-28T15:03:47.100-00:00")
    (->BombOnBoard     1 [1 3] 10 #inst "2021-08-28T15:03:49.100-00:00")
    (->Stone             [3 1])
    (->Stone             [3 3])]
   )
  )



(comment
  (let [board   (board/init 6)
        session (insert-all bomberman-session
                            [(->Board (board/init 6))
                             (->TimestampNow              #inst "2021-08-28T15:03:50.100-00:00")
                             (->BombOnBoard     1 [1 1] 10 #inst "2021-08-28T15:03:47.100-00:00")
                             (->BombOnBoard     1 [1 3] 10 #inst "2021-08-28T15:03:49.100-00:00")
                             (->Stone             [3 1])
                             (->Stone             [3 3])])
        session' (fire-rules session)]
    {:actions
     {:user-moves         (map :?user-move (query session' user-move?))
      :exploading-bombs   (map :?exploading-bombs (query session' exploading-bombs?))
      :dead-users         (map :?dead-users (query session' dead-users?))}}
    (query session)
    )
  )

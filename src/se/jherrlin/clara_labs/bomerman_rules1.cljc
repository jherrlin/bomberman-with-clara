(ns se.jherrlin.clara-labs.bomerman-rules1
  (:require [clara.rules :refer [insert insert-all fire-rules query insert! defrule defsession defquery]]
            [se.jherrlin.clara-labs.board :as board]
            [se.jherrlin.clara-labs.datetime :as datetime]))



(defrecord UserWantsToMove      [user-id board current-xy direction])
(defrecord UserMove             [user-id next-position])
(defrecord BombOnBoard          [user-id position-xy bomb-added-timestamp timestamp-now])
(defrecord UserWantsToPlaceBomb [user-id current-xy fire-length timestamp])
(defrecord AddBombToBoard       [user-id location-xy fire-length timestamp])
(defrecord Fire                 [user-id current-xy])


(defrule user-move
  "User move"
  [UserWantsToMove (= ?user-id user-id) (= ?current-xy current-xy) (= ?direction direction)
   (#{:floor} (board/target-position-type board current-xy direction))]
  [:not [BombOnBoard (= position-xy (board/next-xy-position ?current-xy ?direction))]]
  =>
  (insert! (->UserMove ?user-id (board/next-xy-position ?current-xy ?direction))))

(defrule place-bomb
  "User place bomb in her current location."
  [UserWantsToPlaceBomb (= ?user-id user-id) (= ?fire-length fire-length) (= ?current-xy current-xy) (= ?timestamp timestamp)]
  [:not [BombOnBoard (= position-xy ?current-xy)]]
  =>
  (insert! (->AddBombToBoard ?user-id ?current-xy ?fire-length ?timestamp)))


;; VIP
(defrule bomb-exploding
  "Bomb that is on board for 2000ms (2 seconds) should expoad"
  [BombOnBoard (= ?position-xy position-xy)
   (< 2000 (datetime/milliseconds-between bomb-added-timestamp timestamp-now))]
  [Fire]
  =>

  )

(< 2000 (datetime/milliseconds-between #inst "2021-08-28T15:03:47.100-00:00" #inst "2021-08-28T15:03:50.100-00:00"))
(datetime/now!)

(defquery user-move?
  []
  [?user-move <- UserMove])

(defquery placed-bombs?
  []
  [?placed-bombs <- AddBombToBoard])

(defsession bomberman-session 'se.jherrlin.clara-labs.bomerman-rules1)

(let [board (board/init 6)
      session (insert-all bomberman-session [(->UserWantsToMove 1 board [1 1] :north)
                                             (->UserWantsToMove 2 board [1 1] :east)
                                             (->UserWantsToMove 3 board [1 1] :south)
                                             (->UserWantsToMove 4 board [1 1] :west)
                                             (->UserWantsToMove 5 board [3 1] :west)
                                             (->BombOnBoard      1      [2 1] (datetime/now!) (datetime/now!))
                                             (->UserWantsToPlaceBomb 10 [2 1] 3 (datetime/now!))
                                             (->UserWantsToPlaceBomb 11 [1 1] 3 (datetime/now!))
                                             ])
      session' (fire-rules session)]
  [(query session' user-move?)
   (query session' placed-bombs?)
   ])

(ns se.jherrlin.clara-labs.bomerman-rules1
  (:require [clara.rules :refer [defquery defrule defsession fire-rules insert insert! insert-all insert-unconditional! query retract!]]
            [se.jherrlin.clara-labs.board :as board]
            [se.jherrlin.clara-labs.datetime :as datetime]))



(defrecord Board                [board])
(defrecord TimestampNow         [now])
(defrecord UserWantsToMove      [user-id current-xy direction])
(defrecord UserMove             [user-id next-position])
(defrecord BombOnBoard          [user-id position-xy fire-length bomb-added-timestamp])
(defrecord BombExploading       [user-id position-xy])
(defrecord FireOnBoard          [user-id current-xy fire-start-timestamp])
(defrecord RemoveFireFromBoard  [current-xy])
(defrecord UserWantsToPlaceBomb [user-id current-xy fire-length timestamp])
(defrecord AddBombToBoard       [user-id location-xy fire-length timestamp])
(defrecord AddFireToBoard       [user-id location])
(defrecord UserPositionOnBoard  [user-id current-xy])
(defrecord DeadUser             [user-id killed-by-user-id])


(defrule user-move
  "User move"
  [Board (= ?board board)]
  [UserWantsToMove (= ?user-id user-id) (= ?current-xy current-xy) (= ?direction direction)
   (#{:floor} (board/target-position-type ?board current-xy direction))]
  [:not [BombOnBoard (= position-xy (board/next-xy-position ?current-xy ?direction))]]
  =>
  (insert! (->UserMove ?user-id (board/next-xy-position ?current-xy ?direction))))

(defrule place-bomb
  "User place bomb in her current location."
  [UserWantsToPlaceBomb (= ?user-id user-id) (= ?fire-length fire-length) (= ?current-xy current-xy) (= ?timestamp timestamp)]
  [:not [BombOnBoard (= position-xy ?current-xy)]]
  =>
  (insert! (->AddBombToBoard ?user-id ?current-xy ?fire-length ?timestamp)))

(defrule user-died
  "User dies if she gets hit by fire."
  [UserPositionOnBoard (= ?user-id user-id) (= ?user-current-xy current-xy)]
  [FireOnBoard         (= ?fire-user-id user-id) (= ?fire-current-xy current-xy)
   (= ?fire-current-xy ?user-current-xy)]
  =>
  (insert! (->DeadUser ?user-id ?fire-user-id)))

(defn bomb-fire-spread [board [pos-x pos-y] fire-length]
  (let [current       [pos-x pos-y]
        north         (map (fn [y] [pos-x y]) (range (- pos-y fire-length) pos-y))
        south         (map (fn [y] [pos-x y]) (range pos-y (+ pos-y fire-length)))
        east          (map (fn [x] [x pos-y]) (range pos-x (+ pos-x fire-length)))
        west          (map (fn [x] [x pos-y]) (range (- pos-x fire-length) pos-x))]
    (->> (concat north south east west [current])
         (map #(board/individual-state % board))
         (filter (comp #{:floor} :type))
         (into #{})
         (sort-by :x)
         (vec))))

(defrule bomb-exploding-after-timeout
  "Bomb that is on board for 2000ms (2 seconds) should expload."
  [Board (= ?board board)]
  [TimestampNow (= ?now now)]
  [?bomb <- BombOnBoard (= ?user-id user-id) (= ?bomb-position-xy position-xy) (= ?fire-length fire-length)
   (< 2000 (datetime/milliseconds-between bomb-added-timestamp ?now))]
  =>
  (apply insert-unconditional! (mapv (fn [{:keys [x y]}] (->FireOnBoard ?user-id [x y] ?now))
                                     (bomb-fire-spread ?board ?bomb-position-xy ?fire-length)))
  (retract! ?bomb))

(defrule bomb-exploading-when-hit-by-fire
  "Bomb is exploading if it's hit by fire."
  [Board (= ?board board)]
  [TimestampNow (= ?now now)]
  [?bomb <- BombOnBoard (= ?user-id user-id) (= ?bomb-position-xy position-xy) (= ?fire-length fire-length)]
  [FireOnBoard (= ?user-id user-id) (= ?current-fire-xy current-xy)]
  [:test (= ?bomb-position-xy ?current-fire-xy)]
  =>
  (retract! ?bomb)
  (apply insert-unconditional! (mapv (fn [{:keys [x y]}] (->FireOnBoard ?user-id [x y] ?now))
                                     (bomb-fire-spread ?board ?bomb-position-xy ?fire-length))))

(defrule fire-burns-out
  "Fire on board burns out after some time."
  [TimestampNow (= ?now now)]
  [?fire <- FireOnBoard (= ?fire-start-timestamp fire-start-timestamp)]
  [:test (< 1500 (datetime/milliseconds-between ?fire-start-timestamp ?now))]
  =>
  (insert! (->RemoveFireFromBoard ?fire)))


(comment
  (< 2000 (datetime/milliseconds-between #inst "2021-08-28T15:03:47.100-00:00" #inst "2021-08-28T15:03:50.100-00:00"))
  (datetime/now!)
  )

(defquery user-move?
  []
  [?user-move <- UserMove])

(defquery add-bombs-to-board?
  []
  [?add-bombs-to-board <- AddBombToBoard])

(defquery exploading-bombs?
  []
  [?exploading-bombs <- BombExploading])

(defquery fire-to-add?
  []
  [?add-fire-to-board <- AddFireToBoard])

(defquery fire-on-board?
  []
  [?fire-on-board <- FireOnBoard])

(defquery dead-users?
  []
  [?dead-users <- DeadUser])

(defquery fire-that-have-burned-out?
  []
  [?fire-that-have-burned-out <- RemoveFireFromBoard])

(defsession bomberman-session 'se.jherrlin.clara-labs.bomerman-rules1)

(let [board   (board/init 6)
      session (insert-all bomberman-session
                          [(->Board board)
                           (->TimestampNow #inst "2021-08-28T15:03:50.100-00:00")
                           (->UserWantsToMove 1 [1 1] :north)
                           (->UserWantsToMove 2 [1 1] :east)
                           (->UserWantsToMove 3 [1 1] :south)
                           (->UserWantsToMove 4 [1 1] :west)
                           (->UserWantsToMove 5 [3 1] :west)
                           (->BombOnBoard          1  [2 1] 3 (datetime/now!))
                           (->UserWantsToPlaceBomb 10 [2 1] 3 (datetime/now!))
                           (->UserWantsToPlaceBomb 11 [1 1] 3 (datetime/now!))
                           (->BombOnBoard          66 [1 1] 3 #inst "2021-08-28T15:03:47.100-00:00")

                           (->FireOnBoard 1 [5 4] (datetime/now!))
                           (->UserPositionOnBoard 0 [5 4])
                           ])
      session' (fire-rules session)]
  {:actions
   {:user-moves         (map :?user-move (query session' user-move?))
    :add-bombs-to-board (map :?add-bombs-to-board (query session' add-bombs-to-board?))
    :exploading-bombs   (map :?exploading-bombs (query session' exploading-bombs?))
    :add-fires-to-board (map :?add-fire-to-board (query session' fire-to-add?))
    :dead-users         (map :?dead-users (query session' dead-users?))}})




(comment
  ;; Users can only move on floor
  (let [board    [[{:type :wall} {:type :wall}  {:type :wall}  {:type :wall}]
                  [{:type :wall} {:type :floor} {:type :floor} {:type :wall}]
                  [{:type :wall} {:type :wall}  {:type :wall}  {:type :wall}]]
        session  (insert-all bomberman-session
                             [(->Board board)
                              (->UserWantsToMove 1 [1 1] :east)
                              (->UserWantsToMove 2 [1 1] :south)
                              (->UserWantsToMove 3 [1 1] :west)
                              (->UserWantsToMove 4 [1 1] :north)
                              (->UserWantsToMove 5 [2 1] :east)
                              (->UserWantsToMove 6 [2 1] :south)
                              (->UserWantsToMove 7 [2 1] :west)
                              (->UserWantsToMove 8 [2 1] :north)])
        session' (fire-rules session)]
    (query session' user-move?))

  ;; Users cant walk onto a bomb
  (let [board    [[{:type :wall} {:type :wall}  {:type :wall}  {:type :wall}]
                  [{:type :wall} {:type :floor} {:type :floor} {:type :wall}]
                  [{:type :wall} {:type :wall}  {:type :wall}  {:type :wall}]]
        session  (insert-all bomberman-session
                             [(->Board board)
                              (->UserWantsToMove 1 [1 1] :east)
                              (->UserWantsToMove 2 [2 1] :west)
                              (->BombOnBoard     2 [2 1] 3 (datetime/now!) (datetime/now!))])
        session' (fire-rules session)]
    (query session' user-move?))

  (board/init 4)

  ;; Fire exploads bombs
  (let [board    [[{:type :wall, :x 0, :y 0} {:type :wall,  :x 1, :y 0} {:type :wall,  :x 2, :y 0} {:type :wall, :x 3, :y 0}]
                  [{:type :wall, :x 0, :y 1} {:type :floor, :x 1, :y 1} {:type :floor, :x 2, :y 1} {:type :wall, :x 3, :y 1}]
                  [{:type :wall, :x 0, :y 2} {:type :floor, :x 1, :y 2} {:type :floor, :x 2, :y 2} {:type :wall, :x 3, :y 2}]
                  [{:type :wall, :x 0, :y 3} {:type :wall,  :x 1, :y 3} {:type :wall,  :x 2, :y 3} {:type :wall, :x 3, :y 3}]]
        session  (insert-all bomberman-session
                             [(->Board board)
                              (->TimestampNow (datetime/now!))
                              (->BombOnBoard     1 [1 1] 3 #inst "2021-08-28T15:03:47.100-00:00" #inst "2021-08-28T15:03:50.100-00:00")
                              (->BombOnBoard     1 [2 1] 3 #inst "2021-08-28T15:03:47.100-00:00" #inst "2021-08-28T15:03:47.100-00:00")])
        session' (fire-rules session)]
    (query session' fire-on-board?))

  ;; Fire burns out
  (let [board    [[{:type :wall, :x 0, :y 0} {:type :wall,  :x 1, :y 0} {:type :wall,  :x 2, :y 0} {:type :wall, :x 3, :y 0}]
                  [{:type :wall, :x 0, :y 1} {:type :floor, :x 1, :y 1} {:type :floor, :x 2, :y 1} {:type :wall, :x 3, :y 1}]
                  [{:type :wall, :x 0, :y 2} {:type :floor, :x 1, :y 2} {:type :floor, :x 2, :y 2} {:type :wall, :x 3, :y 2}]
                  [{:type :wall, :x 0, :y 3} {:type :wall,  :x 1, :y 3} {:type :wall,  :x 2, :y 3} {:type :wall, :x 3, :y 3}]]
        session  (insert-all bomberman-session
                             [(->Board board)
                              (->TimestampNow         #inst "2021-08-28T15:03:04.000-00:00")
                              (->FireOnBoard  1 [1 1] #inst "2021-08-28T15:03:02.000-00:00")
                              (->FireOnBoard  1 [1 2] #inst "2021-08-28T15:03:02.300-00:00")
                              (->FireOnBoard  1 [2 1] #inst "2021-08-28T15:03:03.000-00:00")])
        session' (fire-rules session)]
    (map :?fire-that-have-burned-out (query session' fire-that-have-burned-out?)))
  )

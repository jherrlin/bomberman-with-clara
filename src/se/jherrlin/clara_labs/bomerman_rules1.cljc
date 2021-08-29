(ns se.jherrlin.clara-labs.bomerman-rules1
  (:require [clara.rules :refer [insert insert-all fire-rules query insert! defrule defsession defquery]]
            [se.jherrlin.clara-labs.board :as board]
            [se.jherrlin.clara-labs.datetime :as datetime]))



(defrecord Board                [board])
(defrecord UserWantsToMove      [user-id board current-xy direction])
(defrecord UserMove             [user-id next-position])
(defrecord BombOnBoard          [user-id position-xy fire-length bomb-added-timestamp timestamp-now])
(defrecord BombExploading       [user-id position-xy])
(defrecord FireOnBoard          [user-id current-xy])
(defrecord UserWantsToPlaceBomb [user-id current-xy fire-length timestamp])
(defrecord AddBombToBoard       [user-id location-xy fire-length timestamp])
(defrecord AddFireToBoard       [user-id location])
(defrecord UserPositionOnBoard  [user-id current-xy])
(defrecord DeadUser             [user-id killed-by-user-id])


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

(defrule bomb-exploding
  "Bomb that is on board for 2000ms (2 seconds) should expoad"
  [Board (= ?board board)]
  [:or
   [BombOnBoard (= ?user-id user-id) (= ?position-xy position-xy) (= ?fire-length fire-length)
    (< 2000 (datetime/milliseconds-between bomb-added-timestamp timestamp-now))]
   [FireOnBoard (= ?user-id user-id) (= ?current-fire-xy current-xy)
    (= ?current-fire-xy ?position-xy)]]
  =>
  (apply insert! (mapv (fn [{:keys [x y]}] (->AddFireToBoard ?user-id [x y]))
                       (bomb-fire-spread ?board (or ?position-xy ?current-fire-xy) (or ?fire-length 1)))))

(< 2000 (datetime/milliseconds-between #inst "2021-08-28T15:03:47.100-00:00" #inst "2021-08-28T15:03:50.100-00:00"))
(datetime/now!)

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

(defquery dead-users?
  []
  [?dead-users <- DeadUser])

(defsession bomberman-session 'se.jherrlin.clara-labs.bomerman-rules1)

(let [board   (board/init 6)
      session (insert-all bomberman-session
                          [(->Board board)
                           (->UserWantsToMove 1 board [1 1] :north)
                           (->UserWantsToMove 2 board [1 1] :east)
                           (->UserWantsToMove 3 board [1 1] :south)
                           (->UserWantsToMove 4 board [1 1] :west)
                           (->UserWantsToMove 5 board [3 1] :west)
                           (->BombOnBoard     1       [2 1] 3 (datetime/now!) (datetime/now!))
                           (->UserWantsToPlaceBomb 10 [2 1] 3 (datetime/now!))
                           (->UserWantsToPlaceBomb 11 [1 1] 3 (datetime/now!))
                           (->BombOnBoard          66 [1 1] 3 #inst "2021-08-28T15:03:47.100-00:00" #inst "2021-08-28T15:03:50.100-00:00")

                           (->FireOnBoard 1 [5 4])
                           (->UserPositionOnBoard 0 [5 4])
                           ])
      session' (fire-rules session)]
  {:actions
   {:user-moves         (map :?user-move (query session' user-move?))
    :add-bombs-to-board (map :?add-bombs-to-board (query session' add-bombs-to-board?))
    :exploading-bombs   (map :?exploading-bombs (query session' exploading-bombs?))
    :add-fires-to-board (map :?add-fire-to-board (query session' fire-to-add?))
    :dead-users         (map :?dead-users (query session' dead-users?))
    }})


(comment
  ;; Users can only move on floor
  (let [board [[{:type :wall} {:type :wall}  {:type :wall}  {:type :wall}]
               [{:type :wall} {:type :floor} {:type :floor} {:type :wall}]
               [{:type :wall} {:type :wall}  {:type :wall}  {:type :wall}]]
        session (insert-all bomberman-session
                            [(->Board board)
                             (->UserWantsToMove 1 board [1 1] :east)
                             (->UserWantsToMove 2 board [1 1] :south)
                             (->UserWantsToMove 3 board [1 1] :west)
                             (->UserWantsToMove 4 board [1 1] :north)
                             (->UserWantsToMove 5 board [2 1] :east)
                             (->UserWantsToMove 6 board [2 1] :south)
                             (->UserWantsToMove 7 board [2 1] :west)
                             (->UserWantsToMove 8 board [2 1] :north)])
        session' (fire-rules session)]
    (query session' user-move?))
  )

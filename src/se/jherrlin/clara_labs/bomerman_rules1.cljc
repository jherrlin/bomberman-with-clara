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
   [FireOnBoard (= ?user-id user-id) (= ?current-xy current-xy)
    (= ?current-xy ?position-xy)]]
  =>
  (apply insert! (mapv (fn [{:keys [x y]}] (->AddFireToBoard ?user-id [x y]))
                       (bomb-fire-spread ?board (or ?position-xy ?current-xy) ?fire-length))))

(< 2000 (datetime/milliseconds-between #inst "2021-08-28T15:03:47.100-00:00" #inst "2021-08-28T15:03:50.100-00:00"))
(datetime/now!)

(defquery user-move?
  []
  [?user-move <- UserMove])

(defquery placed-bombs?
  []
  [?placed-bombs <- AddBombToBoard])

(defquery exploading-bombs?
  []
  [?exploading-bombs <- BombExploading])

(defquery fire-to-add?
  []
  [?fire-to-add <- AddFireToBoard])

(defsession bomberman-session 'se.jherrlin.clara-labs.bomerman-rules1)

(let [board    (board/init 6)
      session  (insert-all bomberman-session
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

                            ])
      session' (fire-rules session)]
  [(query session' user-move?)
   (query session' placed-bombs?)
   (query session' exploading-bombs?)
   (query session' fire-to-add?)])

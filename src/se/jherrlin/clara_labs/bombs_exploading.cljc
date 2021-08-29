(ns se.jherrlin.clara-labs.bombs-exploading
  (:require [clara.rules :refer [defquery defrule defsession fire-rules insert insert! insert-all insert-unconditional! query retract!]]
            [se.jherrlin.clara-labs.board :as board]
            [se.jherrlin.clara-labs.datetime :as datetime]))

(comment
  (remove-ns 'se.jherrlin.clara-labs.bombs-exploading)
  )


(defrecord Board                [board])
(defrecord BombOnBoard          [user-id position-xy fire-length bomb-added-timestamp timestamp-now])
(defrecord FireOnBoard          [user-id current-xy])

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
  [?bomb <- BombOnBoard (= ?user-id user-id) (= ?bomb-position-xy position-xy) (= ?fire-length fire-length)
   (< 2000 (datetime/milliseconds-between bomb-added-timestamp timestamp-now))]
  =>
  (apply insert-unconditional! (mapv (fn [{:keys [x y]}] (->FireOnBoard ?user-id [x y]))
                                     (bomb-fire-spread ?board ?bomb-position-xy ?fire-length)))
  (retract! ?bomb))

(defrule bomb-exploading-when-hit-by-fire
  "Bomb is exploading if it's hit by fire."
  [Board (= ?board board)]
  [?bomb <- BombOnBoard (= ?user-id user-id) (= ?bomb-position-xy position-xy) (= ?fire-length fire-length)]
  [FireOnBoard (= ?user-id user-id) (= ?current-fire-xy current-xy)]
  [:test (= ?bomb-position-xy ?current-fire-xy)]
  =>
  (retract! ?bomb)
  (apply insert-unconditional! (mapv (fn [{:keys [x y]}] (->FireOnBoard ?user-id [x y]))
                                     (bomb-fire-spread ?board ?bomb-position-xy ?fire-length))))

(defquery fire-on-board?
  []
  [?fire-on-board <- FireOnBoard])

(defquery bombs-on-board?
  []
  [?bombs-on-board <- BombOnBoard])


(defsession bomberman-session 'se.jherrlin.clara-labs.bombs-exploading)

(let [board    [[{:type :wall, :x 0, :y 0} {:type :wall,  :x 1, :y 0} {:type :wall,  :x 2, :y 0} {:type :wall, :x 3, :y 0}]
                [{:type :wall, :x 0, :y 1} {:type :floor, :x 1, :y 1} {:type :floor, :x 2, :y 1} {:type :wall, :x 3, :y 1}]
                [{:type :wall, :x 0, :y 2} {:type :floor, :x 1, :y 2} {:type :floor, :x 2, :y 2} {:type :wall, :x 3, :y 2}]
                [{:type :wall, :x 0, :y 3} {:type :wall,  :x 1, :y 3} {:type :wall,  :x 2, :y 3} {:type :wall, :x 3, :y 3}]]
      session  (insert-all bomberman-session
                           [(->Board board)
                            (->BombOnBoard     1 [1 1] 3 #inst "2021-08-28T15:03:47.100-00:00" #inst "2021-08-28T15:03:50.100-00:00")
                            (->BombOnBoard     1 [2 1] 3 #inst "2021-08-28T15:03:47.100-00:00" #inst "2021-08-28T15:03:47.100-00:00")
                            ])
      session' (fire-rules session)]
  [(query session' fire-on-board?)
   (query session' bombs-on-board?)]
  #_[(bomb-fire-spread board [1 1] 3)
   (bomb-fire-spread board [2 1] 3)]
  )

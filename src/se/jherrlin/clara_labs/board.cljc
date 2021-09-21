(ns se.jherrlin.clara-labs.board)

(defn only-walls [n]
  (vec (take n (repeat {:type :wall}))))

(defn cycle-walls [n]
  (vec (take n (cycle [{:type :wall} {:type :floor}]))))

(defn walls-on-each-side [n]
  (-> (- n 2)
      (take (repeat {:type :floor}))
      (conj {:type :wall})
      (concat [{:type :wall}])
      (vec)))

(defn add-index [xs y]
  (vec (map-indexed (fn [idx x] (assoc x :x idx :y y)) xs)))

(defn init [length]
  (let [length length]
    [(add-index (only-walls length) 0)
     (add-index (walls-on-each-side length) 1)
     (add-index (cycle-walls length) 2)
     (add-index (walls-on-each-side length) 3)
     (add-index (only-walls length) 4)]))

(def board2
  (let [length 19]
    [(add-index (only-walls length)          0)
     (add-index (walls-on-each-side length)  1)
     (add-index (cycle-walls length)         2)
     (add-index (walls-on-each-side length)  3)
     (add-index (cycle-walls length)         4)
     (add-index (walls-on-each-side length)  5)
     (add-index (cycle-walls length)         6)
     (add-index (walls-on-each-side length)  7)

     (add-index (cycle-walls length)         8)
     (add-index (walls-on-each-side length)  9)

     (add-index (only-walls length)         10)
     ]))

(def mini
  #_(let [length 3]
    [(add-index (only-walls length)          0)
     (add-index (walls-on-each-side length)  1)
     (add-index (only-walls length)          2)])
  [[{:type :wall, :x 0, :y 0} {:type :wall, :x 1, :y 0} {:type :wall, :x 2, :y 0}]
   [{:type :wall, :x 0, :y 1} {:type :floor, :x 1, :y 1}{:type :wall, :x 2, :y 1}]
   [{:type :wall, :x 0, :y 2} {:type :wall, :x 1, :y 2} {:type :wall, :x 2, :y 2}]])

(defn individual-state [[x y] board]
  (get-in board [y x]))

(defn neighbour-idxs [[x-index y-index]]
  [(vec (range (- x-index 1) (+ x-index 2)))
   (vec (range (- y-index 1) (+ y-index 2)))])

(defn neighbour-states
  [[current-x current-y] board [x-indexs y-indexs]]
  (vec (for [x x-indexs
             y y-indexs
             :when (not (= [current-x current-y] [x y]))]
         (individual-state [x y] board))))

(defn neighbours
  "Get neighbours state."
  [current-x-y board]
  (->> (neighbour-idxs   current-x-y)
       (neighbour-states current-x-y board)))

(defn nesw-neighbours
  "North, east, west, south neighbours"
  [neighbours]
  {:north (nth neighbours 3)
   :east  (nth neighbours 6)
   :south (nth neighbours 4)
   :west  (nth neighbours 1)})

(defn next-xy-position [[current-x current-y] direction]
  (case direction
    :north [     current-x  (dec current-y)]
    :east  [(inc current-x)      current-y]
    :south [     current-x  (inc current-y)]
    :west  [(dec current-x)      current-y]))

(defn next-xy-board-position [board [current-x current-y] direction]
  (let [board-length    (-> board first count)
        board-height    (-> board count)
        [next-x next-y] (next-xy-position [current-x current-y] direction)]
    [(cond
       (neg? next-x)           (- board-length (Math/abs next-x))
       (= board-length next-x) (mod next-x board-length)
       :else                   next-x)
     (cond
       (neg? next-y)           (- board-height (Math/abs next-y))
       (= board-height next-y) (mod next-y board-height)
       :else next-y)]))



(comment
  (next-xy-board-position (init 6) [0 1] :west)
  (next-xy-board-position board2 [0 1] :west)
  (next-xy-board-position board2 [1 0] :north)

  (next-xy-board-position board2 [3 9] :south)
  (next-xy-board-position board2 [3 10] :south)


  (next-xy-position [1 1] :north)
  (next-xy-position [1 1] :south)
  )

(defn target-position-type
  [board current-xy direction]
  (get-in
   (nesw-neighbours
    (neighbours
     current-xy
     board))
   [direction :type]))


(individual-state (next-xy-position [3 1] :west)
                  (init 6)
                  )

(comment
  (nesw-neighbours
   (neighbours
    [1 1]
    (init 6)))

  #?(:clj  (java.util.UUID/randomUUID)
     :cljs (random-uuid))

  (target-position-type
   (init 6)
   [1 1]
   :east)

  (target-position-type
   (init 6)
   [1 1]
   :south)
  )

(def reserved-floors
  #{[1 1]
    [1 2]
    [2 1]

    [1 8]
    [1 9]
    [2 9]

    [16 1]
    [17 1]
    [17 2]

    [17 8]
    [17 9]
    [16 9]})

(defn generate-stones [board reserved-positions]
  (->> (flatten board)
       (filter (comp #{:floor} :type))
       (map (fn [{:keys [x y]}]
              (let [n (rand-int 10)]
                (cond
                  (#{0 1 2 3} n)     nil
                  (#{4 5 6 7 8 9} n) [x y]))))
       (remove reserved-positions)
       (remove nil?)))

(defn generate-items [stones]
  (->> stones
       (map (fn [[x y]]
              (let [n (rand-int 10)]
                (cond
                  (#{0 1}             n) {:item-position-xy [x y] :item-power :inc-fire-length}
                  (#{2 3 4 5 6 7 8 9} n) nil))))
       (remove nil?)))

(comment
  (generate-items (generate-stones board2 reserved-floors))
  ;; board/board2
  ;; (generate-items stones)
  ;; (generate-stones board/board2 reserved-floors)
  )

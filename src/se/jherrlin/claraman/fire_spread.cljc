(ns se.jherrlin.claraman.fire-spread
  (:require [clojure.set :as set]
            [se.jherrlin.claraman.board :as board]))


(defn bomb-fire-spread-in-all-directions [[pos-x pos-y] fire-length]
  (let [fire-length' (inc fire-length)]
    {:north (->> (map (fn [y] [pos-x y]) (range (inc (- pos-y fire-length')) pos-y))
                 (sort-by second #(compare %2 %1))
                 (vec))
     :west  (->> (map (fn [x] [x pos-y]) (range (inc (- pos-x fire-length')) pos-x))
                 (sort-by first #(compare %2 %1))
                 (vec))
     :east  (->> (map (fn [x] [x pos-y]) (range (inc pos-x) (+ pos-x fire-length')))
                 (sort-by first #(compare %1 %2))
                 (vec))
     :south (->> (map (fn [y] [pos-x y]) (range (inc pos-y) (+ pos-y fire-length')))
                 (sort-by second #(compare %1 %2))
                 (vec))}))

(defn first-stones-hit-by-fire-in-direction [fire-spread stones]
  (let [stones-set                      (set stones)
        {:keys [north west east south]} fire-spread
        first-stone-to-hit-in-direction (fn [vs] (->> vs (filter #(set/subset? #{%} stones-set)) first))]
    {:north (-> north first-stone-to-hit-in-direction)
     :west  (-> west  first-stone-to-hit-in-direction)
     :east  (-> east  first-stone-to-hit-in-direction)
     :south (-> south first-stone-to-hit-in-direction)}))

(defn remove-fire-after-is-hits-a-wall [fire-spread board]
  (let [{:keys [north west east south]} fire-spread
        first-stone-to-hit-in-direction (fn [vs] (->> vs
                                                      (map #(board/individual-state % board))
                                                      (take-while (comp #{:floor} :type))
                                                      (map (fn [{:keys [x y]}] [x y]))
                                                      (vec)))]
    {:north (-> north first-stone-to-hit-in-direction)
     :west  (-> west  first-stone-to-hit-in-direction)
     :east  (-> east  first-stone-to-hit-in-direction)
     :south (-> south first-stone-to-hit-in-direction)}))

(defn until-fire-flame-hits-a-stone [fire-xys stones]
  (let [stones-set (set stones)]
    (loop [[this & rest] fire-xys
           save          []
           stone-found?  false]
      (cond
        (nil? this)                      save
        stone-found?                     save
        ;; If the fire hits a stone, save that fire but discard the rest
        (set/subset? #{this} stones-set) (recur rest (conj save this) true)
        :else                            (recur rest (conj save this) false)))))

(defn remove-fire-after-is-hits-the-first-stone [fire-spread stones]
  (let [{:keys [north west east south]} fire-spread]
    {:north (until-fire-flame-hits-a-stone north stones)
     :west  (until-fire-flame-hits-a-stone west  stones)
     :east  (until-fire-flame-hits-a-stone east  stones)
     :south (until-fire-flame-hits-a-stone south stones)}))

(defn fire-after-it-hit-objects [bomb-xy fire-length board stones]
  (let [stones' (map :stone-position-xy stones)]
    (->> (-> (bomb-fire-spread-in-all-directions bomb-xy fire-length)
             (remove-fire-after-is-hits-a-wall board)
             (remove-fire-after-is-hits-the-first-stone stones'))
         (vals)
         (remove empty?)
         (apply concat)
         (concat [bomb-xy]))))

(comment
  (fire-after-it-hit-objects
   [3 1] 3 (board/init 6) [])

  (let [bomb-xy     [1 1]
        fire-length 10
        board       (board/init 6)
        stones      #_[] [[1 2] [2 1] [3 1]]]
    (->> (-> (bomb-fire-spread-in-all-directions bomb-xy fire-length)
             (remove-fire-after-is-hits-a-wall board)
             (remove-fire-after-is-hits-the-first-stone stones))
         (vals)
         (remove empty?)
         (apply concat)))

  {:north [], :west [], :east [[2 1] [3 1] [4 1]], :south [[1 2] [1 3]]}

  (-> (bomb-fire-spread-in-all-directions [2 2] 2))

  (-> (bomb-fire-spread-in-all-directions [2 2] 2)
      (first-stones-hit-by-fire-in-direction [[2 0] [3 2] [2 3] [2 4] [2 5]]))

  (first-stones-hit-by-fire-in-direction
   {:north [[2 1] [2 0]]
    :west  [[1 2] [0 2]]
    :east  [[3 2] [4 2]]
    :south [[2 3] [2 4]]}
   [[2 0]
    [3 2]
    [2 3]
    [2 4]
    [2 5]])
  )

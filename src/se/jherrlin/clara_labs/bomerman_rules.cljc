(ns se.jherrlin.clara-labs.bomerman-rules
  (:require [clara.rules :refer [insert insert-all fire-rules query insert! defrule defsession defquery]]))



(defn only-walls [n]
  (vec (take n (repeat {:type :wall}))))

(defn cycle-walls [n]
  (vec (take n (cycle [{:type :wall} {:type :free}]))))

(defn walls-on-each-side [n]
  (-> (- n 2)
      (take (repeat {:type :free}))
      (conj {:type :wall})
      (concat [{:type :wall}])
      (vec)))

(defn init-generation [n]
  (let [length n]
    [(only-walls length)
     (walls-on-each-side length)
     (cycle-walls length)
     (walls-on-each-side length)
     (only-walls length)]))

(defn individual-state [[x y] generation]
  (get-in generation [y x]))

(defn neighbour-idxs [[^int x-index ^int y-index]]
  [(vec (range (- x-index 1) (+ x-index 2)))
   (vec (range (- y-index 1) (+ y-index 2)))])

(defn neighbour-states
  [[current-x current-y] generation [x-indexs y-indexs]]
  (vec (for [x x-indexs
             y y-indexs
             :when (not (= [current-x current-y] [x y]))]
         (individual-state [x y] generation))))

(defn neighbours
  "Get neighbours state."
  [current-x-y generation]
  (->> (neighbour-idxs   current-x-y)
       (neighbour-states current-x-y generation)))

(defn nesw-neighbours
  "North, east, west, south neighbours"
  [neighbours]
  {:north (nth neighbours 1)
   :east  (nth neighbours 4)
   :south (nth neighbours 6)
   :west  (nth neighbours 3)})

(comment
  (nesw-neighbours
   (neighbours
    [1 1]
    (init-generation 6)))

  #?(:clj  (java.util.UUID/randomUUID)
     :cljs (random-uuid))


  )

(defn bomb-placed-in-position? [generation [pos-x pos-y]]
  (let [position (get-in generation [pos-y pos-x])]
    (-> position :bomb boolean)))

(defn place-bomb-in-position [generation [pos-x pos-y] user-id]
  (update-in generation [pos-y pos-x] assoc :bomb user-id))

(defn remove-bomb-from-position [generation [pos-x pos-y]]
  (update-in generation [pos-y pos-x] dissoc :bomb))

(defn target-position-type
  [generation current-xy direction]
  (get-in
   (nesw-neighbours
    (neighbours
     current-xy
     generation))
   [direction :type]))

(defn next-xy-position [[current-x current-y] direction]
  (case direction
    :north [     current-x  (inc current-y)]
    :east  [(inc current-x)      current-y]
    :south [     current-x  (dec current-y)]
    :west  [(dec current-x)      current-y]))


(defrecord UserWantsToMove [user-id generation current-xy direction])
(defrecord UserMove        [user-id next-position])
(defrecord PlacedBomb      [user-id position-xy])

{:board
 :players
 :bombs
 :fire}


(defrule user-move
  "User move"
  [UserWantsToMove (= ?user-id user-id) (= ?current-xy current-xy) (= ?direction direction)
   (not (#{:wall} (target-position-type generation current-xy direction)))]
  [:not [PlacedBomb (= ?current-xy current-xy)]]
  =>
  (insert! (->UserMove ?user-id (next-xy-position ?current-xy ?direction))))

(defquery user-move?
  []
  [?user-move <- UserMove])

(defrecord UserWantsToPlaceBomb [user-id generation current-xy fire-length])
(defrecord UserPlaceBomb        [user-id generation current-xy fire-length timestamp])
(defrecord Fire                 [user-id generation current-xy])

(comment
  (-> (place-bomb-in-position (init-generation 6) [1 1] 1)
      (remove-bomb-from-position [1 1]))

  (-> (place-bomb-in-position (init-generation 6) [1 1] 1)
      (bomb-placed-in-position? [1 1]))
  )

(defn now! []
  #?(:clj  (java.util.Date.)
     :cljs (js/Date.)))

(defrule place-bomb
  "User place bomb in current location"
  [UserWantsToPlaceBomb (= ?user-id user-id) (= ?fire-length fire-length) (= ?current-xy current-xy) (= ?generation generation)
   (not (bomb-placed-in-position? generation current-xy))]
  [PlacedBomb]
  =>
  (insert! (->UserPlaceBomb ?user-id ?generation ?current-xy ?fire-length (now!))))

(defn inst->local-datetime
     "Convert `#inst` to `java.time.LocalDateTime`."
     [inst]
     (.toLocalDateTime
      (.atZone
       (.toInstant inst)
       (java.time.ZoneId/systemDefault))))

(defn milliseconds-between
  "Count milliseconds between two `#inst`."
  [inst1 inst2]
  (.toMillis
   (java.time.Duration/between
    (inst->local-datetime inst1)
    (inst->local-datetime inst2))))

(defrule bomb-exploation
  ""
  [UserPlaceBomb (= ?user-id user-id) (= ?current-xy current-xy) (= ?generation generation)
   (< (milliseconds-between timestamp (now!)) 3000)]
  =>
  (insert! (->Fire ?user-id ?generation ?current-xy)))

(comment
  (milliseconds-between
   #inst "2021-08-28T12:35:13.726-00:00"
   #inst "2021-08-28T12:35:16.726-00:00")
  )

;; =>

(defquery placed-bombs
  []
  [?placed-bombs <- UserPlaceBomb])

;; (defrule
;;   "Remove bombs after a certain amount of time"
;;   []
;;   )




(defsession bomberman-session 'se.jherrlin.clara-labs.bomerman-rules)

(let [generation (init-generation 6)
      session (insert-all bomberman-session [(->UserWantsToMove 1 generation [1 1] :north)
                                             (->UserWantsToMove 2 generation [1 1] :east)
                                             (->UserWantsToMove 3 generation [1 1] :south)
                                             (->UserWantsToMove 4 generation [1 1] :west)
                                             (->UserWantsToPlaceBomb 1 generation [1 1] 3)])
      session' (fire-rules session)]
  [(query session' user-move?)
   (query session' placed-bombs)
   ]
  )

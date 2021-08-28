(ns se.jherrlin.clara-labs.bomerman-rules
  (:require [clara.rules :refer [insert insert-all fire-rules query insert! defrule defsession defquery]]))



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
  [:not [PlacedBomb (= ?position-xy position-xy)]]
  =>
  (insert! (->UserMove ?user-id (next-xy-position ?current-xy ?direction))))




(comment
  (-> (place-bomb-in-position (init-generation 6) [1 1] 1)
      (remove-bomb-from-position [1 1]))

  (-> (place-bomb-in-position (init-generation 6) [1 1] 1)
      (bomb-placed-in-position? [1 1]))
  )






;; (defrule bomb-exploation
;;   ""
;;   [UserPlaceBomb (= ?user-id user-id) (= ?current-xy current-xy) (= ?generation generation)
;;    (< (milliseconds-between timestamp (now!)) 3000)]
;;   =>
;;   (insert! (->Fire ?user-id ?generation ?current-xy)))

(comment
  (milliseconds-between
   #inst "2021-08-28T12:35:13.726-00:00"
   #inst "2021-08-28T12:35:16.726-00:00")
  )

;; =>



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

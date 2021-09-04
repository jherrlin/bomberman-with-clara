(ns se.jherrlin.clara-labs.bomberman-rules
  (:require [clara.rules :refer [defquery defrule defsession fire-rules insert insert! insert-all insert-unconditional! query retract!]]
            [clara.rules.accumulators :as acc]
            [se.jherrlin.clara-labs.board :as board]
            [se.jherrlin.clara-labs.datetime :as datetime]
            [clojure.set :as set]))

(comment
  (remove-ns 'se.jherrlin.clara-labs.bomberman-rules)
  )



(defrecord Board                [board])
(defrecord TimestampNow         [now])
(defrecord UserWantsToMove      [user-id current-xy direction])
(defrecord UserMove             [user-id next-position])
(defrecord BombOnBoard          [user-id position-xy fire-length bomb-added-timestamp])
(defrecord BombExploading       [user-id position-xy fire-length])
(defrecord FireOnBoard          [user-id current-xy fire-start-timestamp])
(defrecord RemoveFireFromBoard  [current-xy])
(defrecord UserWantsToPlaceBomb [user-id current-xy fire-length timestamp])
(defrecord AddBombToBoard       [user-id location-xy fire-length timestamp])
(defrecord AddFireToBoard       [user-id location])
(defrecord UserPositionOnBoard  [user-id current-xy])
(defrecord DeadUser             [user-id killed-by-user-id])
(defrecord Stone                [position-xy]) ;; Object on the board that can be removed by fire
(defrecord StoneToRemove        [position-xy])

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

(comment
  (let [bomb-xy     [1 1]
        fire-length 10
        board       (board/init 6)
        stones      #_[] [[1 2] [2 1] [3 1]]
        ]
    (->> (-> (bomb-fire-spread-in-all-directions bomb-xy fire-length)
             (remove-fire-after-is-hits-a-wall board)
             (remove-fire-after-is-hits-the-first-stone stones))
         (vals)
         (remove empty?)
         (apply concat)
         ))

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

(defn remove-fires-that-meet-obstacles [bomb-xy fire-length board stones]
  (let [stones' (map :position-xy stones)]
    (->> (-> (bomb-fire-spread-in-all-directions bomb-xy fire-length)
             (remove-fire-after-is-hits-a-wall board)
             (remove-fire-after-is-hits-the-first-stone stones'))
         (vals)
         (remove empty?)
         (apply concat))))

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

(defrule user-dies
  "User dies if she gets hit by fire."
  [UserPositionOnBoard (= ?user-id user-id)      (= ?user-current-xy current-xy)]
  [FireOnBoard         (= ?fire-user-id user-id) (= ?fire-current-xy current-xy)
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
                            (remove-fires-that-meet-obstacles ?bomb-position-xy ?fire-length ?board ?stones))]
    (apply insert! fire-on-board)))

(defrule bomb-exploding-after-timeout
  "Bomb that is on board for 2000ms (2 seconds) should expload."
  [Board (= ?board board)]
  [TimestampNow (= ?now now)]
  [?bomb <- BombOnBoard (= ?user-id user-id) (= ?bomb-added-timestamp bomb-added-timestamp) (= ?bomb-position-xy position-xy) (= ?fire-length fire-length)]
  [:test (< 2000 (datetime/milliseconds-between ?bomb-added-timestamp ?now))]
  =>
  (retract! ?bomb)
  (insert-unconditional! (->BombExploading ?user-id ?bomb-position-xy ?fire-length)))

(defrule remove-stones-hit-by-fire
  "If a stone is hit by fire, remove it."
  [FireOnBoard (= ?fire-position-xy current-xy)]
  [Stone       (= ?stone-position-xy position-xy)]
  [:test (= ?fire-position-xy ?stone-position-xy)]
  =>
  (insert! (->StoneToRemove ?stone-position-xy)))

(defrule bomb-exploading-when-hit-by-fire
  "Bomb is exploading if it's hit by fire."
  [Board (= ?board board)]
  [TimestampNow (= ?now now)]
  [?bomb <- BombOnBoard (= ?user-id user-id) (= ?bomb-position-xy position-xy) (= ?fire-length fire-length)]
  [FireOnBoard (= ?user-id user-id) (= ?current-fire-xy current-xy)]
  [:test (= ?bomb-position-xy ?current-fire-xy)]
  =>
  (retract! ?bomb)
  (insert-unconditional! (->BombExploading ?user-id ?bomb-position-xy ?fire-length)))

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

(defquery stones-to-remove?
  []
  [?stones-to-remove <- StoneToRemove])

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

(defsession bomberman-session 'se.jherrlin.clara-labs.bomberman-rules)

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

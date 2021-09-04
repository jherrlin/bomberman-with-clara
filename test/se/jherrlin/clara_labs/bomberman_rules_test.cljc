(ns se.jherrlin.clara-labs.bomberman-rules-test
  (:require
   [clara.rules :refer [defquery defrule defsession fire-rules insert insert! insert-all insert-unconditional! query retract!]]
   [clara.rules.accumulators :as acc]
   [se.jherrlin.clara-labs.bomberman-rules :as bomberman]
   [se.jherrlin.clara-labs.datetime :as datetime]
   #?(:clj [clojure.test :as t]
      :cljs [cljs.test :as t :include-macros true])))

(comment
  (remove-ns 'se.jherrlin.clara-labs.bomberman-rules-test)
  )


(def board
  [[{:type :wall :x 0 :y 0} {:type :wall  :x 1 :y 0} {:type :wall  :x 2 :y 0} {:type :wall  :x 3 :y 0} {:type :wall  :x 4 :y 0} {:type :wall  :x 5 :y 0}]
   [{:type :wall :x 0 :y 1} {:type :floor :x 1 :y 1} {:type :floor :x 2 :y 1} {:type :floor :x 3 :y 1} {:type :floor :x 4 :y 1} {:type :wall  :x 5 :y 1}]
   [{:type :wall :x 0 :y 2} {:type :floor :x 1 :y 2} {:type :wall  :x 2 :y 2} {:type :floor :x 3 :y 2} {:type :wall  :x 4 :y 2} {:type :floor :x 5 :y 2}]
   [{:type :wall :x 0 :y 3} {:type :floor :x 1 :y 3} {:type :floor :x 2 :y 3} {:type :floor :x 3 :y 3} {:type :floor :x 4 :y 3} {:type :wall  :x 5 :y 3}]
   [{:type :wall :x 0 :y 4} {:type :wall  :x 1 :y 4} {:type :wall  :x 2 :y 4} {:type :wall  :x 3 :y 4} {:type :wall  :x 4 :y 4} {:type :wall  :x 5 :y 4}]])

(defsession bomberman-session 'se.jherrlin.clara-labs.bomberman-rules)

(t/deftest user-wants-to-move
  (t/is
   (=
    (let [session  (insert-all bomberman-session
                               [(bomberman/->Board board)
                                (bomberman/->UserWantsToMove 1 [1 1] :east)
                                (bomberman/->UserWantsToMove 2 [1 1] :south)
                                (bomberman/->UserWantsToMove 3 [1 1] :west)
                                (bomberman/->UserWantsToMove 4 [1 1] :north)
                                (bomberman/->UserWantsToMove 5 [2 1] :east)
                                (bomberman/->UserWantsToMove 6 [2 1] :south)
                                (bomberman/->UserWantsToMove 7 [2 1] :west)
                                (bomberman/->UserWantsToMove 8 [2 1] :north)])
          session' (fire-rules session)]
      (->> (query session' bomberman/user-move?)
           (map (comp #(into {} %) :?user-move))
           (set)))
    #{{:user-id 7, :next-position [1 1]}
      {:user-id 1, :next-position [2 1]}
      {:user-id 5, :next-position [3 1]}
      {:user-id 2, :next-position [1 0]}})))

(t/deftest user-cant-walk-on-bomb
  (t/testing "User cant walk west as there is a bomb there"
    (t/is
     (=
      (let [session  (insert-all bomberman-session
                                 [(bomberman/->Board board)
                                  (bomberman/->TimestampNow  (datetime/now!))
                                  (bomberman/->UserWantsToMove 1 [1 1] :east)
                                  (bomberman/->UserWantsToMove 2 [2 1] :west)
                                  (bomberman/->BombOnBoard     2 [2 1] 3 (datetime/now!))])
            session' (fire-rules session)]
        (->> (query session' bomberman/user-move?)
             (map (comp #(into {} %) :?user-move))
             (set)))
      #{{:user-id 2, :next-position [1 1]}}))))

(t/deftest exploading-bombs-create-fire-and-fire-destroys-stone
  (t/is
   (=
    (let [session  (insert-all bomberman-session
                               [(bomberman/->Board board)
                                (bomberman/->TimestampNow              #inst "2021-08-28T15:03:50.100-00:00")
                                (bomberman/->BombOnBoard     1 [1 1] 4 #inst "2021-08-28T15:03:47.100-00:00")
                                (bomberman/->BombOnBoard     1 [1 3] 4 #inst "2021-08-28T15:03:49.100-00:00")
                                (bomberman/->Stone             [3 1])
                                (bomberman/->Stone             [3 3])])
          session' (fire-rules session)]
      {:fire-on-board (->> (query session' bomberman/fire-on-board?)
                           (map (comp #(into {} %) :?fire-on-board))
                           (set))
       :exploading-bombs (->> (query session' bomberman/exploading-bombs?)
                              (map (comp #(into {} %) :?exploading-bombs))
                              (set))
       :stones-to-remove (->> (query session' bomberman/stones-to-remove?)
                              (map (comp #(into {} %) :?stones-to-remove))
                              (set))})
    {:fire-on-board    #{{:user-id              1,
                          :current-xy           [1 3],
                          :fire-start-timestamp #inst "2021-08-28T15:03:50.100-00:00"}
                         {:user-id              1,
                          :current-xy           [1 2],
                          :fire-start-timestamp #inst "2021-08-28T15:03:50.100-00:00"}
                         {:user-id              1,
                          :current-xy           [2 3],
                          :fire-start-timestamp #inst "2021-08-28T15:03:50.100-00:00"}
                         {:user-id              1,
                          :current-xy           [2 1],
                          :fire-start-timestamp #inst "2021-08-28T15:03:50.100-00:00"}
                         {:user-id              1,
                          :current-xy           [1 1],
                          :fire-start-timestamp #inst "2021-08-28T15:03:50.100-00:00"}
                         {:user-id              1,
                          :current-xy           [3 3],
                          :fire-start-timestamp #inst "2021-08-28T15:03:50.100-00:00"}
                         {:user-id              1,
                          :current-xy           [3 1],
                          :fire-start-timestamp #inst "2021-08-28T15:03:50.100-00:00"}},
     :exploading-bombs #{{:user-id 1, :position-xy [1 1], :fire-length 4}
                         {:user-id 1, :position-xy [1 3], :fire-length 4}},
     :stones-to-remove #{{:position-xy [3 1]} {:position-xy [3 3]}}})))

(t/deftest fire-burns-out
  (t/is
   (=
    (let [session  (insert-all bomberman-session
                               [(bomberman/->Board board)
                                (bomberman/->TimestampNow         #inst "2021-08-28T15:03:04.000-00:00")
                                (bomberman/->FireOnBoard  1 [1 1] #inst "2021-08-28T15:03:02.000-00:00")
                                (bomberman/->FireOnBoard  1 [1 2] #inst "2021-08-28T15:03:02.300-00:00")
                                (bomberman/->FireOnBoard  1 [2 1] #inst "2021-08-28T15:03:03.000-00:00")])
          session' (fire-rules session)]
      (->> (query session' bomberman/fire-that-have-burned-out?)
           (map (comp #(into {} %) :current-xy :?fire-that-have-burned-out ))
           (set)))
    #{{:user-id 1,
       :current-xy [1 2],
       :fire-start-timestamp #inst "2021-08-28T15:03:02.300-00:00"}
      {:user-id 1,
       :current-xy [1 1],
       :fire-start-timestamp #inst "2021-08-28T15:03:02.000-00:00"}})))

(t/deftest user-dies-if-hit-by-fire
  (t/is
   (=
    (let [session  (insert-all bomberman-session
                               [(bomberman/->Board board)
                                (bomberman/->TimestampNow                #inst "2021-08-28T15:03:04.000-00:00")
                                (bomberman/->FireOnBoard         2 [1 1] #inst "2021-08-28T15:03:02.000-00:00")
                                (bomberman/->UserPositionOnBoard 1 [1 1])])
          session' (fire-rules session)]
      (->> (query session' bomberman/dead-users?)
           (map (comp #(into {} %) :?dead-users))
           (set)))
    #{{:user-id 1, :killed-by-user-id 2}})))

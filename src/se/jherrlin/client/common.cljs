(ns se.jherrlin.client.common
  (:require [reitit.coercion.schema]
            [se.jherrlin.datetime :as datetime]
            [cljs.pprint :as pprint]
            [se.jherrlin.claraman.game-state :as game-state]
            [se.jherrlin.client.events :as client.events]))


(defn screen [screen']
  [:<>
   (for [[i row]  (map-indexed list screen')
         [j cell] (map-indexed list row)]
     ^{:key (str i j cell)}
     (let [t (:type cell)]
       [:<>
        [:div {:style {:width       "30px"
                       :color       (:color cell)
                       :height      "30px"
                       :display     "inline-block"
                       :font-size   "1.7em"
                       :line-height "1.7em"}}
         (:str cell)]
        (when (= (-> screen' first count dec) j)
          [:div {:style {:display "block"}}])]))])

(defn events->screen
  ([events]
   (events->screen events 1))
  ([events nr]
   (->> (take nr events)
        (game-state/the-projection {})
        :games
        (vals)
        (first)
        (client.events/game-state->screen))))

(defn trunc
  [s n]
  (subs s 0 (min (count s) n)))

(defn print-events-table
  "Print event table with newest events on top."
  [events]
  (->> events
       (sort-by :time #(compare %2 %1))
       (reverse)
       (map-indexed vector)
       (reverse)
       (map (fn [[idx {:keys [data time] :as event}]]
              (-> event
                  (assoc :nr idx)
                  (assoc :time (datetime/datetime-format time "yyyy-MM-dd HH:mm:ss"))
                  (dissoc :id :content-type :subject)
                  (assoc :data (trunc (str (dissoc data :game-id :bomb-added-timestamp :timestamp)) 100)))))
       (pprint/print-table)))

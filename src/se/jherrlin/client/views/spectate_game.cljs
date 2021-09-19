(ns se.jherrlin.client.views.spectate-game
  (:require [reitit.coercion.schema]
            [re-frame.core :as re-frame]
            [se.jherrlin.datetime :as datetime]
            [cljs.pprint :as pprint]
            [se.jherrlin.server.game-state :as game-state]
            [se.jherrlin.client.events :as client.events]
            ["semantic-ui-react" :as semantic-ui]))


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

(def events
  [{:n ::events}
   {:n ::time-travel-location
    :s (fn [db [k]] (get db k 1))}])

(re-frame/reg-sub
 ::screen
 :<- [::events]
 :<- [::time-travel-location]
 (fn [[events position-in-time] _]
   (events->screen events position-in-time)))

(doseq [{:keys [n s e]} events]
  (re-frame/reg-sub n (or s (fn [db _] (n db))))
  (re-frame/reg-event-db n (or e (fn [db [_ e]] (assoc db n e)))))

(defn view []
  (let [listen-to-game-id    @(re-frame/subscribe [:listen-to-game-id])
        time-travel-location @(re-frame/subscribe [::time-travel-location])
        events               @(re-frame/subscribe [::events])
        screen               @(re-frame/subscribe [::screen])]
    (when events
      [:<>
       [:div {:style {:text-align "center"}}
        [:h2 "Look back at the game"]]

       [:div {:style {:text-align "center"}}
        (for [[i row]  (map-indexed list screen)
              [j cell] (map-indexed list row)]
          ^{:key (str i j cell)}
          (let [t (:type cell)]
            [:<>
             [:div {:style {:width       "30px"
                            :height      "30px"
                            :display     "inline-block"
                            :font-size   "1.7em"
                            :line-height "1.7em"}}
              (:str cell)]
             (when (= (-> screen first count dec) j)
               [:div {:style {:display "block"}}])]))]
       [:br]
       [:div {:style {:display         "flex"
                      :align-items     "center"
                      :justify-content "center"}}
        [:input
         {:style     {:width "100%"}
          :type      "range"
          :min       1
          :max       (dec (count events))
          :step      1
          :value     time-travel-location
          :on-change #(do
                        (let [nr (js/parseInt (.. % -target -value))]
                          (re-frame/dispatch [::time-travel-location nr])))}]]
       [:hr]
       [:p "Here is a table of the events (some data have been removed to make it easier to read)."]
       [:div
        [:pre {:style {:font-size   "0.7em"
                       :line-height "1.2em"}}
         (with-out-str (print-events-table events))]]])))

(re-frame/reg-event-fx
 ::get-game-events
 (fn [_ [_ game-id]]
   (def game-id' game-id)
   {:ws-send {:data       [:game/events {:game-id game-id}]
              :on-success (fn [data]
                            (def data data)
                            (re-frame/dispatch [::events data]))}}))

(defn routes []
  [["/game/spectate/:game-id"
    {:name        ::view
     :view        [#'view]
     :controllers [{:parameters {:path [:game-id]}
                    :start
                    (fn [{{:keys [game-id]} :path :as a}]
                      (println "heading to /game/spectate/:game-id")
                      (def a a)
                      (let [game-id (uuid game-id)]
                        (def game-id game-id)
                        (re-frame/dispatch [::get-game-events game-id])))
                    :stop
                    (fn [_]
                      (println "leaving" ::view))}]}]])

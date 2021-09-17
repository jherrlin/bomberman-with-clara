(ns se.jherrlin.client.views.spectate-game
  (:require [reitit.coercion.schema]
            [re-frame.core :as re-frame]
            [se.jherrlin.server.game-state :as game-state]
            [se.jherrlin.client.events :as client.events]
            ["semantic-ui-react" :as semantic-ui]))

(def events
  [{:n ::events}
   {:n ::time-travel-location}
   {:n ::screen
    :s (fn [db [k]]
         (let [gs (get db ::screen)]
           (def gs gs)
           (-> gs :games (vals) (first) (client.events/game-state->screen))))}])

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
       [:div
        [:h3 "Time travel"]
        [:input
         {:type      "range"
          :min       1
          :max       (dec (count events))
          :step      1
          :value     time-travel-location
          :on-change #(do
                        (let [nr (js/parseInt (.. % -target -value))]
                          (re-frame/dispatch [::time-travel-location nr])
                          (re-frame/dispatch [::screen (game-state/the-projection {} (take nr events))])))}]
        [:div
         (for [[i row]  (map-indexed list screen)
               [j cell] (map-indexed list row)]
           ^{:key (str i j cell)}
           (let [t (:type cell)]
             [:<>
              [:div {:style {:width   "20px"
                             :height  "20px"
                             :display "inline-block"}}
               (:str cell)]
              (when (= (-> screen first count dec) j)
                [:div {:style {:display "block"}}])]))]]])))

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

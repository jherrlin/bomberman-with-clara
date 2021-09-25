(ns se.jherrlin.client.views.spectate-game
  (:require [reitit.coercion.schema]
            [re-frame.core :as re-frame]
            [se.jherrlin.datetime :as datetime]
            [se.jherrlin.client.common :as client.common]
            [cljs.pprint :as pprint]
            [se.jherrlin.server.game-state :as game-state]
            [se.jherrlin.client.events :as client.events]
            ["semantic-ui-react" :as semantic-ui]))


(defn download-csv [value export-name]
  (let [data-blob (js/Blob. #js [value] #js {:type "application/edn"})
        link (.createElement js/document "a")]
    (set! (.-href link) (.createObjectURL js/URL data-blob))
    (.setAttribute link "download" export-name)
    (.appendChild (.-body js/document) link)
    (.click link)
    (.removeChild (.-body js/document) link)))

(re-frame/reg-event-fx
 ::download-game-events
 (fn [_ [_ events]]
   (def events)
   (download-csv events "game-events.edn")
   {}))

(def events-
  [{:n ::events}
   {:n ::time-travel-location
    :s (fn [db [k]] (get db k 1))}])

(re-frame/reg-sub
 ::screen
 :<- [::events]
 :<- [::time-travel-location]
 (fn [[events position-in-time] _]
   (client.common/events->screen events position-in-time)))

(doseq [{:keys [n s e]} events-]
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
        [:h2 "Look back at the game."]
        [:h3 "Travel in time by dragging the scroll bar below game board."]
        [:div
         [:> semantic-ui/Button
          {:label   "Download game events"
           :icon    "download"
           :onClick #(re-frame/dispatch
                      [::download-game-events (with-out-str (pprint/pprint events))])}]
         [:p "If you wanna inspect the game events event further you can
         download all events."]]]

       [:div {:style {:text-align "center"}}
        [client.common/screen screen]]
       [:div "Timeline -->"]
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
       [:div (str "Game projection up to event nr: " time-travel-location)]
       [:hr]
       [:p "Here is a table of the events (some data have been removed to make it easier to read)."]
       [:div
        [:pre {:style {:font-size   "0.7em"
                       :line-height "1.2em"}}
         (with-out-str (client.common/print-events-table events))]]])))

(re-frame/reg-event-fx
 ::get-game-events
 (fn [_ [_ game-id]]
   (re-frame/dispatch [:http-get
                       {:url        (str "/game-events/" game-id)
                        :on-success [::events]}])))

(defn routes []
  [["/game/spectate/:game-id"
    {:name        ::view
     :view        [#'view]
     :controllers [{:parameters {:path [:game-id]}
                    :start
                    (fn [{{:keys [game-id]} :path}]
                      (let [game-id (uuid game-id)]
                        (re-frame/dispatch [::get-game-events game-id])))
                    :stop
                    (fn [_]
                      (re-frame/dispatch [::time-travel-location 1]))}]}]])

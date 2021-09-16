(ns se.jherrlin.client.main
  (:require [re-frame.core :as re-frame]
            [reagent.dom :as rd]
            [reitit.core :as r]
            [reitit.frontend.easy :as rfe]
            [se.jherrlin.client.router :as client.router]
            [se.jherrlin.client.events :as events]
            [reitit.frontend.easy :as rfe]
            ["semantic-ui-react" :as semantic-ui]))


(defn header-menu [router]
  (let []
    [:div {:style {:flex "1"}}
     [:> semantic-ui/Menu {:size       "large"
                           :borderless true}
      [:> semantic-ui/Menu.Item
       {:as "a" :href (rfe/href :route1/home)} "Home"]
      [:> semantic-ui/Menu.Item
       {:as "a" :href (rfe/href :route2/db)} "DB"]

      [:> semantic-ui/Menu.Menu {:position "right" }
       [:> semantic-ui/Menu.Item
        [:img {:src "img/bomberman-logo.png" :style {:height "2em"}}]]]]]))

(defn main-component [{:keys [router]}]
  (let [current-route @(re-frame/subscribe [:current-route])]
    (def router router)
    [:> semantic-ui/Container
     {:style {:height         "100%"
              :display        "flex"
              :flex-direction "column"}}

     ;; Header
     [header-menu router]

     ;; Submenu
     [:div {:style {:flex "1" :padding-top "0.5em" :flex-direction "column"}}]

     ;; Main body
     [:div {:style {:height      "100%"
                    :overflow-y  "auto"
                    :overflow-x  "hidden"
                    :padding-top "0.5em"}}
      (when current-route
        (-> current-route :data :view))]

     #_[:div
      [:> semantic-ui/Menu
       [:> semantic-ui/Menu.Item {:name "Notifikationer"}]
       [:> semantic-ui/Menu.Item {:name "Något"}]]]]))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (rd/render [main-component {:router (client.router/handler)}] (.getElementById js/document "app")))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (client.router/init)
  (mount-root))

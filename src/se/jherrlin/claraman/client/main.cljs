(ns se.jherrlin.claraman.client.main
  (:require
   [se.jherrlin.claraman.client.websocket :as websocket]
   [re-frame.core :as re-frame]
   [reagent.dom :as rd]
   se.jherrlin.claraman.client.ajax
   [reitit.frontend.easy :as rfe]
   [se.jherrlin.claraman.client.router :as client.router]
   [se.jherrlin.claraman.client.events :as events]
   ["semantic-ui-react" :as semantic-ui]))


(defn header-menu [router]
  [:div {:style {:flex "1"}}
   [:> semantic-ui/Menu {:size       "large"
                         :borderless true}
    [:> semantic-ui/Menu.Item
     {:as "a" :href (rfe/href :se.jherrlin.claraman.client.views.home/view)} "Home"]
    [:> semantic-ui/Menu.Item
     {:as "a"
      :href (rfe/href :se.jherrlin.claraman.client.views.create-game/view)} "Create game"]

    [:> semantic-ui/Menu.Item
     {:as "a"
      :href (rfe/href :se.jherrlin.claraman.client.views.join-game/view)} "Join game"]

    [:> semantic-ui/Menu.Item
     {:as "a"
      :href (rfe/href :se.jherrlin.claraman.client.views.past-games/view)} "Past games"]

    [:> semantic-ui/Menu.Item
     {:as "a" :target "_blank" :href "https://github.com/jherrlin/bomberman-with-clara"} "Source code"]

    [:> semantic-ui/Menu.Menu {:position "right" }
     [:> semantic-ui/Menu.Item
      [:img {:src "img/bomberman-logo.png" :style {:height "2em"}}]]]]])

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
                    :padding-top "1.0em"}}
      (when current-route
        (-> current-route :data :view))]]))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (rd/render [main-component {:router (client.router/handler)}] (.getElementById js/document "app")))


(defmethod websocket/wevent :new/game-state
  [{:as ev-msg :keys [event id ?data]}]
  (re-frame/dispatch [:game-state ?data]))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (client.router/init)
  (mount-root))

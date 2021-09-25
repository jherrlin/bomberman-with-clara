(ns se.jherrlin.client.main
  (:require
   [se.jherrlin.client.websocket :as websocket]
   [re-frame.core :as re-frame]
   [reagent.dom :as rd]
   [reitit.core :as r]
   [se.jherrlin.client.ajax :as ajax]
   [se.jherrlin.clara-labs.bomberman-rules :as bomberman-rules]
   [se.jherrlin.server.game-state :as game-state]
   [se.jherrlin.server.models :as models]
   [reitit.frontend.easy :as rfe]
   [se.jherrlin.client.router :as client.router]
   [se.jherrlin.client.events :as events]
   ["semantic-ui-react" :as semantic-ui]))


(defn header-menu [router]
  (let []
    [:div {:style {:flex "1"}}
     [:> semantic-ui/Menu {:size       "large"
                           :borderless true}
      [:> semantic-ui/Menu.Item
       {:as "a" :href (rfe/href :se.jherrlin.client.views.home/view)} "Home"]
      [:> semantic-ui/Menu.Item
       {:as "a"
        :href (rfe/href :se.jherrlin.client.views.create-game/view)} "Create game"]

      [:> semantic-ui/Menu.Item
       {:as "a"
        :href (rfe/href :se.jherrlin.client.views.join-game/view)} "Join game"]

      [:> semantic-ui/Menu.Item
       {:as "a"
        :href (rfe/href :se.jherrlin.client.views.past-games/view)} "Past games"]

      #_[:> semantic-ui/Menu.Item
       {:as "a" :href (rfe/href :route2/db)} "DB"]

      [:> semantic-ui/Menu.Item
       {:as "a" :target "_blank" :href "https://github.com/jherrlin/bomberman-with-clara"} "Source code"]

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
                    :padding-top "1.0em"}}
      (when current-route
        (-> current-route :data :view))]

     #_[:div
      [:> semantic-ui/Menu
       [:> semantic-ui/Menu.Item {:name "Notifikationer"}]
       [:> semantic-ui/Menu.Item {:name "Något"}]]]]))

(defn ^:dev/after-load mount-root []
  #_(re-frame/clear-subscription-cache!)
  (rd/render [main-component {:router (client.router/handler)}] (.getElementById js/document "app")))


(defmethod websocket/wevent :new/game-state
  [{:as ev-msg :keys [event id ?data]}]
  (re-frame/dispatch [:game-state ?data]))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (client.router/init)
  (mount-root))

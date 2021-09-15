(ns se.jherrlin.client.main
  (:require [re-frame.core :as re-frame]
            [reagent.dom :as rd]
            [reitit.core :as r]
            [se.jherrlin.client.events :as events]
            [reitit.frontend.easy :as rfe]
            ["semantic-ui-react" :as semantic-ui]))


(defn header-menu [router]
  (let []
    [:div {:style {:flex "1"}}
     [:> semantic-ui/Menu {:size       "large"
                           :borderless true
                           :style      {:background "#80ba27"}}
      [:> semantic-ui/Menu.Item
       {:as "a" :href (rfe/href :route1/home)} "Home"]
      [:> semantic-ui/Menu.Item
       {:as "a" :href (rfe/href :route2/db)} "DB"]

      [:> semantic-ui/Menu.Menu {:position "right" }
       [:> semantic-ui/Menu.Item
        #_[:> semantic-ui/Image {:as     "a"
                               :href "asd"
                               :src    picture
                               :avatar true}]]

       [:> semantic-ui/Menu.Item
        [:img {:src "img/logo.png" :style {:width "5em"}}]]]]]))

(defn main-component []
  [:> semantic-ui/Container
   {:style {:height         "100%"
            :display        "flex"
            :flex-direction "column"}}

   ;; Header
   [:div {:style {:flex "1"}}
     [:> semantic-ui/Menu {:size       "large"
                           :borderless true
                           }
      [:> semantic-ui/Menu.Item
       {:as "a"
        ;:href (rfe/href :route1/home)
        } "Home"]
      [:> semantic-ui/Menu.Item
       {:as "a"
        ;:href (rfe/href :route2/db)
        } "Info"]

      [:> semantic-ui/Menu.Item
       {:as "a"
        ;:href (rfe/href :route2/db)
        } "Create new game"]

      [:> semantic-ui/Menu.Item
       {:as "a"
        ;:href (rfe/href :route2/db)
        } "Join a game"]


      [:> semantic-ui/Menu.Menu {:position "right" }
       [:> semantic-ui/Menu.Item
        #_[:> semantic-ui/Image {:as     "a"
                               :href "asd"
                               :src    picture
                               :avatar true}]]

       [:> semantic-ui/Menu.Item
        [:img {:src "img/bomberman-logo.png" :style {:height "2em"}}]]]]]

   [:div {:style {:flex           "1"
                  :padding-top    "0.5em"
                  :flex-direction "column"}}
    ;; Submenu
    ]

   [:div {:style {:height      "100%"
                  :overflow-y  "auto"
                  :overflow-x  "hidden"
                  :padding-top "0.5em"}}
    ;; This is the main location on the page.
    ]

   [:div
    [:> semantic-ui/Menu
     [:> semantic-ui/Menu.Item {:name "Notifikationer"}]
     [:> semantic-ui/Menu.Item {:name "Något"}]]]])

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (rd/render [main-component] (.getElementById js/document "app")))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (mount-root))

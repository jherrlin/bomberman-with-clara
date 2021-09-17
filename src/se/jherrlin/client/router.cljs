(ns se.jherrlin.client.router
  (:require [re-frame.core :as re-frame]
            [reitit.coercion.spec :as rss]
            [cljs.pprint :as pprint]
            [reitit.core :as r]
            [reitit.frontend :as rf]
            [se.jherrlin.client.views.create-game :as views.create-game]
            [se.jherrlin.client.views.game-lobby :as views.game-lobby]
            [se.jherrlin.client.views.join-game :as views.join-game]
            [se.jherrlin.client.views.game-play :as views.game-play]
            [se.jherrlin.client.views.spectate-game :as views.spectate-game]
            [reitit.frontend.controllers :as rfc]
            [reitit.frontend.easy :as rfe]))


(re-frame/reg-event-db
 ::initialize-routes
 (fn [db _]
   (assoc db :current-route nil)))


(re-frame/reg-event-fx
 :push-state
 (fn [_ [_ & route]]
   {:push-state route}))

(re-frame/reg-event-db
 ::navigated
 (fn [db [_ new-match]]
   (let [old-match   (:current-route db)
         controllers (rfc/apply-controllers (:controllers old-match) new-match)]
     (assoc db :current-route (assoc new-match :controllers controllers)))))

(re-frame/reg-sub
 :current-route
 (fn [db]
   (:current-route db)))

(re-frame/reg-fx :push-state
  (fn [route]
    (apply rfe/push-state route)))

(defn on-navigate [new-match]
  (when new-match
    (re-frame/dispatch [::navigated new-match])))

(defn db []
  [:div
   [:pre
    (str "App db:\n"
         (with-out-str (pprint/pprint @re-frame.db/app-db)))]])

(def routes
  [["/"
    {:name      :route1/home
     :link-text "Home"
     :view      [:div "Home"]
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
       :start (fn [& params]
                (js/console.log "Entering home page")
                #_(re-frame/dispatch [::push-state ::home]))
       ;; Teardown can be done here.
       :stop  (fn [& params] (js/console.log "Leaving home page"))}]}]
   ["/db"
    {:name      :route2/db
     :link-text "db"
     :view      [#'db]
     :controllers
     [{:start (fn [& params]
                (js/console.log "Entering db page")
                #_(re-frame/dispatch [::push-state ::db]))
       :stop  (fn [& params] (js/console.log "Leaving db page"))}]}]])

(defn handler []
  (rf/router
   [routes
    (views.create-game/routes)
    (views.game-lobby/routes)
    (views.join-game/routes)
    (views.game-play/routes)
    (views.spectate-game/routes)
    ;; routes
    ]
   {:data {:coercion rss/coercion}}))

(defn init-routes! []
  (js/console.log "initializing routes")
  (rfe/start!
   (handler)
   on-navigate
   {:use-fragment true}))

(def debug? ^boolean goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))

(defn init []
  #_(re-frame/clear-subscription-cache!)
  (println "In here")
  (re-frame/dispatch-sync [::initialize-routes])
  (dev-setup)
  (init-routes!) ;; Reset routes on figwheel reload
)

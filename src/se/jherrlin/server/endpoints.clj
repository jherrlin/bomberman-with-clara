(ns se.jherrlin.server.endpoints
  (:require
   [clojure.spec.alpha :as s]
   [malli.util :as mu]
   [muuntaja.core :as m]
   reitit.coercion
   ring.middleware.keyword-params
   ring.middleware.params
   [reitit.coercion.malli]
   reitit.coercion.spec
   [reitit.dev.pretty :as pretty]
   [se.jherrlin.server.resources :as resources]
   [reitit.ring :as ring]
   [reitit.ring.coercion :as ring.coercion]
   [reitit.ring.malli]
   [clojure.spec.alpha :as s]
   [se.jherrlin.server.pages :as pages]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]
   [reitit.swagger :as swagger]
   [reitit.swagger-ui :as swagger-ui]
   [taoensso.timbre :as timbre]))


(defn websocket-endpoints [websocket]
  [""
   ["/websocket/chsk" (get websocket :reitit-routes)]])

(def events-demo
  (->> (resources/read-edn-file "events/2021-09-25_3-bots.edn")
       (:events)
       (reverse)))

(s/def ::uuid uuid?)
(s/def ::game-id ::uuid)

(defn handler [{:keys [websocket game-state incomming-actions middleware event-store] :as deps} req]
  (def game-state game-state)
  (def event-store event-store)
  ((ring/ring-handler
    (ring/router
     [["/" {:summary "Index page"
            :get     (fn [req]
                       {:status 200 :body (pages/index-html req)})}]

      (websocket-endpoints websocket)

      ["/events-demo"
       {:get (fn [req]
               {:status 200
                :body   events-demo})}]

      ["/list-games"
       {:get (fn [req]
               {:status 200
                :body
                (->> game-state :game-state deref :games vals (filter (comp #{:created} :game-state))
                     (map #(select-keys % [:game-id :game-name :players :game-create-timestamp]))
                     (map (fn [{:keys [players] :as game}]
                            (assoc game :players
                                   (->> players (vals) (map #(select-keys % [:player-name])))))))})}]

      ["/game-events/:game-id"
       {:coercion   reitit.coercion.spec/coercion
        :parameters {:path-params ::game-id}
        :get        (fn [req]
                      (let [{{:keys [game-id]} :path-params} req
                            game-uuid (java.util.UUID/fromString game-id)]
                        {:status 200
                         :body   (->> event-store
                                      :store
                                      deref
                                      :events
                                      (filter (comp #{game-uuid} :subject))
                                      (sort-by :time #(compare %2 %1))
                                      (reverse))}))}]

      ["/past-games"
       {:get (fn [req]
               {:status 200
                :body   (->> game-state :game-state deref :past-games)})}]

      ["/swagger.json"
       {:get {:no-doc  true
              :swagger {:info {:title       "my-api"
                               :description "with [malli](https://github.com/metosin/malli) and reitit-ring"}
                        :tags [{:name "files", :description "file api"}
                               {:name "math", :description "math api"}]}
              :handler (swagger/create-swagger-handler)}}]

      ["/health" {:get (fn [req]
                         {:status 200 :body "ok"})}]]
     {:exception pretty/exception
      :data      {:muuntaja   m/instance
                  :middleware [swagger/swagger-feature
                               parameters/parameters-middleware
                               muuntaja/format-negotiate-middleware
                               muuntaja/format-response-middleware
                               muuntaja/format-request-middleware
                               ring.coercion/coerce-exceptions-middleware
                               ring.coercion/coerce-response-middleware
                               ring.coercion/coerce-request-middleware

                               ;; used by Sente
                               ring.middleware.params/wrap-params
                               ring.middleware.keyword-params/wrap-keyword-params]}})

    (ring/routes
     (swagger-ui/create-swagger-ui-handler
      {:path   "/swagger"
       :config {:validatorUrl     nil
                :operationsSorter "alpha"}})
     (ring/create-resource-handler {:path "/"})
     (ring/create-default-handler {:not-found (constantly {:status 404 :body "Not found"})})))
   req))

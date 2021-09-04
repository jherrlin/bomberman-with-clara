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
   [reitit.ring :as ring]
   [reitit.ring.coercion :as ring.coercion]
   [reitit.ring.malli]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]
   [reitit.swagger :as swagger]
   [reitit.swagger-ui :as swagger-ui]
   [taoensso.timbre :as timbre]))


(defn websocket-endpoints [websocket]
  (def websocket websocket)
  [""
   ["/websocket/chsk" (get websocket :reitit-routes)]])


(defn handler [{:keys [websocket game-state incomming-actions middleware] :as deps} req]
  ((ring/ring-handler
    (ring/router
     [

      (websocket-endpoints websocket)

      ["/swagger.json"
       {:get {:no-doc  true
              :swagger {:info {:title       "my-api"
                               :description "with [malli](https://github.com/metosin/malli) and reitit-ring"}
                        :tags [{:name "files", :description "file api"}
                               {:name "math", :description "math api"}]}
              :handler (swagger/create-swagger-handler)}}]

      ["/health" {:get (fn [_]
                         {:status 200 :body "ok"})}]]
     {
      ;; :compile   reitit.coercion/compile-request-coercers
      :exception pretty/exception
      :data      {
                  ;; :coercion   reitit.coercion.spec/coercion
                  :muuntaja   m/instance
                  :middleware [#_log-middleware
                               swagger/swagger-feature
                               parameters/parameters-middleware
                               muuntaja/format-negotiate-middleware
                               muuntaja/format-response-middleware
                               muuntaja/format-request-middleware
                               ring.coercion/coerce-exceptions-middleware
                               ring.coercion/coerce-response-middleware
                               ring.coercion/coerce-request-middleware

                               ;; used by Sente
                               ring.middleware.params/wrap-params
                               ring.middleware.keyword-params/wrap-keyword-params

                               ]}})

    (ring/routes
     (swagger-ui/create-swagger-ui-handler
      {:path   "/swagger"
       :config {:validatorUrl     nil
                :operationsSorter "alpha"}})
     (ring/create-resource-handler {:path "/"})
     (ring/create-default-handler {:not-found (constantly {:status 404 :body "Not found"})})))
   req))

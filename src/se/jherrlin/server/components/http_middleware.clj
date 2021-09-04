(ns se.jherrlin.server.components.http-middleware
  (:require [com.stuartsierra.component :as component]
            [ring.middleware.session.memory :as memory]
            [ring.middleware.session :as session]
            ring.middleware.anti-forgery
            [taoensso.timbre :as timbre]))


(defrecord HttpMiddleware [args config]
  component/Lifecycle

  (start [this]
    (timbre/info "Creating HttpMiddleware component.")
    (let [store (memory/memory-store)]
      (assoc this
             :store store
             :ring-session-with-anti-forgery
             (case (:environment config)
               :test []
               [[session/wrap-session {:store store}]
                ring.middleware.anti-forgery/wrap-anti-forgery]))))

  (stop [this]
    (timbre/info "Tearing down HttpMiddleware component.")
    (assoc this
           :ring-session-with-anti-forgery nil
           :args                           nil
           :store                          nil)))

(defn create [& [config]]
  (map->HttpMiddleware {:args config}))

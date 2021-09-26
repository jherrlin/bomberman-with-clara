(ns se.jherrlin.claraman.server.components.httpkit
  (:require [com.stuartsierra.component :as component]
            [org.httpkit.server :as httpkit.server]
            [taoensso.timbre :as timbre]))


(defrecord HttpkitServer [args router]
  component/Lifecycle

  (start [this]
    (if (:server this)
      (do
        (timbre/info "Starting Httpkit component but server is already runnig.")
        this)
      (do
        (timbre/info "Starting Httpkit component.")
        (let [options {:legacy-return-value? false
                       :port                 (:port args)}
              handler (:handler router)
              server  (httpkit.server/run-server (fn [req] (handler req)) options)]
          (assoc this :handler handler :server server)))))

  (stop [this]
    (if-let [server (get this :server)]
      (do
        (timbre/info "Stopping Httpkit component.")
        (httpkit.server/server-stop! server)
        (assoc this :server nil :handler nil))
      (do
        (timbre/info "Stopping Httpkit component but no server instance found!")
        this))))

(defn create
  "Create a new Httpkit component.

  - `port`     Server port."
  [config]
  (map->HttpkitServer {:args config}))

(ns se.jherrlin.server.components.router
  (:require [com.stuartsierra.component :as component]
            [taoensso.timbre :as timbre]))


(defrecord Router [args websocket middleware game-state incomming-actions]
  component/Lifecycle

  (start [this]
    (timbre/info "Starting router component.")
    (let [handler (:handler args)]
      (def websocket websocket)
      (assoc this :handler (partial handler {:websocket         websocket
                                             :game-state        game-state
                                             :incomming-actions incomming-actions
                                             :middleware        middleware}))))

  (stop [this]
    (timbre/info "Stopping router component.")
    (assoc this :handler nil)))

(defn create
  "Create a new router component."
  [config]
  (map->Router {:args config}))

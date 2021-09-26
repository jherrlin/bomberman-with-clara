(ns se.jherrlin.claraman.server.components.router
  (:require [com.stuartsierra.component :as component]
            [taoensso.timbre :as timbre]))


(defrecord Router [args websocket middleware game-state event-store]
  component/Lifecycle

  (start [this]
    (timbre/info "Starting router component.")
    (let [handler (:handler args)]
      (def websocket websocket)
      (assoc this :handler (partial handler {:websocket         websocket
                                             :game-state        game-state
                                             :middleware        middleware
                                             :event-store       event-store}))))

  (stop [this]
    (timbre/info "Stopping router component.")
    (assoc this :handler nil)))

(defn create
  "Create a new router component."
  [config]
  (map->Router {:args config}))

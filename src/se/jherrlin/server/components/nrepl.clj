(ns se.jherrlin.server.components.nrepl
  (:require
   [com.stuartsierra.component :as component]
   [nrepl.server :as nrepl]
   [taoensso.timbre :as timbre]))


(defrecord NreplServer [args config]
  component/Lifecycle
  (start [this]
    (let [environment (:environment config)]
      (cond
        (get this :server)
        (do
          (timbre/info "Starting NREPL component but an instance is already running.")
          this)

        (= :test environment)
        (do
          (timbre/info "Not starting NREPL component when testing")
          this)

        :else
        (do
          (timbre/info "Starting NREPL component.")
          (let [address (or (:address args) "127.0.0.1")
                port    (or (:port args) 50505)
                server  (nrepl.server/start-server :bind address :port port)]
            (assoc this :server server))))))

  (stop [this]
    (if-let [server (get this :server)]
      (do
        (timbre/info "Stopping NREPL component.")
        (nrepl/stop-server server)
        (assoc this :server nil))
      (do
        (timbre/info "Stopping NREPL component but no server instance found!")
        this))))

(defn create
  "Create NREPL component

  - `:port`
  - `:address`"
  [config]
  (map->NreplServer {:args config}))

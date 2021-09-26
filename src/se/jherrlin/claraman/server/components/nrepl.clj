(ns se.jherrlin.claraman.server.components.nrepl
  (:require
   [com.stuartsierra.component :as component]
   [nrepl.server :as nrepl]
   [taoensso.timbre :as timbre]))


(defrecord NreplServer [args]
  component/Lifecycle
  (start [this]
    (if (get this :server)
      (do
        (timbre/info "Starting NREPL component but an instance is already running.")
        this)
      (do
        (timbre/info "Starting NREPL component.")
        (let [address (or (:address args) "127.0.0.1")
              port    (or (:port args) 50505)
              server  (nrepl.server/start-server :bind address :port port)]
          (assoc this :server server :port port :address address)))))
  (stop [this]
    (timbre/info "NREPL wont be stopped!")
    this))

(defn create
  "Create NREPL component

  - `:port`
  - `:address`"
  []
  (map->NreplServer {:args nil}))

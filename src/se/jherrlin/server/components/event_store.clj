(ns se.jherrlin.server.components.event-store
  (:require [com.stuartsierra.component :as component]
            [taoensso.timbre :as timbre]))

(defonce store-init {:events '()})
(defonce store (atom store-init))

(comment
  (reset! store store-init)
  )

(defn add-event [store event]
  (swap! store update :events conj event))



(defrecord EventStore [args]
  component/Lifecycle

  (start [this]
    (if (:store this)
      (do
        (timbre/info "Starting EventStore component but server is already runnig.")
        this)
      (do
        (timbre/info "Starting EventStore component.")
        (assoc this
               :store     store
               :add-event (partial add-event store)))))

  (stop [this]
    (if (get this :store)
      (do
        (timbre/info "Stopping EventStore component.")
        (reset! store store-init)
        (assoc this :store nil :add-event nil))
      (do
        (timbre/info "Stopping EventStore component but no server instance found!")
        this))))

(defn create
  "Create a new EventStore component."
  []
  (map->EventStore {:args nil}))

(comment
  (add-watch store :watcher
             (fn [key atom old-state new-state]
               (prn "-- Atom Changed --")
               (prn "key" key)
               (prn "atom" atom)
               (prn "atom type" (type atom))
               (prn "old-state" old-state)
               (prn "new-state" new-state)
               (prn "latest" (-> new-state :events first))))

  @store
  (reset! store store-init)

  (add-event {:a :b})
  (add-event {:c :d})
  (add-event {:abc :def})

  ;; https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type
  ;; Används av Google Docs

  ;; https://en.wikipedia.org/wiki/Raft_(algorithm)

  ;; https://orbitdb.org/
  )

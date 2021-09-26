(ns se.jherrlin.claraman.server.components.chime
  (:require
   [taoensso.timbre :as timbre]
   [com.stuartsierra.component :as component]
   [clojure.core.async :as a :refer [<! go-loop close!]]
   [chime.core-async :refer [chime-ch]]))

(def run-loop (atom nil))

(defrecord ChimeAsync [args game-state incomming-actions websocket event-store]
  component/Lifecycle

  (start [this]
    (let [{:keys [chimes]}  this
          {:keys [f schedule]} args]
      (if chimes
        (do
          (timbre/info "Starting Chime component but another instance is already running!")
          this)
        (do
          (timbre/info "Starting Chime component.")
          (reset! run-loop true)
          (let [chimes (chime-ch schedule)]
            (go-loop []
              (when-let [timestamp (<! chimes)]
                (f timestamp (:game-state game-state) incomming-actions (:broadcast-fn! websocket) (:add-event-fn! event-store) (:add-events-fn! event-store))
                (when @run-loop
                  (recur))))
            (assoc this :chimes chimes))))))

  (stop [this]
    (let [{:keys [chimes]} this]
      (if chimes
        (do
          (timbre/info "Stopping Chime component.")
          (reset! run-loop nil)
          (close! chimes)
          (assoc this :chimes nil))
        (do
          (timbre/info "Stopping Chime component but no instance found!")
          this)))))

(defn create
  "Create a new Chime component.

  - `task` Function that returns Chime task vector.
           Function gets `suppliers-state` atom as argument."
  [config]
  (map->ChimeAsync {:args config}))

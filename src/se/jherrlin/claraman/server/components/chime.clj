(ns se.jherrlin.claraman.server.components.chime
  (:require
   [taoensso.timbre :as timbre]
   [com.stuartsierra.component :as component]
   [clojure.core.async :as a :refer [<! go-loop close!]]
   [chime.core-async :refer [chime-ch]]))

(def run-loop (atom nil))

(defrecord ChimeAsync [args game-state websocket event-store incomming-player-commands]
  component/Lifecycle

  (start [this]
    (let [{:keys [chimes]}     this
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
                (f
                 {:task-execution-timestamp         timestamp
                  :game-state                       (:game-state game-state)
                  :ws-broadcast-fn!                 (:broadcast-fn! websocket)
                  :add-events-fn!                   (:add-events-fn! event-store)
                  :incomming-commands-state         (:incomming-commands-state incomming-player-commands)
                  :reset-incomming-player-commands! (:reset-incomming-player-commands! incomming-player-commands)})
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

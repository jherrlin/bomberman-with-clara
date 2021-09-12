(ns se.jherrlin.server.components.game-state
  (:require [com.stuartsierra.component :as component]
            [taoensso.timbre :as timbre]))


(def initial-game-state {})
(defonce game-state (atom initial-game-state))

(comment
  @game-state
  (reset! game-state initial-game-state)
  )

(defrecord GameState [args event-store]
  component/Lifecycle

  (start [this]
    (if (:game-state this)
      (do
        (timbre/info "Starting GameState component but server is already runnig.")
        this)
      (let [projection-fn (:projection-fn args)]
        (timbre/info "Starting GameState component.")
        (add-watch (:store event-store) :game-state-projection
                   (fn [key atom old-state new-state]
                     (when-let [latest-event (-> new-state :events first)]
                       (let [old-game-state @game-state
                             new-game-state (projection-fn old-game-state latest-event)]
                         (reset! game-state new-game-state)))))
        (assoc this
               :projection-fn projection-fn
               :game-state    game-state))))

  (stop [this]
    (if (get this :game-state)
      (do
        (timbre/info "Stopping GameState component.")
        ;; (reset! game-state initial-game-state)
        (remove-watch (:store event-store) :game-state-projection)
        (assoc this :projection-fn nil :game-state nil))
      (do
        (timbre/info "Stopping GameState component but no instance found!")
        this))))

(defn create
  "Create a new GameState component."
  [projection-fn]
  (map->GameState {:args {:projection-fn projection-fn}}))

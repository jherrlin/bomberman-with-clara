(ns se.jherrlin.claraman.server.components.incomming-commands
  (:require [clojure.spec.alpha :as s]
            [se.jherrlin.claraman.user-commands :as user-commands]
            [com.stuartsierra.component :as component]
            [taoensso.timbre :as timbre]))


(def incomming-commands-state-init {})
(defonce incomming-commands-state (atom incomming-commands-state-init))

(defn reset-incomming-player-commands! []
  (reset! incomming-commands-state incomming-commands-state-init))

(defmulti register-incomming-player-command!
  (fn [incomming-commands-state {:keys [action] :as command}]
    (if (s/valid? ::user-commands/commands command)
      action
      (timbre/error "Command dont confirm to spec: " command))))

(defmethod register-incomming-player-command! :move [incomming-commands-state {:keys [game-id action user-id] :as m}]
  (swap! incomming-commands-state assoc-in [game-id user-id action] m))

(defmethod register-incomming-player-command! :place-bomb [incomming-commands-state {:keys [game-id action user-id] :as m}]
  (swap! incomming-commands-state assoc-in [game-id user-id action] m))

(defmethod register-incomming-player-command! :throw-bomb [incomming-commands-state {:keys [game-id action user-id] :as m}]
  (swap! incomming-commands-state assoc-in [game-id user-id action] m))

(defmethod register-incomming-player-command! :default [incomming-commands-state m]
  (timbre/error "In dont know what to do with" m))

(defrecord PlayerCommands [args]
  component/Lifecycle

  (start [this]
    (if (:store this)
      (do
        (timbre/info "Starting PlayerCommands component but server is already runnig.")
        this)
      (do
        (timbre/info "Starting PlayerCommands component.")
        (assoc this
               :incomming-commands-state           incomming-commands-state
               :register-incomming-player-command! (partial register-incomming-player-command! incomming-commands-state)
               :reset-incomming-player-commands!   reset-incomming-player-commands!))))

  (stop [this]
    (if (get this :store)
      (do
        (timbre/info "Stopping PlayerCommands component.")
        (reset-incomming-player-commands!)
        (assoc this
               :incomming-commands-state           nil
               :register-incomming-player-command! nil
               :reset-incomming-player-commands!   nil))
      (do
        (timbre/info "Stopping PlayerCommands component but no server instance found!")
        this))))

(defn create
  "Create a new PlayerCommands component."
  []
  (map->PlayerCommands {:args nil}))

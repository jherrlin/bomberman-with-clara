(ns se.jherrlin.claraman.server.endpoints-ws
  (:require [taoensso.timbre :as timbre]
            [se.jherrlin.claraman.server.application-service :as application-service]
            [se.jherrlin.datetime :as datetime]))


(defmulti handler
  (fn [{:keys [?data client-id id]}]
    (timbre/debug "Incomming command: "id client-id ?data)
    id))

(defmethod handler :default
  [req]
  (timbre/debug "Dont know what do to with handler: " (:id req)))

(defmethod handler :command/user-action
  [{:keys [?data incomming-player-commands]}]
  (let [{:keys [register-incomming-player-command!]} incomming-player-commands]
    (register-incomming-player-command! (assoc ?data :timestamp (datetime/now)))))

(defmethod handler :game/create
  [req]
  (let [{:keys [?data ?reply-fn event-store game-state]} req
        {:keys [add-events-fn!]}                         event-store]
    (?reply-fn
     (application-service/create-game! game-state add-events-fn! (assoc ?data :action :create-game)))))

(defmethod handler :game/join
  [req]
  (let [{:keys [?data ?reply-fn event-store game-state]} req
        {:keys [add-events-fn!]}                         event-store]
    (?reply-fn
     (application-service/join-game! game-state add-events-fn! (assoc ?data :action :join-game)))))

(defmethod handler :game/start
  [req]
  (let [{:keys [?data ?reply-fn event-store game-state]} req
        {:keys [add-events-fn!]}                         event-store]
    (?reply-fn (application-service/start-game! game-state add-events-fn! (assoc ?data :action :start-game)))))

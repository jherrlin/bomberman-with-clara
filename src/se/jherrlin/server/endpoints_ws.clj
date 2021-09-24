(ns se.jherrlin.server.endpoints-ws
  (:require [taoensso.timbre :as timbre]
            [se.jherrlin.server.user-commands :as user-commands]
            [se.jherrlin.server.application-service :as application-service]
            [se.jherrlin.datetime :as datetime]))


(defmulti handler :id)

(defmethod handler :default
  [req]
  ;; (def req req)
  (timbre/debug "Dont know what do to with handler: " (:id req)))

(defmethod handler :command/user-action
  [{:keys [?reply-fn incomming-actions ?data client-id id] :as req}]
  (timbre/debug id client-id ?data)
  (user-commands/register-incomming-user-command! incomming-actions (assoc ?data :timestamp (datetime/now))))

(defmethod handler :game/create
  [req]
  (let [{:keys [?data ?reply-fn event-store game-state client-id id]} req
        {:keys [add-events-fn!]}                                      event-store]
    (timbre/debug id client-id ?data)
    (?reply-fn
     (application-service/create-game! game-state add-events-fn! (assoc ?data :action :create-game)))))

(defmethod handler :game/join
  [req]
  (let [{:keys [?data ?reply-fn event-store game-state id client-id]} req
        {:keys [add-events-fn!]}                                      event-store]
    (timbre/debug id client-id ?data)
    (?reply-fn
     (application-service/join-game! game-state add-events-fn! (assoc ?data :action :join-game)))))

(defmethod handler :game/start
  [req]
  (let [{:keys [?data ?reply-fn event-store game-state id client-id]} req
        {:keys [add-events-fn!]}                                      event-store]
    (timbre/debug id client-id ?data)
    (?reply-fn (application-service/start-game! game-state add-events-fn! (assoc ?data :action :start-game)))))

(defmethod handler :game/events
  [req]
  (let [{:keys [?data ?reply-fn event-store]} req
        {:keys [game-id]}                     ?data]
    (?reply-fn
     (->> event-store
          :store
          deref
          :events
          (filter (comp #{game-id} :subject))
          (sort-by :time #(compare %2 %1))
          (reverse)))))

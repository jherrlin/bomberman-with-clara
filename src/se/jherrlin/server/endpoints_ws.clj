(ns se.jherrlin.server.endpoints-ws
  (:require [taoensso.timbre :as timbre]
            [clojure.spec.alpha :as s]
            [se.jherrlin.server.user-commands :as user-commands]
            [se.jherrlin.server.application-service :as application-service]
            [se.jherrlin.datetime :as datetime]))


(defmulti handler :id)

(defmethod handler :default
  [req]
  ;; (def req req)
  (timbre/debug "Dont know what do to with handler: " (:id req)))

(defmethod handler ::echo
  [{:keys [?reply-fn] :as req}]
  (?reply-fn {:server-sending-in-ws "JA-JA-JA"}))

(defmethod handler :command/user-action
  [{:keys [?reply-fn incomming-actions ?data] :as req}]
  (def ?data ?data)
  (user-commands/register-incomming-user-command! incomming-actions (assoc ?data :timestamp (datetime/now))))

(defmethod handler :game/create
  [req]
  (def req req)
  (def game-state (-> req :game-state))
  (def add-events-fn! (-> req :event-store :add-events-fn!))
  (let [{:keys [?data ?reply-fn event-store game-state]} req
        {:keys [add-events-fn!]}                         event-store]
    (?reply-fn
     (application-service/create-game! game-state add-events-fn! (assoc ?data :action :create-game)))))

(defmethod handler :game/list
  [req]
  (let [{:keys [?reply-fn]} req]
    (?reply-fn (->> req :game-state deref :games vals
                    (filter (comp #{:created} :game-state))
                    (map #(select-keys % [:game-name :subject]))))))

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

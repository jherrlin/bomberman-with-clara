(ns se.jherrlin.server.endpoints-ws
  (:require [taoensso.timbre :as timbre]
            [clojure.spec.alpha :as s]
            [se.jherrlin.server.user-commands :as user-commands]
            [se.jherrlin.server.application-service :as application-service]))


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
  (user-commands/register-incomming-user-command! incomming-actions ?data))

(defmethod handler :game/create
  [req]
  (def req req)
  (let [{:keys [?data ?reply-fn client-id event-store incomming-actions projection-fn game-state send-fn]} req
        {:keys [add-events-fn!]}                                                                           event-store]
    (send-fn client-id [:game/create-response
                        (application-service/create-game! game-state add-events-fn! (assoc ?data :action :create-game))])))

{:action        :create-game
 :game-name     ""
 :game-password ""}





(defmethod handler :game/list
  [req]
  (let [{:keys [?data ?reply-fn client-id event-store game-state incomming-actions]} req
        {:keys [game-name password]}                                                 ?data]
    (?reply-fn (->> req :game-state deref :games vals
                    (filter (comp #{:created} :game-state))
                    (map #(select-keys % [:game-name :subject]))))))

(defmethod handler :game/join
  [req]
  (let [{:keys [?data ?reply-fn client-id event-store game-state incomming-actions]} req
        {:keys [subject player-id player-name password]}                             ?data]
    (application-service/join-game! (:add-event-fn! event-store) subject player-id player-name)))

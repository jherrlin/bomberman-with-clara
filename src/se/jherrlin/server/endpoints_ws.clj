(ns se.jherrlin.server.endpoints-ws
  (:require [taoensso.timbre :as timbre]
            [se.jherrlin.server.user-commands :as user-commands]
            [se.jherrlin.server.application-service :as application-service]
            [clojure.spec.alpha :as s]))


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
  (if (s/valid? ::user-commands/actions ?data)
    (user-commands/register-incomming-user-command! incomming-actions ?data)
    (timbre/error "Dont know how to handle incomming user action: " ?data)))

(defmethod handler :game/create
  [req]
  (def req req)
  (let [{:keys [?reply-fn incomming-actions ?data client-id event-store]} req
        {:keys [game-name password]}                                      ?data]
    (application-service/create-game (:add-event-fn! event-store) game-name password)))

(defmethod handler :game/list
  [req]
  (let [{:keys [?data ?reply-fn client-id event-store game-state incomming-actions]} req
        {:keys [game-name password]}                                                 ?data]
    (?reply-fn (->> req :game-state :game-state deref :games vals
                    (filter (comp #{:created} :game-state))
                    (map #(select-keys % [:game-name :subject]))))))

(defmethod handler :game/join
  [req]
  (let [{:keys [?data ?reply-fn client-id event-store game-state incomming-actions]} req
        {:keys [subject player-id player-name password]}                             ?data]
    (application-service/join-game (:add-event-fn! event-store) subject player-id player-name)))

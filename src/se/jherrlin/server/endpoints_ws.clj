(ns se.jherrlin.server.endpoints-ws
  (:require [taoensso.timbre :as timbre]
            [se.jherrlin.server.user-commands :as user-commands]
            [clojure.spec.alpha :as s]))


(defmulti handler :id)

(defmethod handler :default
  [req]
  (def req req)
  (timbre/debug "Dont know what do to with handler: " (:id req)))

(defmethod handler ::echo
  [{:keys [?reply-fn] :as req}]
  (?reply-fn {:server-sending-in-ws "JA-JA-JA"}))


(defmethod handler :command/user-action
  [{:keys [?reply-fn incomming-actions ?data] :as req}]
  (if (s/valid? ::user-commands/actions ?data)
    (user-commands/register-incomming-user-action! incomming-actions ?data)
    (timbre/error "Dont know how to handle incomming user action: " ?data)))

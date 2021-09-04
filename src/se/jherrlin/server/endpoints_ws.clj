(ns se.jherrlin.server.endpoints-ws
  (:require [taoensso.timbre :as timbre]))


(defmulti handler :id)

(defmethod handler :default
  [req]
  (def req req)
  (timbre/debug "Dont know what do to with handler: " (:id req)))

(defmethod handler ::echo
  [{:keys [?reply-fn] :as req}]
  (?reply-fn {:server-sending-in-ws "JA-JA-JA"}))

(ns se.jherrlin.claraman.client.websocket
  (:require [taoensso.timbre :as timbre]
            [re-frame.core :as re-frame]
            [taoensso.sente :as sente :refer [cb-success?]]))


(declare chsk-send!)
(declare chsk)
(declare ch-chsk)
(declare chsk-state)

(defmulti wevent :id)

(defn event-msg-handler
  "Wraps `wevent` with logging, error catching, etc."
  [{:as ev-msg :keys [id ?data]}]
  #_(timbre/debug "incomming on ws from server" ev-msg)
  (wevent ev-msg))

(defmethod wevent :default
  [{:as ev-msg :keys [event id ?data]}]
  #_(timbre/debug "on default event handler" ev-msg)
  (timbre/debug "Could not find ws event handler so running on :default" id ?data)
  nil)

(try
  ;; Setup websocket
  (let [{:keys [chsk ch-recv send-fn state]}
        (sente/make-channel-socket! "/websocket/chsk"
                                    nil
                                    {:type           :auto
                                     :wrap-recv-evs? false})]
    (def chsk       chsk)
    (def ch-chsk    ch-recv)     ; ChannelSocket's receive channel
    (def chsk-send! send-fn)
    (def chsk-state state)       ; Watchable, read-only atom

    (sente/start-client-chsk-router!
     ch-chsk event-msg-handler))

  (catch js/Error e
    (js/console.log e)))

(re-frame/reg-fx
 :ws-send
 (fn [{:keys [data on-error on-success timeout]
       :or   {on-error   #(js/console.log %)
              on-success #(js/console.log %)
              timeout    5000}}]
   (if-not on-success
     (chsk-send! data)
     (chsk-send! data timeout
                 (fn [cb-reply]
                   (if (cb-success? cb-reply)
                     (on-success cb-reply)
                     (on-error cb-reply)))))))

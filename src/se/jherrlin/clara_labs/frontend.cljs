(ns se.jherrlin.clara-labs.frontend
  (:require [reagent.dom :as rd]
            ["semantic-ui-react" :as semantic-ui]
            [taoensso.timbre :as timbre]
            [taoensso.sente :as sente :refer [cb-success?]]
            ))

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
  (let [{:keys [chsk ch-recv send-fn state]}
        (sente/make-channel-socket! "/websocket/chsk"
                                    nil
                                    {
                                     :port           3005
                                     :type           :auto
                                     :wrap-recv-evs? false})]
    (def chsk       chsk)
    (def ch-chsk    ch-recv)     ; ChannelSocket's receive channel
    (def chsk-send! send-fn)
    (def chsk-state state)       ; Watchable, read-only atom

    (sente/start-client-chsk-router!
     ch-chsk event-msg-handler))

  (catch js/Error e
    (js/console.log e)))


(defn root-component []
  [:> semantic-ui/Container
        {:style {:height         "100%"
                 :display        "flex"
                 :flex-direction "column"}}
   [:div "Hello World"]
   ]
  )

(defn mount-root []
  (rd/render [root-component] (.getElementById js/document "root")))

(defn init []
  (println "Hello World!")
  (mount-root)
  )

;; (js/alert "Hejsan")

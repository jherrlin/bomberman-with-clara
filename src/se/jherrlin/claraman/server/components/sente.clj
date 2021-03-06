(ns se.jherrlin.claraman.server.components.sente
  (:require [com.stuartsierra.component :as component]
            [taoensso.timbre :as timbre]
            [taoensso.sente :as sente]))


(defn- get-sch-adapter []
  (when-let [server-name
             (cond
               (resolve 'org.httpkit.server/run-server)
               'http-kit

               (resolve 'nginx.clojure.embed/run-server)
               'nginx-clojure

               (resolve 'aleph.http/start-server)
               'aleph)]

    (let [sym (->> server-name (str "taoensso.sente.server-adapters.") symbol)]
      (require sym)
      ((resolve (symbol (str sym "/get-sch-adapter")))))))

(defrecord SenteWebsocket [args game-state event-store incomming-player-commands]
  component/Lifecycle
  (start [this]
    (if (::server this)
      (do
        (timbre/info "Starting Sente websocket server but server is already started.")
        this)
      (let [{:keys [config event-msg-handler handler]} args
            event-msg-handler
            (or event-msg-handler
                (fn event-msg-handler [ws-event]
                  (future (handler
                           (assoc ws-event
                                  :game-state                (:game-state game-state)
                                  :projection-fn             (:projection-fn game-state)
                                  :incomming-player-commands incomming-player-commands
                                  :event-store               event-store)))))
            sch-adapter                                (get-sch-adapter)
            {:keys [ch-recv send-fn connected-uids
                    ajax-post-fn ajax-get-or-ws-handshake-fn]}
            (sente/make-channel-socket-server!
             sch-adapter
             (merge {:packer        :edn
                     :user-id-fn    :client-id
                     :csrf-token-fn nil}  ;; WARNING!!!!
                    config))]
        (timbre/info "Starting Sente websocket server.")

        (def connected-uids connected-uids)

        {:ring-ajax-post                ajax-post-fn
         :ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn
         :ch-chsk                       ch-recv
         :chsk-send!                    send-fn
         :connected-uids                connected-uids
         :event-msg-handler             event-msg-handler
         :broadcast-fn!                 (fn [message]
                                          (doseq [uid (-> connected-uids deref :ws)]
                                            (send-fn uid message)))
         :stop-fn                       (sente/start-server-chsk-router!
                                         ch-recv
                                         event-msg-handler)
         :reitit-routes                 {:name ::websocket-sente
                                         :post ajax-post-fn
                                         :get  ajax-get-or-ws-handshake-fn}})))

  (stop [this]
    (if-let [stop-fn (:stop-fn this)]
      (do
        (timbre/info "Stopping Sente websocket server.")
        (stop-fn)
        (assoc this
               :ring-ajax-post                nil
               :ring-ajax-get-or-ws-handshake nil
               :ch-chsk                       nil
               :chsk-send!                    nil
               :connected-uids                nil
               :event-msg-handler             nil
               :stop-fn                       nil
               :reitit-routes                 nil))
      (do
        (timbre/info "Stopping Sente websocket server but no server instance found.")
        this))))

(defn create
  "Create a new SenteWebsocket component.

  *- `:event-msg-handler`
  *- `:config`
  `:handler`
  "
  [config]
  (map->SenteWebsocket {:args config}))

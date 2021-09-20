(ns se.jherrlin.server.components.timbre
  (:require
   [taoensso.timbre :as timbre]
   [com.stuartsierra.component :as component]
   [taoensso.timbre.appenders.core :as appenders]))


(defn config-logging [{:keys [logfile println?] :as config}]
  (timbre/info "Configure loggin with `timbre`.")
  ;; timbre/default-config
  (let [time-config (assoc
                     timbre/default-timestamp-opts
                     :timezone (java.util.TimeZone/getTimeZone "Europe/Stockholm"))]
    (timbre/merge-config!
     (merge
      (cond-> {:timestamp-opts time-config
               ;:ns-whitelist ["se.jherrlin.*"]
               }
        logfile
        (assoc :appenders {:println {:enabled? println?}
                           :spit    (appenders/spit-appender {:fname     logfile
                                                              :hostname_ "server"
                                                              :instant   java.util.Date})}))
      config))))

(defrecord Timbre [args]
  component/Lifecycle

  (start [this]
    (timbre/info "Creating Timbre component.")
    (assoc this :config (config-logging args)))

  (stop [this]
    (timbre/info "Tearing down Timbre component.")
    (timbre/info "Setting timbre to `timbre/default-config`")
    (timbre/set-config! timbre/default-config)
    (assoc this :config nil)))

(defn create [config]
  (map->Timbre {:args config}))

(comment
  (config-logging {:min-level :warn})
  (create {:min-level :warn})
  )

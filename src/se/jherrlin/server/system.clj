(ns se.jherrlin.server.system
  (:require
   [chime.core :as chime]
   [com.stuartsierra.component :as component]
   [se.jherrlin.server.components.http-middleware :as components.http-middleware]
   [se.jherrlin.server.components.httpkit :as components.httpkit]
   [se.jherrlin.server.components.nrepl :as components.nrepl]
   [se.jherrlin.server.components.router :as components.router]
   [se.jherrlin.server.components.sente :as components.sente]
   [se.jherrlin.server.components.chime :as components.chime]
   [se.jherrlin.server.components.timbre :as components.timbre]
   [se.jherrlin.server.endpoints :as server.endpoints]
   [se.jherrlin.server.endpoints-ws :as server.endpoints-ws]
   [taoensso.timbre :as timbre])
  (:import [java.time Instant Duration])
  (:gen-class))


(defn system [{:keys [scheduler timbre webserver ws-handler http-handler]}]
  (timbre/info "Creating system.")
  (component/system-map
   :game-state        (atom {})
   :incomming-actions (atom {})
   :logging           (components.timbre/create timbre)
   :scheduler         (component/using
                       (components.chime/create scheduler)
                       [:game-state :incomming-actions])
   :websocket         (component/using
                       (components.sente/create {:handler ws-handler})
                       [:game-state :incomming-actions])
   :router            (component/using
                       (components.router/create {:handler http-handler})
                       [:websocket :logging :game-state :incomming-actions])
   :webserver         (component/using
                       (components.httpkit/create webserver)
                       [:logging :router])))

(def production
  (system
   {:http-handler #'server.endpoints/handler
    :webserver    {:port 3005}
    :ws-handler   #'server.endpoints-ws/handler
    :scheduler    {:f        (fn [task-execution-timestamp game-state incomming-state]
                               ;; (println "Schedule:" task-execution-timestamp game-state incomming-state)
                               )
                   :schedule (chime/periodic-seq (Instant/now)
                                                 (Duration/ofMinutes 2)
                                                 #_(Duration/ofMillis 200))}}))


(comment
  (alter-var-root #'production component/start)
  (alter-var-root #'production component/stop)
  )


{:action  :move
 :user-id 1
 :payload {:direction :west}
 }

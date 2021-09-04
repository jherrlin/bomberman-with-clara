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
   [se.jherrlin.clara-labs.board :as board]
   [taoensso.timbre :as timbre])
  (:import [java.time Instant Duration])
  (:gen-class))


(defn system [{:keys [scheduler timbre webserver ws-handler http-handler]}]
  (timbre/info "Creating system.")
  (component/system-map
   :game-state        (atom {:players {1 {:position    [1 1]
                                          :fire-length 3
                                          :sign        "1"}}
                             :stones  [[4 1]]
                             :board   (board/init 6)
                             :fire    #{}})
   :incomming-actions (atom {})
   :logging           (components.timbre/create timbre)
   :scheduler         (component/using
                       (components.chime/create scheduler)
                       [:game-state :incomming-actions :websocket])
   :websocket         (component/using
                       (components.sente/create {:handler ws-handler})
                       [:game-state :incomming-actions])
   :router            (component/using
                       (components.router/create {:handler http-handler})
                       [:websocket :logging :game-state :incomming-actions])
   :webserver         (component/using
                       (components.httpkit/create webserver)
                       [:logging :router])))

(defn game-loop [task-execution-timestamp game-state incomming-state ws-broadcast]
  (def ws-broadcast ws-broadcast)
  (def game-state game-state)
  (def incomming-state incomming-state)

  ;; (ws-broadcast [:test/broadcast "hejsan"])
  )

(def production
  (system
   {:http-handler #'server.endpoints/handler
    :webserver    {:port 3005}
    :ws-handler   #'server.endpoints-ws/handler
    :scheduler    {:f        #'game-loop
                   :schedule (chime/periodic-seq (Instant/now)
                                                 (Duration/ofMinutes 2)
                                                 #_(Duration/ofMillis 200))}}))


(comment
  (alter-var-root #'production component/start)
  (alter-var-root #'production component/stop)
  )

(defmulti register-incomming-action :action)

;; Handlers for resulting dispatch values
(defmethod register-incomming-action :move [{:keys [action user-id] :as m}]
  (swap! incomming-state assoc-in [action user-id] m))
(defmethod register-incomming-action :place-bomb [{:keys [action user-id] :as m}]
  (swap! incomming-state assoc-in [action user-id] m))
(defmethod register-incomming-action :default [m]
 (throw (Exception. (str "In dont know what to do with" m))))

(register-incomming-action
 {:action  :move
  :user-id 1
  :payload {:direction :west}})
@incomming-state
@game-state
(reset! incomming-state {})

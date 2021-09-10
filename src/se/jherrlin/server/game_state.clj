(ns se.jherrlin.server.game-state
  (:require [se.jherrlin.server.event-sourcing.event-store :as event-store]
            [se.jherrlin.server.event-sourcing :as event-sourcing]
            [clojure.string :as str]))

(def initial-game-state {})
(defonce game-state (atom initial-game-state))


(defn urn->qualified-keyword
  "Convert event `source` and `type` to qualified keyword."
  [source type]
  (keyword (str
            (-> source
                (str/replace #"^urn:" "")
                (str/replace #":" "."))
            "/"
            type)))

(defn event->projection-key [{:keys [source type] :as event}]
  (urn->qualified-keyword source type))

(defmulti projection (fn [game-state event] (event->projection-key event)))

(defmethod projection :se.jherrlin.bomberman.game/create-game
  [game-state {:keys [subject data] :as event}]
  (assoc-in game-state [:games subject] data))

(defmethod projection :se.jherrlin.bomberman.game/join-game
  [game-state {:keys [subject data] :as event}]
  (let [players-in-game    (get-in game-state [:games subject :players])
        number-of-players  (count players-in-game)
        player-gets-number (inc number-of-players)]
    (assoc-in game-state
              [:games subject :players (:player-id data)]
              (assoc data :player-nr player-gets-number))))

(defmethod projection :se.jherrlin.bomberman.game/start
  [game-state {:keys [subject data] :as event}]
  (assoc-in game-state [:games subject :game-state] :started))


(defmethod projection :default [game-state event]
  (println "Error! Could not find projection for event:")
  ;; (println game-state)
  (clojure.pprint/pprint event))

(add-watch event-store/store :game-state-projection
           (fn [key atom old-state new-state]
             (when-let [latest-event (-> new-state :events first)]
               (let [old-game-state @game-state
                     new-game-state (projection old-game-state latest-event)]
                 (reset! game-state new-game-state)))))


(comment
  @game-state
  (reset! game-state initial-game-state)
  @event-store/store
  (reset! event-store/store event-store/store-init)

  (def repl-subject #uuid "c03e430f-2b24-4109-a923-08c986a682a8")
  (def player-1-ws-id #uuid "e677bf82-0137-4105-940d-6d74429d31b0" #_(java.util.UUID/randomUUID))
  (def player-2-ws-id #uuid "663bd7a5-7220-40e5-b08d-597c43b89e0a")

  (-> {}
      (projection (event-sourcing/create-game repl-subject "First game" "my-secret"))
      (projection (event-sourcing/join-game   repl-subject player-1-ws-id "John"))
      (projection (event-sourcing/join-game   repl-subject player-2-ws-id "Hannah"))
      (projection (event-sourcing/start-game  repl-subject))
      (projection (event-sourcing/player-move repl-subject ))
      )
  )



(comment
  (urn->qualified-keyword "urn:se:jherrlin:bomberman:game" "start")

  (event->projection-key
   {:id           #uuid "d07fd861-bb4c-4458-9d4a-dd1ac1435893",
    :source       "urn:se:jherrlin:bomberman:game",
    :subject      #uuid "8633f232-8628-4c0a-80d6-bccea44c723e",
    :type         "join-game",
    :time         #inst "2021-09-10T11:34:45.302-00:00",
    :content-type "application/edn",
    :data
    {:player-id   #uuid "78a3c3d4-babf-4524-8fc0-c3576791747e",
     :player-name "Killer"}})

  )

(ns se.jherrlin.server.game-state
  (:require [se.jherrlin.server.event-sourcing.event-store :as event-store]
            [clojure.string :as str]))

(def initial-game-state {})
(defonce game-state (atom initial-game-state))

(comment
  @game-state
  (reset! game-state initial-game-state)
  )

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
  (assoc-in @game-state [:games subject] data))





;; Handlers for resulting dispatch values
(defmethod projection :default [game-state event]
  (println game-state)
  (clojure.pprint/pprint event))


(add-watch event-store/store :game-state-projection
           (fn [key atom old-state new-state]
             (when-let [latest-event (-> new-state :events first)]
               (let [old-game-state @game-state
                     new-game-state (projection old-game-state latest-event)]
                 (reset! game-state new-game-state)))))


(:events @event-store/store)
@game-state

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

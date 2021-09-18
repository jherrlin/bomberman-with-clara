(ns se.jherrlin.server.repl
  (:require
   [se.jherrlin.server.game-state :as game-state]
   [se.jherrlin.server.models :as models]
   [se.jherrlin.clara-labs.board :as board]
   [se.jherrlin.clara-labs.bomberman-rules :as bomberman-rules]
   [clojure.pprint :as pprint]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [se.jherrlin.server.system :as system]
   [clojure.core.async
    :as a
    :refer [>! <! >!! <!! go chan buffer close! thread go-loop put!
            alts! alts!! timeout]]
   [taoensso.timbre :as timbre])
  (:import [java.time Instant Duration]
           [se.jherrlin.server.models
            PlayerMove StoneToRemove FireToRemove BombToRemove BombExploading FireOnBoard PlayerDies BombOnBoard FlyingBomb
            CreateGame JoinGame StartGame EndGame PlayerWantsToPlaceBomb ActiveGame]))

(defn read-edn-file
  "Read file from filesystem and parse it to edn."
  [resource-path]
  (try
    (edn/read-string (slurp (io/resource resource-path)))
    (catch java.io.IOException e
      (printf "Couldn't open '%s': %s\n" resource-path (.getMessage e)))
    (catch Exception e
      (printf "Error parsing edn file '%s': %s\n" resource-path (.getMessage e)))))


(def add-event-fn! (-> system/production :event-store :add-event-fn!))
(def add-events-fn! (-> system/production :event-store :add-events-fn!))
(def game-state' (-> system/production :game-state :game-state))
(def event-store (-> system/production :event-store :store))
(def broadcast-fn! (get-in system/production [:websocket :broadcast-fn!]))

(comment
  @game-state'
  @event-store
  @system/incomming-commands-state
  (reset! game-state' se.jherrlin.server.components.game-state/initial-game-state)
  (reset! event-store se.jherrlin.server.components.event-store/store-init)
  (reset! incomming-commands-state/incomming-commands-state {})
  )


(defn trunc
  [s n]
  (subs s 0 (min (count s) n)))

(defn print-events-table
  "Print event table with newest events on top."
  [events]
  (->> events
       (sort-by :time #(compare %2 %1))
       (reverse)
       (map-indexed vector)
       (reverse)
       (map (fn [[idx {:keys [data] :as event}]]
              (-> event
                  (assoc :nr idx)
                  (dissoc :id :content-type #_:subject)
                  (assoc :data (trunc (str data) 100)))))
       (pprint/print-table)))

(defn rule-actions-to-cloud-events [actions]
  (->> actions
       (vals)
       (apply concat)
       (map #(.toCloudEvent %))))

(->> (read-edn-file "events/2021-09-17_3-players.edn")
     :events
     print-events-table)


(let [game-id        #uuid "65168a85-ef2f-4c59-a08b-e3da88bf7bc5"
      john-player-id #uuid "ceea28f0-4997-47af-8074-69526fdfc374"
      previous-state (->> (read-edn-file "events/2021-09-17_3-players.edn")
                          :events
                          (reverse)
                          (take 31)
                          (game-state/the-projection {}))]
  (->> previous-state
       (game-state/game-state->enginge-facts)
       (concat
        [(models/->TimestampNow (java.util.Date.))
         (models/->PlayerWantsToMove game-id john-player-id [1 1] :south)])
       (bomberman-rules/run-rules)
       :actions
       (rule-actions-to-cloud-events)
       (game-state/the-projection previous-state)))

(->> (read-edn-file "events/2021-09-17_3-players.edn")
     :events
     (reverse)
     (take 31)
     (game-state/the-projection {})
     (game-state/game-state->enginge-facts)
     (map type))
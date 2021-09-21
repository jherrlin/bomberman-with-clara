(ns se.jherrlin.server.repl
  (:require
   [se.jherrlin.server.game-state :as game-state]
   [se.jherrlin.server.models :as models]
   [se.jherrlin.server.resources :as resources]
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
   [taoensso.timbre :as timbre]
   [se.jherrlin.datetime :as datetime]
   [se.jherrlin.server.game-state2 :as game-state2]))


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

  (spit "/tmp/before-games-1.edn" (with-out-str (clojure.pprint/pprint @game-state')))
  )

(comment
  (reset! game-state'
          (->> (resources/read-edn-file "events/danks-alfa-testning-events.edn")
               :events
               (reverse)
               (game-state/the-projection {})))

  (reset! event-store (resources/read-edn-file "events/danks-alfa-testning-events.edn"))
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

(->> (resources/read-edn-file "events/2021-09-17_3-players.edn")
     :events
     print-events-table)


(let [timestamp      (datetime/now)
      game-id        #uuid "65168a85-ef2f-4c59-a08b-e3da88bf7bc5"
      john-player-id #uuid "ceea28f0-4997-47af-8074-69526fdfc374"
      previous-state (->> (resources/read-edn-file "events/2021-09-17_3-players.edn")
                          :events
                          (reverse)
                          (take 31)
                          (game-state/the-projection {}))]
  (->> previous-state
       (game-state2/games-facts)
       (concat
        [(models/->TimestampNow (java.util.Date.))
         (models/->PlayerWantsToMove timestamp game-id john-player-id [1 1] :south)])
       (bomberman-rules/run-rules)
       :actions
       (rule-actions-to-cloud-events)
       (game-state/the-projection previous-state)))

(->> (resources/read-edn-file "events/2021-09-17_3-players.edn")
     :events
     (reverse)
     (take 31)
     (game-state/the-projection {})
     (game-state2/games-facts)
     (map type))

(->> (resources/read-edn-file "events/danks-alfa-testning-events.edn")
     :events
     (reverse)
     (game-state/the-projection {}))

(comment
  (->> @event-store
       :events
       (reverse)
       (game-state/the-projection {})
       )
  )

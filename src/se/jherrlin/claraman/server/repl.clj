(ns se.jherrlin.claraman.server.repl
  (:require
   [se.jherrlin.claraman.game-state :as game-state]
   [se.jherrlin.claraman.models :as models]
   [se.jherrlin.claraman.server.resources :as resources]
   [se.jherrlin.claraman.board :as board]
   [se.jherrlin.claraman.claraman-rules :as claraman-rules]
   [clojure.pprint :as pprint]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [se.jherrlin.claraman.server.system :as system]
   [clojure.core.async
    :as a
    :refer [>! <! >!! <!! go chan buffer close! thread go-loop put!
            alts! alts!! timeout]]
   [taoensso.timbre :as timbre]
   [se.jherrlin.datetime :as datetime]))


(def add-event-fn! (-> system/production :event-store :add-event-fn!))
(def add-events-fn! (-> system/production :event-store :add-events-fn!))
(def game-state' (-> system/production :game-state :game-state))
(def event-store (-> system/production :event-store :store))
(def broadcast-fn! (get-in system/production [:websocket :broadcast-fn!]))
(def event-store (-> system/production :event-store :store))


(defn dump-app-states! [game-name]
  (let [datetime (.format (java.text.SimpleDateFormat. "yyyy-MM-dd_HH-mm") (java.util.Date.))]
       (spit (str "/tmp/" game-name "-" datetime "-game-state.edn") (with-out-str (clojure.pprint/pprint @se.jherrlin.claraman.server.components.game-state/game-state)))
       (spit (str "/tmp/" game-name "-" datetime "-events.edn") (with-out-str (clojure.pprint/pprint @se.jherrlin.claraman.server.components.event-store/store)))
       (spit (str "/tmp/" game-name "-" datetime "-incomming-commands.edn") (with-out-str (clojure.pprint/pprint @se.jherrlin.claraman.server.system/incomming-commands-state)))))

(comment
  (dump-app-states! "test")
  )

(comment
  @game-state'
  @event-store
  @system/incomming-commands-state
  (reset! game-state' se.jherrlin.claraman.server.components.game-state/initial-game-state)
  (reset! event-store se.jherrlin.claraman.server.components.event-store/store-init)
  (reset! incomming-commands-state/incomming-commands-state {})

  (spit "/tmp/kamrat-game-state.edn" (with-out-str (clojure.pprint/pprint @game-state')))
  )

(comment
  (reset! game-state'
          (->> (resources/read-edn-file "events/danks-alfa-testning-events.edn")
               :events
               (reverse)
               (game-state/the-projection {})))

  (reset! event-store (resources/read-edn-file "events/danks-alfa-testning-events.edn"))


  (reset! game-state' (resources/read-edn-file "events/kamrat-2021-09-23/kamrat-test-2021-09-23_15-29-game-state.edn"))
  (reset! event-store (resources/read-edn-file "events/kamrat-2021-09-23/kamrat-test-2021-09-23_15-29-events.edn"))
  (reset! system/incomming-commands-state
          (resources/read-edn-file "events/kamrat-2021-09-23/kamrat-test-2021-09-23_15-29-incomming-commands.edn"))

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

(comment
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
       (game-state/games-facts)
       (concat
        [(models/->TimestampNow (java.util.Date.))
         (models/->PlayerWantsToMove timestamp game-id john-player-id [1 1] :south)])
       (claraman-rules/run-rules)
       :actions
       (rule-actions-to-cloud-events)
       (game-state/the-projection previous-state)))

(->> (resources/read-edn-file "events/2021-09-17_3-players.edn")
     :events
     (reverse)
     (take 31)
     (game-state/the-projection {})
     (game-state/games-facts)
     (map type))

(->> (resources/read-edn-file "events/danks-alfa-testning-events.edn")
     :events
     (reverse)
     (game-state/the-projection {}))

  )

(comment
  (->> @event-store
       :events
       (reverse)
       (game-state/the-projection {})
       )
  )

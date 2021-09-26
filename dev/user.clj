(ns user
  (:require [se.jherrlin.claraman.server.system :as system]
            [se.jherrlin.claraman.game-state :as game-state]
            [se.jherrlin.claraman.claraman-rules :as claraman-rules]
            [se.jherrlin.claraman.user-commands :as user-commands]
            [se.jherrlin.claraman.server.components.game-state :as components.game-state]
            [se.jherrlin.claraman.server.components.event-store :as components.event-store]
            [se.jherrlin.datetime :as datetime]
            [clojure.core.async :as a :refer [<! go-loop timeout]]
            [se.jherrlin.claraman.models :as models]
            [com.stuartsierra.component :as component]))


(comment
  (def add-events-fn! (-> system/production :event-store :add-events-fn!))
  (def game-state'     (-> system/production :game-state :game-state))
  (def event-store    (-> system/production :event-store :store))
  (def broadcast-fn!  (get-in system/production [:websocket :broadcast-fn!]))

  (alter-var-root #'system/production component/start)
  (alter-var-root #'system/production component/stop)

  system/production

  (->> @event-store
       :events
       (reverse)
       (take 198)
       (reduce game-state/projection {})
       )

  (def facts-with-record-annotation
    (with-in-str (slurp "/tmp/facts-to-analyze.edn")
      (read)))

  (claraman-rules/run-rules facts-with-record-annotation)

  (reset! game-state' components.game-state/initial-game-state)
  (reset! event-store components.event-store/store-init)
  (reset! system/incomming-commands-state {})

  (java.util.UUID/randomUUID)

  (claraman-rules/run-rules
   (concat
    (system/incomming-actions system/incomming-commands-state game-state')
    (game-state/games-facts @game-state')
    [(models/->TimestampNow (java.util.Date.))]))

  (claraman-rules/run-rules
   (game-state/games-facts
    (game-state/the-projection {} (->> @event-store :events reverse))
    ))

  )


(comment
  ;; Bots
  (let [game-id #uuid "246e1ee5-48d3-47e4-b9fe-c66e648439a0"]
    (map (fn [{:keys [bot-id bot-name]}]
           (add-events-fn! [(.toCloudEvent (models/->JoinGame (java.util.Date.) game-id bot-id bot-name))]))
         [{:bot-id   #uuid "e24b0220-b98d-4319-8991-9c634da7027c"
           :bot-name "Bot 1"}
          {:bot-id   #uuid "ebc270ae-62fe-42de-90ec-a6b3875eb56e"
           :bot-name "Bot 2"}
          {:bot-id   #uuid "a9d89612-cd08-46ab-8303-89918a633193"
           :bot-name "Bot 3"}]))

  (def run-bot-commands? (atom true))
  (go-loop []
    (let [game-id #uuid "246e1ee5-48d3-47e4-b9fe-c66e648439a0"]
      (doall
       (map (fn [{:keys [bot-id]}]
              (user-commands/register-incomming-user-command!
               system/incomming-commands-state
               (assoc (user-commands/generate-bot-action game-id bot-id)
                      :timestamp (datetime/now))))
            [{:bot-id   #uuid "e24b0220-b98d-4319-8991-9c634da7027c"
              :bot-name "Bot 1"}
             {:bot-id   #uuid "ebc270ae-62fe-42de-90ec-a6b3875eb56e"
              :bot-name "Bot 2"}
             {:bot-id   #uuid "a9d89612-cd08-46ab-8303-89918a633193"
              :bot-name "Bot 3"}])))
    (when @run-bot-commands?
      (<! (timeout 100))
      (recur)))
  (reset! run-bot-commands? false)

  )


(comment
  (java.util.UUID/randomUUID)
  (def repl-subject "JOHN-HANNAS-game")
  (def player-1-id "johns-id")
  (def player-2-id "hannahs-id")


  (user-commands/register-incomming-user-command!
   system/incomming-commands-state
   {:game-id   repl-subject
    :user-id   player-1-id
    :action    :move
    :direction :east})

  (user-commands/register-incomming-user-command!
   system/incomming-commands-state
   {:game-id   repl-subject
    :user-id   player-1-id
    :action    :move
    :direction :west})

  (user-commands/register-incomming-user-command!
   system/incomming-commands-state
   {:game-id repl-subject
    :action  :place-bomb
    :user-id player-1-id})

  (user-commands/register-incomming-user-command!
   system/incomming-commands-state
   {:game-id repl-subject
    :action  :throw-bomb
    :user-id player-1-id})

  )


(comment
  (def run-loop? (atom true))
  (go-loop []
    (time
     (let [game-state          game-state'
           user-action-facts   (system/incomming-actions system/incomming-commands-state game-state)
           _                   (def user-action-facts user-action-facts)
           game-state-facts    (game-state/games-facts @game-state)
           _                   (def game-state-facts game-state-facts)
           rule-enginge-facts  (concat
                                user-action-facts
                                game-state-facts
                                [(models/->TimestampNow (java.util.Date.))])
           _                   (def rule-enginge-facts rule-enginge-facts)
           actions-from-enging (claraman-rules/run-rules rule-enginge-facts)
           _                   (def actions-from-enging actions-from-enging)
           _                   (def the-sorted (system/to-cloud-events (system/sort-events actions-from-enging)))]
       (add-events-fn! the-sorted)
       ;; (reset! game-state (game-state/the-projection @game-state' the-sorted))
       (reset! system/incomming-commands-state {})
       @game-state'))

    (when @run-loop?
      (<! (timeout 500))
      (recur)))
  (reset! run-loop? false)

  (system/game-loop (java.util.Date.) game-state' system/incomming-commands-state broadcast-fn! add-events-fn!)

  )

(ns se.jherrlin.server.game-state
  (:require [se.jherrlin.server.events :as events]
            [clojure.string :as str]))

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
  (assoc-in game-state [:games subject] (assoc data :subject subject)))

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

(defmethod projection :se.jherrlin.bomberman.player/move
  [game-state {:keys [subject data] :as event}]
  (let [{:keys [direction user-id]} data]
    (assoc-in game-state [:games subject :players user-id :user-facing-direction] direction)))

(defmethod projection :default [game-state event]
  (println "Error! Could not find projection for event:")
  ;; (println game-state)
  (clojure.pprint/pprint event)
  game-state)


(comment
  (java.util.UUID/randomUUID)
  (def repl-subject #uuid "c03e430f-2b24-4109-a923-08c986a682a8")
  (def player-1-ws-id #uuid "e677bf82-0137-4105-940d-6d74429d31b0")
  (def player-2-ws-id #uuid "663bd7a5-7220-40e5-b08d-597c43b89e0a")

  (-> {}
      (projection (events/create-game repl-subject "First game" "my-secret"))
      (projection (events/join-game   repl-subject player-1-ws-id "John"))
      (projection (events/join-game   repl-subject player-2-ws-id "Hannah"))
      (projection (events/start-game  repl-subject))
      (projection (events/player-move repl-subject ))
      )
  )

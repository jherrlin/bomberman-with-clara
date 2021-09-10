(ns se.jherrlin.server.application-service
  (:require [se.jherrlin.server.events :as events]))



(defn create-game [add-event-fn! game-name password]
  (let [subject (java.util.UUID/randomUUID)]
    (add-event-fn! (events/create-game subject game-name password))))

(defn join-game [add-event-fn! subject player-id player-name]
  (add-event-fn! (events/join-game subject player-id player-name)))

(ns se.jherrlin.server.application-service
  (:require [se.jherrlin.server.event-sourcing :as event-sourcing]))



(defn create-game [add-event-fn! game-name password]
  (let [subject (java.util.UUID/randomUUID)]
    (add-event-fn! (event-sourcing/create-game subject game-name password))))

(defn join-game [add-event-fn! subject player-id player-name]
  (add-event-fn! (event-sourcing/join-game subject player-id player-name)))

(ns se.jherrlin.server.event-sourcing
  (:require [se.jherrlin.server.event-sourcing.create-events :as create-events]))


(defn create-game [board game-name init-game-state password]
  (create-events/create-game
   {:data
    {:game-name           game-name
     :join-password       password
     :board               board
     :initial-board-state init-game-state}}))

(defn join-game [subject player-name]
  (create-events/join-game
   {:subject subject
    :data    {:player-id   (java.util.UUID/randomUUID)
              :player-name player-name}}))

(defn start-game [subject]
  (create-events/start-game
   {:subject subject}))

(defn player-action [subject action]
  (create-events/player-action
   {:subject subject
    :data    action}))

(defn end-game [subject winner]
  (create-events/end-game
   {:subject subject
    :data    {:winner winner}}))




(comment
  (create-game
   [{:x 0 :y 0 :type :floor}]
   "First game"
   {:stones [[1 2] [1 3] [1 4]]}
   "my-secret")

  {:id           #uuid "b3853d20-cc86-4d3d-96c7-594ab7c1de7e",
   :source       "urn:se:jherrlin:bomberman:game",
   :subject      #uuid "d26460aa-7f4a-48c1-8bfa-c6dc2f37006a",
   :type         "create-game",
   :time         #inst "2021-09-10T08:16:54.066-00:00",
   :content-type "application/edn",
   :data
   {:game-name           "First game",
    :join-password       "my-secret",
    :board               [{:x 0, :y 0, :type :floor}],
    :initial-board-state {:stones [[1 2] [1 3] [1 4]]}}}

  (join-game
   #uuid "8633f232-8628-4c0a-80d6-bccea44c723e"
   "Killer")

  {:id           #uuid "dd38d58f-1a2c-4ac2-b9b0-781e64338ab2",
   :source       "urn:se:jherrlin:bomberman:game",
   :subject      #uuid "8633f232-8628-4c0a-80d6-bccea44c723e",
   :type         "join-game",
   :time         #inst "2021-09-10T08:10:57.830-00:00",
   :content-type "application/edn",
   :data
   {:player-id   #uuid "cafd9e3b-9598-4ff9-8d88-5838d4af77d0",
    :player-name "Killer"}}

  (start-game
   #uuid "8633f232-8628-4c0a-80d6-bccea44c723e")

  {:id           #uuid "49d1d65a-67e2-4ea8-b338-f5a431827097",
   :source       "urn:se:jherrlin:bomberman:game",
   :subject      #uuid "8633f232-8628-4c0a-80d6-bccea44c723e",
   :type         "start-game",
   :time         #inst "2021-09-10T08:11:29.559-00:00",
   :content-type "application/edn"}

  (player-action
   #uuid "8633f232-8628-4c0a-80d6-bccea44c723e"
   {:action  :move
    :user-id 1
    :payload {:direction :east}})

  {:id           #uuid "11a6d87f-5f9b-4287-98b4-f3d13434717c",
   :source       "urn:se:jherrlin:bomberman:player",
   :subject      #uuid "8633f232-8628-4c0a-80d6-bccea44c723e",
   :type         "player-action",
   :time         #inst "2021-09-10T08:12:49.273-00:00",
   :content-type "application/edn",
   :data         {:action :move, :user-id 1, :payload {:direction :east}}}

  (player-action
   #uuid "8633f232-8628-4c0a-80d6-bccea44c723e"
   {:action  :place-bomb
    :user-id 1})

  {:id           #uuid "050b9a73-9c03-4c75-8d10-b8a2d8b9f4bf",
   :source       "urn:se:jherrlin:bomberman:player",
   :subject      #uuid "8633f232-8628-4c0a-80d6-bccea44c723e",
   :type         "player-action",
   :time         #inst "2021-09-10T08:15:07.737-00:00",
   :content-type "application/edn",
   :data         {:action :place-bomb, :user-id 1}}

  (player-action
   #uuid "8633f232-8628-4c0a-80d6-bccea44c723e"
   {:action  :throw-bomb
    :user-id 1})

  (end-game
   #uuid "8633f232-8628-4c0a-80d6-bccea44c723e"
   "Killer")

  {:id           #uuid "43eef471-caf3-4446-b52b-27eece3d0381",
   :source       "urn:se:jherrlin:bomberman:game",
   :subject      #uuid "8633f232-8628-4c0a-80d6-bccea44c723e",
   :type         "end-game",
   :time         #inst "2021-09-10T08:13:23.815-00:00",
   :content-type "application/edn",
   :data         {:winner "Killer"}}
  )

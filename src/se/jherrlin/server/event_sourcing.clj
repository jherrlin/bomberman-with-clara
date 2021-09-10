(ns se.jherrlin.server.event-sourcing
  (:require [se.jherrlin.server.event-sourcing.event-store :as event-store]
            [se.jherrlin.clara-labs.board :as board]))

(comment
  (remove-ns 'se.jherrlin.server.event-sourcing)
  )

(defn- template
  ([source subject type]
   (template source subject type nil))
  ([source subject type data]
   (cond-> {:id           (java.util.UUID/randomUUID) ;; event id
            :source       source
            :subject      subject
            :type         type
            :time         (java.util.Date.)
            :content-type "application/edn"}
     data (assoc :data data))))

(defn create-game [subject game-name password]
  (template
   "urn:se:jherrlin:bomberman:game"
   subject
   "create-game"
   {:game-state    :created
    :game-name     game-name
    :game-password password
    :board         #_board/board2 (board/init 6)
    :stones        [[1 2] [3 3] [4 4] [4 5] [5 5] [5 6] [6 7] [7 8] [8 9]]}))

(defn join-game [subject player-id player-name]
  (template
   "urn:se:jherrlin:bomberman:game"
   subject
   "join-game"
   {:player-id                player-id
    :player-name              player-name
    :user-facing-direction    :south
    :max-nr-of-bombs-for-user 3
    :fire-length              3}))

(defn start-game [subject]
  (template
   "urn:se:jherrlin:bomberman:game"
   subject
   "start"))

(defn end-game [subject winner]
  (template
   "urn:se:jherrlin:bomberman:game"
   subject
   "end"
   {:winner winner}))

(defn player-move [subject user-id direction]
  (template
   "urn:se:jherrlin:bomberman:player"
   subject
   "move"
   {:user-id   user-id
    :direction direction}))

(defn player-place-bomb [subject user-id]
  (template
   "urn:se:jherrlin:bomberman:player"
   subject
   "place-bomb"
   {:user-id user-id}))

(defn player-throw-bomb [subject user-id]
  (template
   "urn:se:jherrlin:bomberman:player"
   subject
   "throw-bomb"
   {:user-id user-id}))



(comment

  (def repl-subject #uuid "c03e430f-2b24-4109-a923-08c986a682a8")

  (event-store/add-event!
   (create-game repl-subject "First game" "my-secret"))

  (event-store/add-event!
   (join-game
    repl-subject
    (java.util.UUID/randomUUID) ;; event id
    "Killer"))

  (event-store/add-event!
   (join-game
    #uuid "218efd63-3ac5-47b4-9490-c2b77ac728ba"
    (java.util.UUID/randomUUID) ;; event id
    "Hitman"))

  (event-store/add-event!
   (start-game
    #uuid "8633f232-8628-4c0a-80d6-bccea44c723e"))

  {:id           #uuid "49d1d65a-67e2-4ea8-b338-f5a431827097",
   :source       "urn:se:jherrlin:bomberman:game",
   :subject      #uuid "8633f232-8628-4c0a-80d6-bccea44c723e",
   :type         "start",
   :time         #inst "2021-09-10T08:11:29.559-00:00",
   :content-type "application/edn"}

  (event-store/add-event!
   (player-action
    #uuid "8633f232-8628-4c0a-80d6-bccea44c723e"
    {:action  :move
     :user-id 1
     :payload {:direction :east}}))

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
   :type         "end",
   :time         #inst "2021-09-10T08:13:23.815-00:00",
   :content-type "application/edn",
   :data         {:winner "Killer"}}
  )

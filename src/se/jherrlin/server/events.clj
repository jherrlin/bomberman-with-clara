(ns se.jherrlin.server.events
  (:require [se.jherrlin.clara-labs.board :as board]))

(comment
  (remove-ns 'se.jherrlin.server.events)
  )

(defn template
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
    :stones        [[1 2] [3 3] [4 4] [4 5] [5 5] [5 6] [6 7] [7 8] [8 9]]
    :fire         '()
    :flying-bombs '()
    :bombs        '()}))

(defn join-game [subject player-id player-name]
  (template
   "urn:se:jherrlin:bomberman:game"
   subject
   "join-game"
   {:player-id                player-id
    :player-name              player-name
    :user-facing-direction    :south
    :max-nr-of-bombs-for-user 3
    :position                 [1 1]
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

(defn player-wants-to-move [subject user-id direction]
  (template
   "urn:se:jherrlin:bomberman:player"
   subject
   "wants-to-move-move"
   {:user-id   user-id
    :direction direction}))

(defn player-wants-to-place-bomb [subject user-id]
  (template
   "urn:se:jherrlin:bomberman:player"
   subject
   "wants-to-place-bomb"
   {:user-id user-id}))

(defn player-wants-to-throw-bomb [subject user-id]
  (template
   "urn:se:jherrlin:bomberman:player"
   subject
   "wants-to-throw-bomb"
   {:user-id user-id}))

(defn engine-action-move [subject user-id next-position direction]
  (template
   "urn:se:jherrlin:bomberman:rule-engine-action"
   subject
   "move"
   {:user-id       user-id
    :next-position next-position
    :direction     direction}))

(defn engine-action-place-bomb [subject user-id]
  (template
   "urn:se:jherrlin:bomberman:rule-engine-action"
   subject
   "place-bomb"
   {:user-id user-id}))

(defn engine-action-throw-bomb [subject user-id]
  (template
   "urn:se:jherrlin:bomberman:rule-engine-action"
   subject
   "throw-bomb"
   {:user-id user-id}))

(comment
  (def repl-subject #uuid "c03e430f-2b24-4109-a923-08c986a682a8")

  )

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

(defn player-wants-to-move [subject player-id direction]
  (template
   "urn:se:jherrlin:bomberman:player"
   subject
   "wants-to-move"
   {:player-id player-id
    :direction direction}))

(defn player-wants-to-place-bomb [subject player-id]
  (template
   "urn:se:jherrlin:bomberman:player"
   subject
   "wants-to-place-bomb"
   {:player-id player-id}))

(defn player-wants-to-throw-bomb [subject player-id]
  (template
   "urn:se:jherrlin:bomberman:player"
   subject
   "wants-to-throw-bomb"
   {:player-id player-id}))

(defn player-move [subject player-id next-position direction]
  (template
   "urn:se:jherrlin:bomberman:player"
   subject
   "move"
   {:player-id     player-id
    :next-position next-position
    :direction     direction}))

(defn bomb-exploading [subject player-id position-xy fire-length]
  (template
   "urn:se:jherrlin:bomberman:game"
   subject
   "bomb-exploading"
   {:player-id   player-id
    :position-xy position-xy
    :fire-length fire-length}))

(defn stone-to-remove [subject position-xy]
  (template
   "urn:se:jherrlin:bomberman:game"
   subject
   "stone-to-remove"
   {:position-xy position-xy}))

(defn bomb-to-remove [subject position-xy]
  (template
   "urn:se:jherrlin:bomberman:game"
   subject
   "bomb-to-remove"
   {:position-xy position-xy}))

(defn bomb-on-board [subject player-id bomb-position-xy fire-length bomb-added-timestamp]
  (template
   "urn:se:jherrlin:bomberman:game"
   subject
   "bomb-on-board"
   {:player-id            player-id
    :bomb-position-xy     bomb-position-xy
    :fire-length          fire-length
    :bomb-added-timestamp bomb-added-timestamp}))

(defn fire-on-board [subject player-id fire-position-xy fire-start-timestamp]
  (template
   "urn:se:jherrlin:bomberman:game"
   subject
   "fire-on-board"
   {:player-id            player-id
    :fire-position-xy     fire-position-xy
    :fire-start-timestamp fire-start-timestamp}))

(defn fire-to-remove [subject position-xy]
  (template
   "urn:se:jherrlin:bomberman:game"
   subject
   "fire-to-remove"
   {:position-xy position-xy}))





(defn engine-action-place-bomb [subject player-id]
  (template
   "urn:se:jherrlin:bomberman:rule-engine-action"
   subject
   "place-bomb"
   {:player-id player-id}))

(defn engine-action-throw-bomb [subject player-id]
  (template
   "urn:se:jherrlin:bomberman:rule-engine-action"
   subject
   "throw-bomb"
   {:player-id player-id}))

(comment
  (def repl-subject #uuid "c03e430f-2b24-4109-a923-08c986a682a8")

  )

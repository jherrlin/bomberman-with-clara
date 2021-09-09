(ns se.jherrlin.server.event-sourcing.create-events)


(defn create-game [{:keys [board game-name init-game-state password]}]
  {:id           (java.util.UUID/randomUUID) ;; event id
   :source       "urn:se:jherrlin:bomberman:game"
   :subject      (java.util.UUID/randomUUID) ;; game id
   :type         "create-game"
   :time         (java.util.Date.)
   :content-type "application/edn"
   :data         {:game-name           game-name
                  :join-password       password
                  :board               board
                  :initial-board-state init-game-state}})

(defn join-game [{:keys [subject player-name]}]
  {:id           (java.util.UUID/randomUUID) ;; event id
   :source       "urn:se:jherrlin:bomberman:game"
   :subject      subject                     ;; game id
   :type         "join-game"
   :time         (java.util.Date.)
   :content-type "application/edn"
   :data         {:player-id   (java.util.UUID/randomUUID)
                  :player-name player-name}})

(defn start-game [{:keys [subject]}]
  {:id           (java.util.UUID/randomUUID) ;; event id
   :source       "urn:se:jherrlin:bomberman:game"
   :subject      subject                     ;; game id
   :type         "start-game"
   :time         (java.util.Date.)
   :content-type "application/edn"})

(defn user-action [{:keys [subject action]}]
  {:id           (java.util.UUID/randomUUID) ;; event id
   :source       "urn:se:jherrlin:bomberman:player"
   :subject      subject                     ;; game id
   :type         "user-action"
   :time         (java.util.Date.)
   :content-type "application/edn"
   :data         action})

(defn end-game [{:keys [subject data]}]
  {:id           (java.util.UUID/randomUUID) ;; event id
   :source       "urn:se:jherrlin:bomberman:game"
   :subject      subject                     ;; game id
   :type         "end-game"
   :time         (java.util.Date.)
   :content-type "application/edn"
   :data         data})

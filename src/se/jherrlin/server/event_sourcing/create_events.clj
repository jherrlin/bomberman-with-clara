(ns se.jherrlin.server.event-sourcing.create-events)


(defn create-game [{:keys [data]}]
  {:id           (java.util.UUID/randomUUID) ;; event id
   :source       "urn:se:jherrlin:bomberman:game"
   :subject      (java.util.UUID/randomUUID) ;; game id
   :type         "create-game"
   :time         (java.util.Date.)
   :content-type "application/edn"
   :data         data})

(defn join-game [{:keys [subject data]}]
  {:id           (java.util.UUID/randomUUID) ;; event id
   :source       "urn:se:jherrlin:bomberman:game"
   :subject      subject                     ;; game id
   :type         "join-game"
   :time         (java.util.Date.)
   :content-type "application/edn"
   :data         data})

(defn start-game [{:keys [subject]}]
  {:id           (java.util.UUID/randomUUID) ;; event id
   :source       "urn:se:jherrlin:bomberman:game"
   :subject      subject                     ;; game id
   :type         "start-game"
   :time         (java.util.Date.)
   :content-type "application/edn"})

(defn user-action [{:keys [subject data]}]
  {:id           (java.util.UUID/randomUUID) ;; event id
   :source       "urn:se:jherrlin:bomberman:player"
   :subject      subject                     ;; game id
   :type         "user-action"
   :time         (java.util.Date.)
   :content-type "application/edn"
   :data         data})

(defn end-game [{:keys [subject data]}]
  {:id           (java.util.UUID/randomUUID) ;; event id
   :source       "urn:se:jherrlin:bomberman:game"
   :subject      subject                     ;; game id
   :type         "end-game"
   :time         (java.util.Date.)
   :content-type "application/edn"
   :data         data})

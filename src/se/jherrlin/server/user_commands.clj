(ns se.jherrlin.server.user-commands
  (:require [clojure.spec.alpha :as s]
            [taoensso.timbre :as timbre]))


(s/def ::game-id    (s/or :s string? :u uuid?))
(s/def ::action     #{:place-bomb :move :throw-bomb})
(s/def ::user-id    (s/or :n number? :u uuid? :s string?))
(s/def ::direction  #{:west :east :north :south})
(s/def ::move       (s/keys :req-un [::game-id ::action ::user-id ::direction]))
(s/def ::place-bomb (s/keys :req-un [::game-id ::action ::user-id]))
(s/def ::commands
  (s/or :move       ::move
        :place-bomb ::place-bomb))

(defmulti register-incomming-user-command!
  (fn [incomming-commands-state command]
    (if (s/valid? ::commands command)
      (:action command)
      (timbre/error "Command dont confirm to spec: " command))))

(defmethod register-incomming-user-command! :move [incomming-commands-state {:keys [game-id action user-id] :as m}]
  (swap! incomming-commands-state assoc-in [game-id user-id action] m))

(defmethod register-incomming-user-command! :place-bomb [incomming-commands-state {:keys [game-id action user-id] :as m}]
  (swap! incomming-commands-state assoc-in [game-id user-id action] m))

(defmethod register-incomming-user-command! :throw-bomb [incomming-commands-state {:keys [game-id action user-id] :as m}]
  (swap! incomming-commands-state assoc-in [game-id user-id action] m))

(defmethod register-incomming-user-command! :default [incomming-commands-state m]
  (throw (Exception. (str "In dont know what to do with" m))))


(comment
  (s/valid? ::move
            {:game-id   (java.util.UUID/randomUUID)
             :user-id   1
             :action    :move
             :direction :east})

  (s/valid? ::move
            {:game-id   (java.util.UUID/randomUUID)
             :action    :move1
             :user-id   1
             :direction :east})

  (s/valid? ::commands
            {:game-id "ett UUID"
             :action  :throw-bomb
             :user-id 1})

  (s/valid? ::commands
            {:game-id (java.util.UUID/randomUUID)
             :action  :place-bomb
             :user-id 1})
  )

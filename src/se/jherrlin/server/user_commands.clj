(ns se.jherrlin.server.user-commands
  (:require [clojure.spec.alpha :as s]))

(defmulti register-incomming-user-action! (fn [incomming-actions-state command] (:action command)))

(defmethod register-incomming-user-action! :move [incomming-actions-state {:keys [game-id action user-id] :as m}]
  (swap! incomming-actions-state assoc-in [game-id action user-id] m))

(defmethod register-incomming-user-action! :place-bomb [incomming-actions-state {:keys [game-id action user-id] :as m}]
  (swap! incomming-actions-state assoc-in [game-id action user-id] m))

(defmethod register-incomming-user-action! :throw-bomb [incomming-actions-state {:keys [game-id action user-id] :as m}]
  (swap! incomming-actions-state assoc-in [game-id action user-id] m))

(defmethod register-incomming-user-action! :default [incomming-actions-state m]
  (throw (Exception. (str "In dont know what to do with" m))))


(s/def ::game-id    (s/or :s string? :u uuid?))
(s/def ::action     #{:place-bomb :move :throw-bomb})
(s/def ::user-id    number?)
(s/def ::direction  #{:west :east :north :south})
(s/def ::move       (s/keys :req-un [::game-id ::action ::user-id ::direction]))
(s/def ::place-bomb (s/keys :req-un [::game-id ::action ::user-id]))
(s/def ::actions
  (s/or :move       ::move
        :place-bomb ::place-bomb))

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

  (s/valid? ::actions
            {:game-id "ett UUID"
             :action  :throw-bomb
             :user-id 1})

  (s/valid? ::actions
            {:game-id (java.util.UUID/randomUUID)
             :action  :place-bomb
             :user-id 1})
  )

(ns se.jherrlin.server.user-commands
  (:require [clojure.spec.alpha :as s]
            [taoensso.timbre :as timbre]
            [clojure.string :as str]))

(s/def ::non-blank-string (s/and string? (complement str/blank?)))

(s/def ::game-id    (s/or :s string? :u uuid?))
(s/def ::action     #{:place-bomb :move :throw-bomb :create-game :join-game :start-game})
(s/def ::user-id    (s/or :n number? :u uuid? :s string?))
(s/def ::direction  #{:west :east :north :south})
(s/def ::game-name  ::non-blank-string)
(s/def ::game-password ::non-blank-string)
(s/def ::player-name ::non-blank-string)
(s/def ::start-game (s/keys :req-un  [::action ::game-id]))
(s/def ::create-game (s/keys :req-un [::action ::game-name ::game-password]))
(s/def ::join-game  (s/keys :req-un  [::action ::game-name ::game-password ::player-name]))
(s/def ::move       (s/keys :req-un  [::action ::game-id ::user-id ::direction]))
(s/def ::place-bomb (s/keys :req-un  [::action ::game-id ::user-id]))
(s/def ::commands
  (s/or :move       ::move
        :place-bomb ::place-bomb
        :create     ::create-game
        :join       ::join-game
        :start      ::start-game))

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
  (timbre/error "In dont know what to do with" m))


(comment

  (s/valid? ::start-game
            {:action  :start-game
             :game-id "1"})

  (s/valid? ::join-game
            {:action        :join-game
             :game-name     "asd"
             :game-password "pwd"
             :player-name   "John"})

  (s/valid? ::commands
            {:action        :create-game
             :game-name     "asd"
             :game-password "pwd"})

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

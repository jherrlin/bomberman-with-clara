(ns se.jherrlin.server.user-commands
  (:require [clojure.spec.alpha :as s]))

(defmulti register-incomming-user-action! (fn [incomming-actions-state action-payload] (:action action-payload)))

(defmethod register-incomming-user-action! :move [incomming-actions-state {:keys [action user-id] :as m}]
  (swap! incomming-actions-state assoc-in [action user-id] m))

(defmethod register-incomming-user-action! :place-bomb [incomming-actions-state {:keys [action user-id] :as m}]
  (swap! incomming-actions-state assoc-in [action user-id] m))

(defmethod register-incomming-user-action! :throw-bomb [incomming-actions-state {:keys [action user-id] :as m}]
  (swap! incomming-actions-state assoc-in [action user-id] m))

(defmethod register-incomming-user-action! :default [incomming-actions-state m]
  (throw (Exception. (str "In dont know what to do with" m))))



(s/def ::action     #{:place-bomb :move :throw-bomb})
(s/def ::user-id    number?)
(s/def ::direction  #{:west :east :north :south})
(s/def ::payload    (s/keys :req-un [::direction]))
(s/def ::move       (s/keys :req-un [::action ::user-id ::payload]))
(s/def ::place-bomb (s/keys :req-un [::action ::user-id]))
(s/def ::actions
  (s/or :move       ::move
        :place-bomb ::place-bomb))

(comment
  (s/valid? ::move
            {:action  :move
             :user-id 1
             :payload {:direction :east}})
  (s/valid? ::move
            {:action  :move
             :user-id 1
             :payload {:direction :east}})
  (s/valid? ::actions
            {:action  :move
             :user-id 1})
  (s/valid? ::move
            {:action  :move1
             :user-id 1
             :payload {:direction :east}})

  (s/valid? ::actions
            {:action  :throw-bomb
             :user-id 1})
  )

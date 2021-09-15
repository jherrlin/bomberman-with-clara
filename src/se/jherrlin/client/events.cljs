(ns se.jherrlin.client.events
  (:require [re-frame.core :as re-frame]))


(re-frame/reg-event-db
 ::initialize-db
 (fn [db _] {}))

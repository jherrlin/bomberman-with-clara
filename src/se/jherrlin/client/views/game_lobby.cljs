(ns se.jherrlin.client.views.game-lobby
  (:require [reitit.coercion.schema]
            [schema.core :as s]))

(defn view []
  [:div "lobby for:"]
  )


(defn routes []
  [["/game/lobby/:game-id"
    {:name        ::view
     :view        [#'view]
     :controllers [{:parameters {:path [:game-id]}
                    :start
                    (fn [{{:keys [game-id]} :path :as a}]
                      (def a a)
                      (let [game-id (uuid game-id)]
                        (def game-id game-id))
                      #_(re-frame/dispatch [:form-value ::create-game :action :create-game]))
                    :stop
                    (fn [_]
                      (println "leaving" ::view))}]}]])

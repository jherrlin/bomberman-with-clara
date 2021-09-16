(ns se.jherrlin.client.views.game-lobby
  (:require [reitit.coercion.schema]
            [re-frame.core :as re-frame]
            [schema.core :as s]))



(defn view []
  (let [listen-to-game-id @(re-frame/subscribe [:listen-to-game-id])
        game-state        @(re-frame/subscribe [:game-state])
        player            @(re-frame/subscribe [:player])
        screen               @(re-frame/subscribe [:screen])]

    [:<>
     [:div (str "lobby for:" listen-to-game-id)]
     [:div "Players:"]
     (for [{:keys [player-name player-nr player-id]} (->> game-state
                                                          :players
                                                          (vals))]
       ^{:key (str player-name "/" player-id)}
       [:div (str player-nr " | " player-name)])
     (for [[i row]  (map-indexed list screen)
           [j cell] (map-indexed list row)]
       ^{:key (str cell)}
       [:<>
        [:div {:style {:width   "20px"
                       :height  "20px"
                       :display "inline-block"}}
         (:str cell)]
        (when (= (-> screen first count dec) j)
          [:div {:style {:display "block"}}])])]))


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

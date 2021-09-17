(ns se.jherrlin.client.views.game-lobby
  (:require [reitit.coercion.schema]
            [re-frame.core :as re-frame]
            [se.jherrlin.client.views.game-play :as game-play]
            ["semantic-ui-react" :as semantic-ui]
            [schema.core :as s]))


(re-frame/reg-event-fx
 ::start-game
 (fn [_ [_ game-id]]
   (def game-id' game-id)
   {:ws-send {:data       [:game/start {:game-id game-id}]
              :on-success (fn [{:keys [status data message] :as m}]
                            (def m m)
                            (if (= status :error)
                              (js/alert message)
                              (do
                                (re-frame/dispatch [:push-state ::game-play/view {:game-id (:game-id data)}]))))}}))

(defn view []
  (let [listen-to-game-id                       @(re-frame/subscribe [:listen-to-game-id])
        game-state                              @(re-frame/subscribe [:game-state])
        player                                  @(re-frame/subscribe [:player])
        screen                                  @(re-frame/subscribe [:screen])
        {:keys [start-game-errors start-games]} @(re-frame/subscribe [:start-game])]
    (when (and listen-to-game-id
               (-> game-state :game-state (= :started))
               player)
      (re-frame/dispatch [:push-state ::game-play/view {:game-id listen-to-game-id}]))
    (when start-game-errors
      [:<>
       [:div (str "lobby for:" listen-to-game-id)]
       (if (seq start-game-errors)
         [:div (-> start-game-errors first :message)]
         [:div
          [:div "Enough players have joined. Game can be started!"]
          [:> semantic-ui/Form.Button
           {:onClick #(re-frame/dispatch [::start-game listen-to-game-id])}
           "Start game!"]])
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
            [:div {:style {:display "block"}}])])])))

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

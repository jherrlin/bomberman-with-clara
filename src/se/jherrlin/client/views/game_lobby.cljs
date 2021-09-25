(ns se.jherrlin.client.views.game-lobby
  (:require [reitit.coercion.schema]
            [re-frame.core :as re-frame]
            [se.jherrlin.client.common :as client.common]
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
  (let [listen-to-game-id                           @(re-frame/subscribe [:listen-to-game-id])
        {:keys [game-name password] :as game-state} @(re-frame/subscribe [:game-state])
        player                                      @(re-frame/subscribe [:player])
        screen                                      @(re-frame/subscribe [:screen])
        {:keys [start-game-errors start-games]}     @(re-frame/subscribe [:start-game])]
    (when (and listen-to-game-id
               (-> game-state :game-state (= :started))
               player)
      (re-frame/dispatch [:push-state ::game-play/view {:game-id listen-to-game-id}]))
    [:div
     [:> semantic-ui/Container {:text true}
      [:> semantic-ui/Header {:as "h3"}
       "Lobby for " game-name ", game password is: " password]

      [:p "Dont leave the lobby! If you do, you wont be able to join the game
      when it starts!"]

      [:p "Wait here until enough player have joined the game. There needs to be
      at least 2 players (maximum 4) before you can start the game. When enough
      players have joined, a \"Start game!\" button will show up. If a game is
      not started within 5 minutes, it will be garbage collected."]

      (if (seq start-game-errors)
        [:p (-> start-game-errors first :message)]
        [:div {:style {:display         "flex"
                       :align-items     "center"
                       :justify-content "center"}}
         [:> semantic-ui/Form.Button
          {:onClick #(re-frame/dispatch [::start-game listen-to-game-id])
           :color "green"}
          "Start game!"]])

      [:p "Players:"]
      (for [{:keys [player-name player-nr player-id]} (->> game-state
                                                           :players
                                                           (vals))]
        ^{:key (str player-name "/" player-id)}
        [:p (str player-nr " | " player-name)])

      [:div {:style {:display         "flex"
                     :align-items     "center"
                     :justify-content "center"}}
       [:div
        [client.common/screen screen]]]]]))

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

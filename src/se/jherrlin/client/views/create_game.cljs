(ns se.jherrlin.client.views.create-game
  (:require [se.jherrlin.client.form.managed :as form.managed]
            [re-frame.core :as re-frame]
            [cljs.pprint :as pprint]
            [se.jherrlin.client.views.join-game :as join-game]
            [se.jherrlin.server.user-commands :as user-commands]
            ["semantic-ui-react" :as semantic-ui]))


(re-frame/reg-event-fx
 ::create-game
 (fn [_ [_ create-game-value]]
   (def create-game-value create-game-value)
   {:ws-send {:data       [:game/create (select-keys create-game-value [:action :game-name :game-password])]
              :on-success (fn [{:keys [status data message] :as m}]
                            (if (= status :error)
                              (js/alert message)
                              (re-frame/dispatch [::join-game/join-game (assoc create-game-value :action :join-game)])))}}))

(defn view []
  (let [form-values     @(re-frame/subscribe [:form-values ::create-game])
        form-valid?     @(re-frame/subscribe [:form-valid? ::create-game ::user-commands/create-game])
        clicked-create? @(re-frame/subscribe [:form-meta :create-game :clicked-create?])]

    [:div
     [:> semantic-ui/Container {:text true}
      [:> semantic-ui/Header {:as "h3"}
       "Create new game"]
      [:p "Select a game name, a password for the game and your player name. The
       password is used so that your friends can join your game. When you have
       created a new game you will go to the lobby where you wait for your
       friends to join."]]
     [:br]
     [:div {:style {:display         "flex"
                    :align-items     "center"
                    :justify-content "center"}}
      [:> semantic-ui/Form
       [form.managed/input {:form        ::create-game
                            :spec        ::user-commands/game-name
                            :validate?   clicked-create?
                            :label       "Game name"
                            :placeholder "Game name"
                            :attr        :game-name
                            :style       {:width "300px"}}]
       [form.managed/input {:form        ::create-game
                            :spec        ::user-commands/game-password
                            :validate?   clicked-create?
                            :label       "Game password"
                            :placeholder "Game password"
                            :attr        :game-password
                            :style       {:width "300px"}}]
       [form.managed/input {:form        ::create-game
                            :spec        ::user-commands/player-name
                            :validate?   clicked-create?
                            :label       "Player name"
                            :placeholder "Player name"
                            :attr        :player-name
                            :style       {:width "300px"}}]
       [:> semantic-ui/Form.Button
        {:fluid   true
         :onClick (fn [_]
                    (if form-valid?
                      (re-frame/dispatch [::create-game form-values])
                      (re-frame/dispatch [:form-meta :create-game :clicked-create? true])))}
        "Create game"]]]]))

(defn routes []
  [["/game/create"
    {:name        ::view
     :view        [#'view]
     :controllers [{:start
                    (fn [_]
                      (re-frame/dispatch [:form-value ::create-game :action :create-game]))
                    :stop
                    (fn [_]
                      (re-frame/dispatch [:form-reset ::create-game]))}]}]])

(ns se.jherrlin.client.views.create-game
  (:require [se.jherrlin.client.form.managed :as form.managed]
            [re-frame.core :as re-frame]
            [cljs.pprint :as pprint]
            [se.jherrlin.server.user-commands :as user-commands]
            ["semantic-ui-react" :as semantic-ui]))


(re-frame/reg-event-fx
 ::create-game
 (fn [_ [_ create-game-value]]
   {:ws-send {:data       [:game/create create-game-value]
              :on-success (fn [{:keys [status data message] :as m}]
                            (if (= status :error)
                              (js/alert message)
                              (re-frame/dispatch [:push-state :se.jherrlin.client.views.game-lobby/view (select-keys data [:game-id])])))}}))
{:status :ok,
 :data
 {:game-id   #uuid "5ef147ca-8caa-4ecf-b3c1-5cef2a2a5dee",
  :game-name "kalle",
  :password  "kalle"}}


#_(re-frame/dispatch [:push-state :se.jherrlin.client.views.game-lobby/view {:game-id #uuid "5ef147ca-8caa-4ecf-b3c1-5cef2a2a5dee"}])

(defn view []
  (let [form-values     @(re-frame/subscribe [:form-values ::create-game])
        form-valid?     @(re-frame/subscribe [:form-valid? ::create-game ::user-commands/create-game])
        clicked-create? @(re-frame/subscribe [:form-meta :create-game :clicked-create?])]
    [:div
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
     [:br]
     [:> semantic-ui/Form.Button
      {:onClick (fn [_]
                  (if form-valid?
                    (re-frame/dispatch [::create-game form-values])
                    (re-frame/dispatch [:form-meta :create-game :clicked-create? true])))}
      "Create game"]
     [:div
      [:div (str "form valid?" form-valid?)]]
     [:pre
      (str "Create game form values:\n"
           (with-out-str (pprint/pprint form-values)))]]))

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

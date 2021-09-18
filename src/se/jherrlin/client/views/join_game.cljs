(ns se.jherrlin.client.views.join-game
  (:require [se.jherrlin.client.form.managed :as form.managed]
            [re-frame.core :as re-frame]
            [cljs.pprint :as pprint]
            [se.jherrlin.client.views.game-lobby :as game-lobby]
            [se.jherrlin.server.user-commands :as user-commands]
            ["semantic-ui-react" :as semantic-ui]))

(def events
  [{:n ::available-games}])

(comment
  (::available-games @re-frame.db/app-db)
  )

{:status :ok,
 :data
 {:game-id #uuid "355d82d6-e038-4dac-ada5-080570be7921",
  :player-id #uuid "a6a0bc9c-9845-471c-a726-aa53c1c4c5a5",
  :player-name "asd"}}
(re-frame/reg-event-fx
 ::join-game
 (fn [_ [_ join-game-data]]
   {:ws-send {:data       [:game/join join-game-data]
              :on-success (fn [{:keys [status data message] :as m}]
                            (def m m)
                            (if (= status :error)
                              (js/alert message)
                              (do
                                (re-frame/dispatch [:listen-to-game-id (:game-id data)])
                                (re-frame/dispatch [:player data])
                                (re-frame/dispatch [:push-state ::game-lobby/view {:game-id (:game-id data)}]))))}}))

(doseq [{:keys [n s e]} events]
  (re-frame/reg-sub n (or s (fn [db _] (n db))))
  (re-frame/reg-event-db n (or e (fn [db [_ e]] (assoc db n e)))))

(re-frame/reg-event-fx
 ::get-available-games
 (fn [_ [_]]
   {:ws-send {:data       [:game/list nil]
              :on-success (fn [games]
                            (re-frame/dispatch [::available-games games]))}}))

(defn view []
  (let [the-list @(re-frame/subscribe [::available-games])]
    [:div
     (for [{:keys [game-name subject]} the-list]
       ^{:key (str subject)}
       [:<>
        [:br]
        [:br]
        [:div game-name
         [:button
          {:on-click  #(re-frame/dispatch [:push-state ::login {:game-name game-name :game-id subject}])
           :body      "Join game"}
          "join"]]])]))

(defn login []
  (let [form-values     @(re-frame/subscribe [:form-values ::join-game])
        form-valid?     @(re-frame/subscribe [:form-valid? ::join-game ::user-commands/join-game])
        clicked-create? @(re-frame/subscribe [:form-meta :join-game :clicked-create?])]
    [:<>
     [:> semantic-ui/Container {:text true}
      [:> semantic-ui/Header {:as "h3"}
       "Join game"]
      [:p "Enter game password and your player name to join the game. If the
      password is correct and your player name is unique to the game you will be
      taken to the game lobby."]]
     [:br]
     [:div {:style {:display         "flex"
                    :align-items     "center"
                    :justify-content "center"}}
      [:> semantic-ui/Form
       [form.managed/input {:form        ::join-game
                            :spec        ::user-commands/game-password
                            :validate?   clicked-create?
                            :label       "Game password"
                            :placeholder "Game password"
                            :attr        :game-password
                            :style       {:width "300px"}}]
       [form.managed/input {:form        ::join-game
                            :spec        ::user-commands/player-name
                            :validate?   clicked-create?
                            :label       "Player name"
                            :placeholder "Player name"
                            :attr        :player-name
                            :style       {:width "300px"}}]
       [:br]
       [:> semantic-ui/Form.Button
        {:fluid   true
         :onClick (fn [_]
                    (if form-valid?
                      (re-frame/dispatch [::join-game form-values])
                      (re-frame/dispatch [:form-meta :join-game :clicked-create? true])))}
        "Join game"]]]]))

(defn routes []
  [["/game/join"
    {:name        ::view
     :view        [#'view]
     :controllers [{:start
                    (fn [_]
                      (re-frame/dispatch [::get-available-games])
                      (println "enter join"))
                    :stop
                    (fn [_]
                      (println "leave join"))}]}]
   ["/game/join/:game-name/:game-id"
    {:name        ::login
     :view        [#'login]
     :controllers [{:parameters {:path [:game-id :game-name]}
                    :start
                    (fn [{{:keys [game-id game-name]} :path :as a}]
                      (re-frame/dispatch [:form-value ::join-game :action :join-game])
                      (re-frame/dispatch [:form-value ::join-game :game-name game-name])
                      (println "enter login"))
                    :stop
                    (fn [_]
                      (println "leave login"))}]}]])

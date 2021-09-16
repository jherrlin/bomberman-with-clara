(ns se.jherrlin.client.views.join-game
  (:require [se.jherrlin.client.form.managed :as form.managed]
            [re-frame.core :as re-frame]
            [cljs.pprint :as pprint]
            [se.jherrlin.server.user-commands :as user-commands]
            ["semantic-ui-react" :as semantic-ui]))

(def events
  [{:n ::available-games}])

(comment
  (::available-games @re-frame.db/app-db)
  )

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
    [:div
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
      {:onClick (fn [_]
                  (if form-valid?
                    (re-frame/dispatch [::join-game form-values])
                    (re-frame/dispatch [:form-meta :join-game :clicked-create? true])))}
      "Join game"]
     [:div
      [:div (str "form valid?" form-valid?)]]
     [:pre
      (str "Join game form values:\n"
           (with-out-str (pprint/pprint form-values)))]]))

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
     :controllers [{:start
                    (fn [_]
                      ;; (re-frame/dispatch [::get-available-games])
                      (println "enter login"))
                    :stop
                    (fn [_]
                      (println "leave login"))}]}]])

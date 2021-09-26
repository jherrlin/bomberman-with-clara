(ns se.jherrlin.client.views.past-games
  (:require [se.jherrlin.client.form.managed :as form.managed]
            [re-frame.core :as re-frame]
            [cljs.pprint :as pprint]
            [se.jherrlin.client.views.game-lobby :as game-lobby]
            [se.jherrlin.claraman.user-commands :as user-commands]
            ["semantic-ui-react" :as semantic-ui]
            [clojure.string :as str]))


(def events-
  [{:n ::past-games}])

(doseq [{:keys [n s e]} events-]
  (re-frame/reg-sub n (or s (fn [db _] (n db))))
  (re-frame/reg-event-db n (or e (fn [db [_ e]] (assoc db n e)))))

(re-frame/reg-event-fx
 ::get-past-games
 (fn [_ [_]]
   (re-frame/dispatch [:http-get
                       {:url        "/past-games"
                        :on-success [::past-games]}])))

({:game-id #uuid "2d8607f2-b4cb-4e56-923a-6dbfb9aab2ed",
  :game-name "asd",
  :winner "K.O."}
 {:game-id #uuid "d7947630-55ef-4c68-92a2-21f8b1d65652",
  :game-name "asd",
  :winner "asd"})

(defn view []
  (let [the-past-games-list @(re-frame/subscribe [::past-games])]
    [:div
     (if-not (seq the-past-games-list)
       [:p "No past games."]
       [:<>
        [:> semantic-ui/Table {:celled true}
         [:> semantic-ui/Table.Header
          [:> semantic-ui/Table.Row
           [:> semantic-ui/Table.HeaderCell "Game name"]
           [:> semantic-ui/Table.HeaderCell "Game winner"]
           [:> semantic-ui/Table.HeaderCell "Inspect game"]]]
         [:> semantic-ui/Table.Body
          (for [{:keys [game-name game-id winner]} the-past-games-list]
            ^{:key (str game-id)}
            [:> semantic-ui/Table.Row
             [:> semantic-ui/Table.Cell game-name]
             [:> semantic-ui/Table.Cell winner]
             [:> semantic-ui/Table.Cell [:> semantic-ui/Form.Button
                                         {:on-click #(re-frame/dispatch [:push-state :se.jherrlin.client.views.spectate-game/view {:game-id game-id}])}
                                         "Inspect"]]])]]])]))

(defn routes []
  [["/game/pasts/"
    {:name        ::view
     :view        [#'view]
     :controllers [{:start
                    (fn [_]
                      (re-frame/dispatch [::get-past-games]))}]}]])

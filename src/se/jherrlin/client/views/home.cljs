(ns se.jherrlin.client.views.home
  (:require [se.jherrlin.client.form.managed :as form.managed]
            [re-frame.core :as re-frame]
            [cljs.pprint :as pprint]
            [se.jherrlin.server.game-state :as game-state]
            [se.jherrlin.client.views.join-game :as join-game]
            [se.jherrlin.server.user-commands :as user-commands]
            [se.jherrlin.datetime :as datetime]
            [se.jherrlin.client.events :as client.events]
            ["semantic-ui-react" :as semantic-ui]))


(def events-
  [{:n ::demo-events}
   {:n ::demo-position-in-time
    :s (fn [db [k]] (get db k 0))}])

(comment
  (::demo-events @re-frame.db/app-db)
  (::demo-position-in-time @re-frame.db/app-db)
  )

(defn events->screen
  ([events]
   (events->screen events 1))
  ([events nr]
   (->> (take nr events)
        (game-state/the-projection {})
        :games
        (vals)
        (first)
        (client.events/game-state->screen))))

(re-frame/reg-sub
 ::demo-screen
 :<- [::demo-events]
 :<- [::demo-position-in-time]
 (fn [[demo-events demo-position-in-time] _]
   (events->screen demo-events demo-position-in-time)))

(doseq [{:keys [n s e]} events-]
  (re-frame/reg-sub n (or s (fn [db _] (n db))))
  (re-frame/reg-event-db n (or e (fn [db [_ e]] (assoc db n e)))))

(defn trunc
  [s n]
  (subs s 0 (min (count s) n)))

(defn print-events-table
  "Print event table with newest events on top."
  [events]
  (->> events
       (sort-by :time #(compare %2 %1))
       (reverse)
       (map-indexed vector)
       (reverse)
       (map (fn [[idx {:keys [data time] :as event}]]
              (-> event
                  (assoc :nr idx)
                  (assoc :time (datetime/datetime-format time "yyyy-MM-dd HH:mm:ss"))
                  (dissoc :id :content-type :subject)
                  (assoc :data (trunc (str (dissoc data :game-id :bomb-added-timestamp :timestamp)) 100)))))
       (pprint/print-table)))

(defn view []
  (let [demo-events           @(re-frame/subscribe [::demo-events])
        demo-position-in-time @(re-frame/subscribe [::demo-position-in-time])
        demo-screen           @(re-frame/subscribe [::demo-screen])]
    [:<>
     [:div
      [:div {:style {:text-align "center"}}
       [:h2 "Welcome to Claraman, a Bomberman clone!"]]
      [:> semantic-ui/Container {:text true}
       [:> semantic-ui/Header {:as "h3"}
        "What"]
       [:p "Claraman is a game project around two concepts. A rule engine named "
        [:a {:href "http://www.clara-rules.org/" :target "_blank"} "Clara"] " and event sourcing."]
       [:> semantic-ui/Container {:text true}
        [:> semantic-ui/Header {:as "h4"} "Clara"]
        [:p "takes care of all the business logic and creates small composable
       rules that together makes the rules of the game. Ryan has a really
       good " [:a {:href "https://www.youtube.com/watch?v=Z6oVuYmRgkk" :target "_blank"}
               " talk"] " that give a good introduction to Clara. I first heard
       of Clara through Felix podcast " [:a {:target "_blank"
                                             :href   "https://thesearch.space/episodes/2-ryan-brush-on-retaking-rules-for-developers"} "The
       Search Space"] "."]]
       [:> semantic-ui/Header {:as "h4"} "Event sourcing"]
       [:p "is the concept of accumulating state through events. The current
       game state is a projection from running the events through a reduce
       function. Below is a table with all of the events in the example game.
       Some data have been removed to make the table easier to read. You can
       drag the timeline back and forth and a new game state will be calculated
       from the events up until the time you point it to. "
        "Johan Halebys " [:a
                          {:href   "https://occurrent.org/documentation#introduction"
                           :target "_blank"} "Occurrent"] "
        is a good source for reading about event sourcing. I have followed
        Johans exampe and used the " [:a
                                      {:target "_blank"
                                       :href   "https://cloudevents.io/"} "cloudevents"] " specification for
        event structure in this project."]

       [:> semantic-ui/Header {:as "h3"}
        "How"]
       [:p "You can play the game with friends and it should be fully
       functional. The game tries to run at 5Hz and each player is able to make
       1 move, 1 place and 1 throw command every Hz."]
       [:> semantic-ui/Header {:as "h4"} "Controls"]
       [:> semantic-ui/Table {:celled true}
        [:> semantic-ui/Table.Header
         [:> semantic-ui/Table.Row
          [:> semantic-ui/Table.HeaderCell "Keyboard"]
          [:> semantic-ui/Table.HeaderCell "Action"]
          ]]
        [:> semantic-ui/Table.Body
         [:> semantic-ui/Table.Row
          [:> semantic-ui/Table.Cell "Up, Down, Left, Right"]
          [:> semantic-ui/Table.Cell "Move"]]
         [:> semantic-ui/Table.Row
          [:> semantic-ui/Table.Cell "Space"]
          [:> semantic-ui/Table.Cell "Place bomb"]]
         [:> semantic-ui/Table.Row
          [:> semantic-ui/Table.Cell "T"]
          [:> semantic-ui/Table.Cell "Throw bomb (if bomb is next to you and you face that direction)"]]]]

       [:> semantic-ui/Header {:as "h4"} "Board signs"]
       [:> semantic-ui/Table {:celled true}
        [:> semantic-ui/Table.Header
         [:> semantic-ui/Table.Row
          [:> semantic-ui/Table.HeaderCell "Sign"]
          [:> semantic-ui/Table.HeaderCell "Description"]]]
        [:> semantic-ui/Table.Body
         [:> semantic-ui/Table.Row
          [:> semantic-ui/Table.Cell "W"]
          [:> semantic-ui/Table.Cell "Wall, indestructible"]]
         [:> semantic-ui/Table.Row
          [:> semantic-ui/Table.Cell "S"]
          [:> semantic-ui/Table.Cell "Stone, can be destroyed with fire. Item may be underneath"]]
         [:> semantic-ui/Table.Row
          [:> semantic-ui/Table.Cell "F"]
          [:> semantic-ui/Table.Cell "Fire, if fire hits a player, player dies"]]
         [:> semantic-ui/Table.Row
          [:> semantic-ui/Table.Cell "+"]
          [:> semantic-ui/Table.Cell "Item, increase fire length by 1"]]]]]

      [:hr]
      [:div {:style {:text-align "center"}}
       (for [[i row]  (map-indexed list demo-screen)
             [j cell] (map-indexed list row)]
         ^{:key (str i j cell)}
         (let [t (:type cell)]
           [:<>
            [:div {:style {:width       "30px"
                           :height      "30px"
                           :display     "inline-block"
                           :font-size   "1.7em"
                           :line-height "1.7em"}}
             (:str cell)]
            (when (= (-> demo-screen first count dec) j)
              [:div {:style {:display "block"}}])]))]]
     [:div "Timeline -->"]
     [:input
      {:style     {:width "100%"}
       :type      "range"
       :min       1
       :max       (dec (count demo-events))
       :step      1
       :value     demo-position-in-time
       :on-change #(do
                     (let [nr (js/parseInt (.. % -target -value))]
                       (re-frame/dispatch [::demo-position-in-time nr])))}]
     [:div (str "Point in time: " demo-position-in-time)]
     [:hr]
     [:div
      [:pre {:style {:font-size   "0.7em"
                     :line-height "1.2em"}}
       (str "App db:\n"
            (with-out-str (print-events-table demo-events)))]]]))

(defn routes []
  [["/"
    {:name        ::view
     :view        [#'view]
     :controllers [{:start
                    (fn [_]
                      (re-frame/dispatch [:http-get
                                          {:url        "/events-demo"
                                           :on-success [::demo-events]}])
                      (re-frame/dispatch [::demo-position-in-time 1]))
                    :stop
                    (fn [_]
                      (println "leave new home"))}]}]])
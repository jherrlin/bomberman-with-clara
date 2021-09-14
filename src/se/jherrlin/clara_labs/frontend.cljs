(ns se.jherrlin.clara-labs.frontend
  (:require [reagent.dom :as rd]
            ["semantic-ui-react" :as semantic-ui]
            [taoensso.timbre :as timbre]
            [cljs.pprint :as pprint]
            [re-frame.core :as re-frame]
            [taoensso.sente :as sente :refer [cb-success?]]
            [goog.events.KeyCodes :as keycodes]
            [se.jherrlin.client.input.events :as events]
            [se.jherrlin.client.input.inputs :as inputs]
            [clojure.spec.alpha :as s]
            [se.jherrlin.server.user-commands :as user-commands]
            [goog.events :as gev])
  (:import [goog.events EventType KeyHandler]))

(set! *warn-on-infer* true)

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _] {:view :welcome}))

(declare chsk-send!)
(declare chsk)
(declare ch-chsk)
(declare chsk-state)

(defmulti wevent :id)

(defn event-msg-handler
  "Wraps `wevent` with logging, error catching, etc."
  [{:as ev-msg :keys [id ?data]}]
  #_(timbre/debug "incomming on ws from server" ev-msg)
  (wevent ev-msg))

(defmethod wevent :default
  [{:as ev-msg :keys [event id ?data]}]
  #_(timbre/debug "on default event handler" ev-msg)
  (timbre/debug "Could not find ws event handler so running on :default" id ?data)
  nil)

(re-frame/reg-event-db
 ::game-state
 (fn [db [_ game-state]]
   (let [{:keys [game-id]} (get db ::current-playing-game)]
     (if (= game-id (:game-id game-state))
       (assoc db :game-state game-state)
       db))))

(re-frame/reg-event-db ::change-view (fn [db [_ view]] (assoc db :view view)))
(re-frame/reg-sub ::view (fn [db] (:view db)))

(re-frame/reg-event-db ::game-list (fn [db [_ view]] (assoc db ::game-list view)))
(re-frame/reg-sub ::game-list (fn [db] (::game-list db)))


(re-frame/reg-event-db ::current-playing-game (fn [db [_ view]] (assoc db ::current-playing-game view)))
(re-frame/reg-sub ::current-playing-game (fn [db] (::current-playing-game db)))



(re-frame/reg-sub ::game-state (fn [db] (:game-state db)))
(re-frame/reg-sub ::players (fn [db] (get-in db [:game-state :players] [])))
(re-frame/reg-sub ::stones  (fn [db] (get-in db [:game-state :stones] [])))
(re-frame/reg-sub ::board   (fn [db] (get-in db [:game-state :board])))
(re-frame/reg-sub ::bombs   (fn [db] (get-in db [:game-state :bombs] [])))
(re-frame/reg-sub ::fire    (fn [db] (get-in db [:game-state :fire] [])))


(defn add-players-to-screen [players screen]
  (reduce
   (fn [screen' {:keys [position player-nr]}]
     (let [[x y] position]
       (-> screen'
           (assoc-in [y x :type] :player)
           (assoc-in [y x :str] (str player-nr)))))
   screen players))

(defn add-bombs-to-screen [bombs screen]
  (reduce
   (fn [screen' {:keys [bomb-position-xy]}]
     (let [[x y] bomb-position-xy]
       (assoc-in screen' [y x :type] :bomb)))
   screen bombs))

(defn add-fire-to-screen [fire screen]
  (reduce
   (fn [screen' {:keys [fire-position-xy]}]
     (let [[x y] fire-position-xy]
       (assoc-in screen' [y x :type] :fire)))
   screen fire))

(defn add-stones-to-screen [stones screen]
  (reduce
   (fn [screen' [x y]]
     (assoc-in screen' [y x :type] :stone))
   screen stones))

@re-frame.db/app-db

(re-frame/reg-sub
 ::screen
 (fn [{:keys [game-state] :as db}]
   (let [{:keys [players stones board bombs fire]} game-state]
     (some->> board
              (add-players-to-screen (vals players))
              (add-bombs-to-screen bombs)
              (add-fire-to-screen fire)
              (add-stones-to-screen stones)
              (mapv (fn [row]
                      (mapv (fn [cell]
                              (let [t (:type cell)]
                                (case t
                                  ;; :player (assoc cell :str "W")
                                  :wall   (assoc cell :str "W")
                                  :floor  (assoc cell :str " ")
                                  :fire   (assoc cell :str "F")
                                  :stone  (assoc cell :str "S")
                                  :bomb   (assoc cell :str "B")
                                  cell)))
                            row)))))))

(defmethod wevent :new/game-state
  [{:as ev-msg :keys [event id ?data]}]
  (re-frame/dispatch [::game-state ?data]))

(comment
  @re-frame.db/app-db
  )

(try
  ;; Setup websocket
  (let [{:keys [chsk ch-recv send-fn state]}
        (sente/make-channel-socket! "/websocket/chsk"
                                    nil
                                    {
                                     :port           3005
                                     :type           :auto
                                     :wrap-recv-evs? false})]
    (def chsk       chsk)
    (def ch-chsk    ch-recv)     ; ChannelSocket's receive channel
    (def chsk-send! send-fn)
    (def chsk-state state)       ; Watchable, read-only atom

    (sente/start-client-chsk-router!
     ch-chsk event-msg-handler))

  (catch js/Error e
    (js/console.log e)))

(defn create-game-form []
  (let [create-game-name     @(re-frame/subscribe [:form-value :create-game :game-name])
        create-game-password @(re-frame/subscribe [:form-value :create-game :game-password])
        form-values          @(re-frame/subscribe [:form-values :create-game])
        form-valid?          @(re-frame/subscribe [:form-valid? :create-game ::user-commands/create-game])]
    (re-frame/dispatch [:form-value :create-game :action :create-game])
    [:div
     [:h2 "Create a new game."]
     [:p "You need to send you game name and password to your friends."]
     [inputs/text
      {:placeholder "Game name"
       :on-change   #(re-frame/dispatch [:form-value :create-game :game-name %])
       :value       create-game-name}]
     [:br]
     [inputs/text
      {:placeholder "Game password"
       :on-change   #(re-frame/dispatch [:form-value :create-game :game-password %])
       :value       create-game-password}]

     [:pre
         (str "Create game form values:\n"
              (with-out-str (pprint/pprint form-values)))]

     (when-not form-valid?
       [:h4 (str "Is NOT form valid?")])

     [inputs/button
      {:on-click  #(chsk-send! [:game/create form-values])
       :body      "Start new game"
       :disabled? (not form-valid?)}]]))



(defn welcome-component []
  [:div
   #_[create-game-form]
   [:h3 "Welcome to Claraman, a Bomberman clone."]
   [:p "Claraman is mainly an experimental design project."]
   [:p "It uses the rule engine Clara for all of it's logic and event sourcing
   for state management."]])

(defn game-screen []
  (let [screen @(re-frame/subscribe [::screen])]
    [:div
     (if screen
       [:div
        (for [[i row]  (map-indexed list screen)
              [j cell] (map-indexed list row)]
          (let [t (:type cell)]
            [:<>
             [:div {:style {:width   "20px"
                            :height  "20px"
                            :display "inline-block"}}
              (:str cell)]
             (when (= (-> screen first count dec) j)
               [:div {:style {:display "block"}}])]))]
       [:div "loading..."])
     [:div
      [:br]
      [:p "Arrow keys to go up, down, left and right."]
      [:p "Space bar to place bomb."]
      [:p "T to throw bomb."]]]))

(defn list-games []
  (let [the-list              @(re-frame/subscribe [::game-list])
        join-game-form-values @(re-frame/subscribe [:form-values :join-game])
        join-game-password    @(re-frame/subscribe [:form-value :join-game :game-password])
        join-game-player-name @(re-frame/subscribe [:form-value :join-game :player-name])
        join-selected-game    @(re-frame/subscribe [:input-value :join-selected-game])
        form-valid?           @(re-frame/subscribe [:form-valid? :join-game ::user-commands/join-game])
        ]
    [:div
     [:p "List of games:"]
     [:div
      (for [{:keys [game-name subject]} the-list]
        ^{:key (str subject)}
        [:<>
         [:br]
         [:br]
         [:div game-name

          (when-not (= join-selected-game subject)
            [inputs/button
             {:on-click  #(do
                            (re-frame/dispatch [:form-value :join-game :game-name game-name])
                            (re-frame/dispatch [:input-value :join-selected-game subject]))
              :body      "Join game"
              :disabled? false}])
          #_[inputs/button
             {:on-click  #(js/console.log "soon")
              :body      "Spectate game"
              :disabled? false}]
          (when (= join-selected-game subject)
            [:div
             [inputs/text
              {:placeholder "Game password"
               :on-change   #(re-frame/dispatch [:form-value :join-game :game-password %])
               :value       join-game-password}]
             [:br]
             [inputs/text
              {:placeholder "Your player name for this game"
               :on-change   #(re-frame/dispatch [:form-value :join-game :player-name %])
               :value       join-game-player-name}]
             [:br]
             (when form-valid?
               [:p "Form is valid!"])
             [inputs/button
              {:on-click  #(chsk-send! [:game/join join-game-form-values]
                                       5000
                                       (fn [cb-reply]
                                         {:status :ok
                                          :data   {:game-id     #uuid "34c69976-f863-4f7f-abf1-d2805a4c208f",
                                                   :player-id   #uuid "7c64548e-687c-4ccd-b37a-52d0f39127e0",
                                                   :player-name "Pupas"}}
                                         (if (cb-success? cb-reply)
                                           (let [{:keys [status message data]} cb-reply]
                                             (println "Join success: "cb-reply)
                                             (if (= :ok status)
                                               (do
                                                 (re-frame/dispatch [::current-playing-game data])
                                                 (re-frame/dispatch [::change-view :game]))
                                               (js/alert message)))

                                           (println "Join errors: "cb-reply))))
               :body      "Lets join the game!"
               :disabled? false}]])
          [:br]]])]
     [:pre
         (str "Create game form values:\n"
              (with-out-str (pprint/pprint join-game-form-values)))]
     [:pre
         (str "join-selected-game:\n"
              (with-out-str (pprint/pprint join-selected-game)))]]))

(defn root-component []
  (let [view @(re-frame/subscribe [::view])]
    [:<>
     [:div
      [inputs/button
       {:on-click  #(re-frame/dispatch [::change-view :welcome])
        :body      "Home"}]
      [inputs/button
       {:on-click  #(re-frame/dispatch [::change-view :game])
        :body      "Game"}]
      [inputs/button
       {:on-click  #(re-frame/dispatch [::change-view :create-game])
        :body      "Create game"}]
      [inputs/button
       {:on-click  (fn [_]
                     (chsk-send! [:game/list nil]
                                 5000
                                 (fn [cb-reply]
                                   (if (cb-success? cb-reply)
                                     (do (println "Success: "cb-reply)
                                         (re-frame/dispatch [::game-list cb-reply]))
                                     (println "Error: "cb-reply))))
                     (re-frame/dispatch [::change-view :list-games]))
        :body      "List games"}]]
     (case view
       :welcome [welcome-component]
       :game [game-screen]
       :create-game [create-game-form]
       :list-games [list-games]
       )
     #_[:pre
         (str "Create game form values:\n"
              (with-out-str (pprint/pprint @re-frame.db/app-db)))]
     ]))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (rd/render [root-component] (.getElementById js/document "root")))


(defn capture-key
  "Given a `keycode`, execute function `f` "
  [keycode-map]
  (let [key-handler (KeyHandler. js/document)
        press-fn (fn [key-press]
                   (when-let [f (get keycode-map (.. key-press -keyCode))]
                     (f)))]
    (gev/listen key-handler
                (-> KeyHandler .-EventType .-KEY)
                press-fn)))

(def repl-subject "JOHN-HANNAS-game")
(def player-1-id "johns-id")

(defn g [m]
  (merge
   {:game-id   (get-in @re-frame.db/app-db [::current-playing-game :game-id])
    :user-id   (get-in @re-frame.db/app-db [::current-playing-game :player-id])}
   m))

(defn reagent-content-fn []
  ;; sets up the event listener
  ;; https://tech.toryanderson.com/2020/10/22/capturing-key-presses-in-clojurescript-with-closure/
  (capture-key {keycodes/DOWN  #(chsk-send! [:command/user-action (g {:action :move :direction :south})])
                keycodes/UP    #(chsk-send! [:command/user-action (g {:action :move :direction :north})])
                keycodes/LEFT  #(chsk-send! [:command/user-action (g {:action :move :direction :west})])
                keycodes/RIGHT #(chsk-send! [:command/user-action (g {:action :move :direction :east})])
                keycodes/SPACE #(chsk-send! [:command/user-action (g {:action :place-bomb})])
                keycodes/T     #(chsk-send! [:command/user-action (g {:action :throw-bomb})])})
  ;; ... the actual content that the rest of the fn should produce
  ;; (like the components that will use the keybinding)
  )


(comment
  (chsk-send! [:game/create {:game-name "FireGame" :game-password "pwd"}])

  (chsk-send! [:game/list nil]
              5000
              (fn [cb-reply]
                   (if (cb-success? cb-reply)
                     (println "Success: "cb-reply)
                     (println "Error: "cb-reply))))
  (chsk-send! [:game/join {:subject     #uuid "7efd2079-701a-4f85-9e99-f1a365af81c2"
                           :player-id   #uuid "c4ca7cfe-ae84-4792-b90a-67fdb8544787"
                           :player-name "John"
                           :password    "pwd"}])
  (chsk-send! [:game/join {:subject     #uuid "7efd2079-701a-4f85-9e99-f1a365af81c2"
                           :player-id   #uuid "b8484943-798a-43db-b9da-a1e6e5ce44c0"
                           :player-name "Hannah"
                           :password    "pwd"}])

  )

(defn init []
  (re-frame/dispatch-sync [::initialize-db])
  (println "Hello World!")
  (reagent-content-fn)
  (mount-root))




;; (js/alert "Hejsan")

(comment

  )

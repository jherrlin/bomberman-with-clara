(ns se.jherrlin.clara-labs.frontend
  (:require [reagent.dom :as rd]
            ["semantic-ui-react" :as semantic-ui]
            [taoensso.timbre :as timbre]
            [re-frame.core :as re-frame]
            [taoensso.sente :as sente :refer [cb-success?]]
            [goog.events.KeyCodes :as keycodes]
            [goog.events :as gev])
  (:import [goog.events EventType KeyHandler]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _] {}))

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
   (assoc db :game-state game-state)))

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

(defn root-component []
  (let [screen @(re-frame/subscribe [::screen])]
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
      [:div "loading..."])))

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
   {:game-id   repl-subject
    :user-id   player-1-id}
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

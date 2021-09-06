(ns se.jherrlin.clara-labs.frontend
  (:require [reagent.dom :as rd]
            ["semantic-ui-react" :as semantic-ui]
            [taoensso.timbre :as timbre]
            [re-frame.core :as re-frame]
            [taoensso.sente :as sente :refer [cb-success?]]
            ))

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
   (fn [screen' {:keys [position sign]}]
     (let [[x y] position]
       (-> screen'
           (assoc-in [y x :type] :player)
           (assoc-in [y x :str] sign))))
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

(re-frame/reg-sub
 ::screen
 (fn [{:keys [game-state] :as db}]
   (let [{:keys [players stones board bombs fire]}
         (:game-state @re-frame.db/app-db)
         #_game-state]
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


;; (let [board (get-in @re-frame.db/app-db [:game-state :board] [])]
;;   (->> board
;;        (mapv (fn [row]
;;                (mapv (fn [cell]
;;                        (let [t (:type cell)]
;;                          (case t
;;                            :wall  "W"
;;                            :floor " ")))
;;                      row)))))


(defmethod wevent :new/game-state
  [{:as ev-msg :keys [event id ?data]}]
  (re-frame/dispatch [::game-state ?data]))

(comment
  @re-frame.db/app-db
  )

(try
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
            [:div {:style {:width   "40px"
                           :height  "40px"
                           :display "inline-block"}}
             (:str cell)]
            (when (= 5 j)
              [:div {:style {:display "block"}}])]))]
      [:div "loading..."])))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (rd/render [root-component] (.getElementById js/document "root")))

(defn init []
  (re-frame/dispatch-sync [::initialize-db])
  (println "Hello World!")
  (mount-root))




;; (js/alert "Hejsan")

(comment
  (chsk-send! [::hejsan "asd"])
  )

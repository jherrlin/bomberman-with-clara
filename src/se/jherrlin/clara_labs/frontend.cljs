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



(let [board (get-in @re-frame.db/app-db [:game-state :board] [])]
  (->> board
       (mapv (fn [row]
               (mapv (fn [cell]
                       (let [t (:type cell)]
                         (case t
                           :wall  "W"
                           :floor " ")))
                     row)))))


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

(defn render-row [row]
  [:div
   (for [cell row]
     ^{:key (str cell)}
     [:div (:type cell)])])

(defn add-player-to-screen [{:keys [position sign]} screen]
  (assoc-in screen [(last position) (first position)] sign))

(defn add-players-to-screen [players screen]
  (reduce
   (fn [s player]
     (add-player-to-screen player s))
   screen players))



(->> @(re-frame/subscribe [::board])
     (mapv (fn [row]
             (mapv (fn [cell]
                     (let [t (:type cell)]
                       (case t
                         :wall  "W"
                         :floor " ")))
                   row)))
     (add-players-to-screen
      (->> @(re-frame/subscribe [::players])
           (vals))
      )
     )

(defn root-component []
  (let [board @(re-frame/subscribe [::board])]
    (if board
      [:div
       (for [[i row]  (map-indexed list board)
             [j cell] (map-indexed list row)]
         (let [t (:type cell)]
           [:<>
            (case t
              :wall  [:div {:style {:width   "40px"
                                    :height  "40px"
                                    :display "inline-block"}}
                      "wall"]
              :floor [:div {:style {:width   "40px"
                                    :height  "40px"
                                    :display "inline-block"}} "floor"])
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

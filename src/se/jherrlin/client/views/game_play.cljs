(ns se.jherrlin.client.views.game-play
  (:require [reitit.coercion.schema]
            [re-frame.core :as re-frame]
            [schema.core :as s]
            [se.jherrlin.client.websocket :as websocket]
            [se.jherrlin.client.views.spectate-game :as spectate-game]
            [goog.events :as gev]
            ["semantic-ui-react" :as semantic-ui]
            [goog.events.KeyCodes :as keycodes])
  (:import [goog.events EventType KeyHandler]))


(defn capture-key
  "Given a `keycode`, execute function `f` "
  [keycode-map]
  (let [key-handler (KeyHandler. js/document)
        press-fn (fn [key-press]
                   (when-let [f (get keycode-map (.. key-press -keyCode))]
                     (f)))]
    (gev/listen key-handler
                 (-> ^js KeyHandler .-EventType .-KEY)
                press-fn)))

(defn g [m]
  (merge
   {:game-id   (get-in @re-frame.db/app-db [:listen-to-game-id])
    :user-id   (get-in @re-frame.db/app-db [:player :player-id])}
   m))

(defn reagent-content-fn []
  ;; sets up the event listener
  ;; https://tech.toryanderson.com/2020/10/22/capturing-key-presses-in-clojurescript-with-closure/
  (capture-key {keycodes/DOWN  #(websocket/chsk-send! [:command/user-action (g {:action :move :direction :south})])
                keycodes/UP    #(websocket/chsk-send! [:command/user-action (g {:action :move :direction :north})])
                keycodes/LEFT  #(websocket/chsk-send! [:command/user-action (g {:action :move :direction :west})])
                keycodes/RIGHT #(websocket/chsk-send! [:command/user-action (g {:action :move :direction :east})])
                keycodes/SPACE #(websocket/chsk-send! [:command/user-action (g {:action :place-bomb})])
                keycodes/T     #(websocket/chsk-send! [:command/user-action (g {:action :throw-bomb})])})
  ;; ... the actual content that the rest of the fn should produce
  ;; (like the components that will use the keybinding)
  )


(defn view []
  (let [listen-to-game-id                       @(re-frame/subscribe [:listen-to-game-id])
        {:keys [game-name] :as game-state}      @(re-frame/subscribe [:game-state])
        player                                  @(re-frame/subscribe [:player])
        screen                                  @(re-frame/subscribe [:screen])
        {:keys [start-game-errors start-games]} @(re-frame/subscribe [:start-game])]
    (when (and listen-to-game-id
               (-> game-state :game-state (= :ended))
               player)
      (re-frame/dispatch [:push-state ::spectate-game/view {:game-id listen-to-game-id}]))
    (when start-game-errors
      [:<>
       [:div {:style {:text-align "center"}}
        [:> semantic-ui/Container {:text true}
         [:> semantic-ui/Header {:as "h3"}
          (if (seq start-game-errors)
            (-> start-game-errors first :message)
            "GAME ON!")]
         (for [{:keys [player-name player-nr player-id]} (->> game-state
                                                              :players
                                                              (vals))]
           ^{:key (str player-name "/" player-id)}
           [:p (str player-nr " | " player-name)])]]

       [:br]
       [:div {:style {:text-align "center"}}
        (for [[i row]  (map-indexed list screen)
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
             (when (= (-> screen first count dec) j)
               [:div {:style {:display "block"}}])]))]])))

(defn routes []
  [["/game/play/:game-id"
    {:name        ::view
     :view        [#'view]
     :controllers [{:parameters {:path [:game-id]}
                    :start
                    (fn [{{:keys [game-id]} :path :as a}]
                      (def a a)
                      (let [game-id (uuid game-id)]
                        (def game-id game-id))
                      (reagent-content-fn)
                      #_(re-frame/dispatch [:form-value ::create-game :action :create-game]))
                    :stop
                    (fn [_]
                      (println "leaving" ::view))}]}]])

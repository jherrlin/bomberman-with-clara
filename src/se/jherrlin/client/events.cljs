(ns se.jherrlin.client.events
  (:require [re-frame.core :as re-frame]
            [se.jherrlin.clara-labs.bomberman-rules :as bomberman-rules]
            [se.jherrlin.claraman.game-state :as game-state]))


(re-frame/reg-event-db
 ::initialize-db
 (fn [db _] {}))

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

(defn add-items-to-screen [items screen]
  (reduce
   (fn [screen' {:keys [item-position-xy item-power]}]
     (let [[x y] item-position-xy]
       (assoc-in screen' [y x :type] item-power #_:inc-fire-length)))
   screen items))

(defn add-stones-to-screen [stones screen]
  (reduce
   (fn [screen' [x y]]
     (assoc-in screen' [y x :type] :stone))
   screen stones))

(defn game-state->screen [gs]
  (let [{:keys [players stones board bombs fire game-state items]} gs]
    (when (#{:started :created} game-state)
      (some->> board
               (add-players-to-screen (vals players))
               (add-bombs-to-screen   bombs)
               (add-fire-to-screen    fire)
               (add-items-to-screen   items)
               (add-stones-to-screen  stones)
               (mapv (fn [row]
                       (mapv (fn [cell]
                               (let [t (:type cell)]
                                 (case t
                                   :wall            (assoc cell :str "#" :color "#000000")
                                   :floor           (assoc cell :str " " :color "#FFFFFF")
                                   :fire            (assoc cell :str "F" :color "#FFA701")
                                   :stone           (assoc cell :str "S" :color "#808080")
                                   :inc-fire-length (assoc cell :str "+" :color "#008080")
                                   :bomb            (assoc cell :str "B" :color "#FF0000")
                                   cell)))
                             row)))))))

(def events
  [{:n :listen-to-game-id}
   {:n :player}
   {:n :screen
    :s (fn [{:keys [game-state] :as db}]
         (game-state->screen game-state))}
   {:n :game-state
    :e (fn [db [_ game-state]]
         (let [game-id (get db :listen-to-game-id)]
           (if (= game-id (:game-id game-state))
             (assoc db :game-state game-state)
             db)))}
   {:n :start-game
    :s (fn [{:keys [game-state] :as db} [_]]
         (->> game-state
              (game-state/game-facts)
              (bomberman-rules/run-start-game-rules)))}])

(doseq [{:keys [n s e]} events]
  (re-frame/reg-sub n (or s (fn [db _] (n db))))
  (re-frame/reg-event-db n (or e (fn [db [_ e]] (assoc db n e)))))

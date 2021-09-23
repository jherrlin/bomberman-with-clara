(ns se.jherrlin.server.components.game-state
  (:require [com.stuartsierra.component :as component]
            [se.jherrlin.server.resources :as resources]
            [taoensso.timbre :as timbre]))


(def initial-game-state {})
;; (def initial-game-state (resources/read-edn-file "game-states/some-games.edn"))
(defonce game-state (atom initial-game-state))

(comment
  @game-state
  (reset! game-state initial-game-state)

  )



(defrecord GameState [args event-store]
  component/Lifecycle

  (start [this]
    (if (:game-state this)
      (do
        (timbre/info "Starting GameState component but server is already runnig.")
        this)
      (let [projection-fn (:projection-fn args)]
        (timbre/info "Starting GameState component.")
        (add-watch (:store event-store) :game-state-projection
                   (fn [key atom old-state new-state]
                     (when (seq (:events new-state))
                       (let [diffcount (- (-> new-state :events count)
                                          (-> old-state :events count))]
                         (reset! game-state
                                 (projection-fn
                                  @game-state
                                  (->> new-state
                                       :events
                                       (take diffcount)
                                       (reverse))))))))
        (assoc this
               :projection-fn projection-fn
               :game-state    game-state))))

  (stop [this]
    (if (get this :game-state)
      (do
        (timbre/info "Stopping GameState component.")
        ;; (reset! game-state initial-game-state)
        (remove-watch (:store event-store) :game-state-projection)
        (assoc this :projection-fn nil :game-state nil))
      (do
        (timbre/info "Stopping GameState component but no instance found!")
        this))))

(defn create
  "Create a new GameState component."
  [projection-fn]
  (map->GameState {:args {:projection-fn projection-fn}}))


(comment
  (def test-tom (atom {:events '()}))

  (concat
   [:a :b :c]
   [:d :e :f])

  (add-watch test-tom :test-tom
             (fn [key atom old-state new-state]
               (def old-state old-state)
               (def new-state new-state)))

  (let [diffcount (- (-> new-state :events count)
                     (-> old-state :events count))]
    (take diffcount (-> new-state :events)))

  @test-tom

  (swap! test-tom update :events #(concat [:a :b :c] %))
  (swap! test-tom update :events #(concat [:d :e :f] %))
  (swap! test-tom update :events #(concat [:g :h :i :j] %))
  (swap! test-tom update :events #(concat [:k :l :m :n :o :p :q] %))

  )

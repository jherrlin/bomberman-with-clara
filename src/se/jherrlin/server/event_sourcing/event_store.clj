(ns se.jherrlin.server.event-sourcing.event-store)

(defonce store-init {:events '()})

(defonce store (atom store-init))

(defn add-event [event]
  (swap! store update :events conj event))

(comment
  (add-watch store :watcher
             (fn [key atom old-state new-state]
               (prn "-- Atom Changed --")
               (prn "key" key)
               (prn "atom" atom)
               (prn "atom type" (type atom))
               (prn "old-state" old-state)
               (prn "new-state" new-state)
               (prn "latest" (-> new-state :events first))))

  @store
  (reset! store store-init)

  (add-event {:a :b})
  (add-event {:c :d})
  (add-event {:abc :def})

  ;; https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type
  ;; Används av Google Docs

  ;; https://en.wikipedia.org/wiki/Raft_(algorithm)

  ;; https://orbitdb.org/
  )

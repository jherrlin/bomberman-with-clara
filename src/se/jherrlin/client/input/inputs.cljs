(ns se.jherrlin.client.input.inputs)


(defn text [{:keys [attr on-blur on-change on-focus placeholder value]
             :or   {placeholder ""
                    on-change   #(js/console.log "text: "  (.. % -target -value))
                    on-focus    (fn [])
                    on-blur     (fn [])}}]
  [:input
   (merge
    {:on-change   #(on-change (.. % -target -value))
     :on-focus    on-focus
     :on-blur     on-blur
     :type        "text"
     :placeholder placeholder
     :value       (or value "")}
    attr)])

(defn password [m]
  (text
   (assoc-in m [:attr :type] "password")))

(defn textarea [{:keys [attr on-blur on-change on-focus placeholder value rows cols]
                 :or   {placeholder ""
                        on-change   #(js/console.log "text: "  (.. % -target -value))
                        rows        5
                        cols        33
                        on-focus    (fn [])
                        on-blur     (fn [])}}]
  [:textarea
   (merge
    {:on-change   #(on-change (.. % -target -value))
     :on-focus    on-focus
     :on-blur     on-blur
     :rows        rows
     :cols        cols
     :type        "text"
     :placeholder placeholder
     :value       (or value "")}
    attr)])

(defn number [{:keys [attr on-blur on-change on-focus placeholder value]
               :or   {placeholder ""
                      on-change   #(js/console.log "numer: "  (.. % -target -value))
                      on-focus    (fn [])
                      on-blur     (fn [])}}]
  [:input.form-control
   (merge
    {:on-change   #(on-change (int (.. % -target -value)))
     :on-focus    on-focus
     :on-blur     on-blur
     :type        "number"
     :placeholder placeholder
     :value       (str value)}
    attr)])

(defn button [{:keys [on-click body disabled? attr]}]
  [:button
   (merge
    {:disabled disabled?
     :on-click on-click}
    attr)
   body])

(defn select [{:keys [on-click values display-fn]}]
  (let [values-indexed (map-indexed vector values)]
    [:select {:on-click #(on-click (nth values (int (.. % -target -value))))}
     (for [[idx m] values-indexed]
       ^{:key idx}
       [:option {:value idx}
        (display-fn m)])]))

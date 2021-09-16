(ns se.jherrlin.client.form.managed
  "Managed forms/inputs have options on how things should work. It's alywas
  possible to override default's."
  (:require [re-frame.core :as re-frame]
            [clojure.spec.alpha :as s]
            se.jherrlin.client.input.events
            ["semantic-ui-react" :as semantic-ui]
            [clojure.string :as str]))


(defn input [{:keys [attr error-fn form label read-only required spec validate?]
              :as   prop}]
  {:pre [(keyword? form) (keyword? attr)]}
  (let [value    (re-frame/subscribe [:form-value form attr])
        error-fn (or error-fn
                     (fn [value*]
                       (cond
                         (and validate? (not (s/valid? (or spec attr) value*)))
                         {:content (s/explain-str (or spec attr) value*)})))]
    [:> semantic-ui/Form.Input
     (merge
      {:fluid    true
       :required required
       :label    label
       :readOnly read-only
       :value    (or @value "")
       :onChange #(let [v (.. % -target -value)]
                    (re-frame/dispatch [:form-value form attr v]))
       :error    (error-fn @value)}
      prop)]))

(defn number [{:keys [label form attr read-only required validate? spec error-fn] :as prop}]
  (let [value    (re-frame/subscribe [:form-value form attr])
        error-fn (or error-fn
                     (fn [value*]
                       (cond
                         (and validate? (not (s/valid? (or spec attr) value*)))
                         {:content (s/explain-str attr value*)})))]
    [:> semantic-ui/Form.Input
     (merge
      {:fluid    true
       :required required
       :label    label
       :readOnly read-only
       :value    (or @value "")
       :onChange #(let [n (.. % -target -value)
                        n (js/parseInt n)
                        n (if (js/Number.isNaN n) nil n)]
                    (re-frame/dispatch [:form-value form attr n]))
       :error    (error-fn @value)}
      prop)]))

(defn textarea [{:keys [label form attr read-only required validate? spec error-fn] :as prop}]
  (let [value    (re-frame/subscribe [:form-value form attr])
        error-fn (or error-fn
                     (fn [value*]
                       (cond
                         (and validate? (not (s/valid? (or spec attr) value*)))
                         {:content (s/explain-str attr value*)})))]
    [:> semantic-ui/Form.TextArea
     (merge
      {:fluid    true
       :required required
       :label    label
       :readOnly read-only
       :value    (or @value "")
       :onChange #(let [v (.. % -target -value)]
                    (re-frame/dispatch [:form-value form attr v]))
       :error    (error-fn @value)}
      prop)]))

(defn select [{:keys [label form attr options validate? spec error-fn] :as props}]
  (let [value    (re-frame/subscribe [:form-value form attr])
        error-fn (or error-fn
                     (fn [value*]
                       (cond
                         (and validate? (not (s/valid? (or spec attr) value*)))
                         {:content (s/explain-str attr value*)})))]
    [:> semantic-ui/Form.Select
     (merge
      {:fluid     true
       :label     label
       :value     @value
       :search    true
       :selection true
       :options   options
       :onChange  (fn [_ data]
                    (let [{:keys [value]} (js->clj data :keywordize-keys true)]
                      (re-frame/dispatch [:form-value form attr value])))
       :error     (error-fn @value)}
      props)]))

(defn checkbox [{:keys [attr error-fn form label read-only required spec validate?]
                 :as   prop}]
  {:pre [(keyword? form) (keyword? attr)]}
  (let [value    (re-frame/subscribe [:form-value form attr])
        error-fn (or error-fn
                     (fn [value*]
                       (cond
                         (and validate? (not (s/valid? (or spec attr) value*)))
                         {:content (s/explain-str attr value*)})))]
    [:> semantic-ui/Form.Checkbox
     (merge
      {:fluid    true
       :label    label
       :type     "checkbox"
       :checked  @value
       :onChange #(let [{:keys [checked]} (js->clj %2 :keywordize-keys true)]
                    (re-frame/dispatch [:form-value form attr checked]))
       :error    (error-fn @value)}
      prop)]))

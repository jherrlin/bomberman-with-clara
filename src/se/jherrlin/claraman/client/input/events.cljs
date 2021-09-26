(ns se.jherrlin.client.input.events
  (:require
   [re-frame.core :as re-frame]
   [clojure.spec.alpha :as s]
   [clojure.string :as str]))


(defn set-form [db [_ form values]]                  (assoc-in db (flatten [::form form]) values))
(defn get-form [db [_ form]]                           (get-in db (flatten [::form form])))
(defn set-form-value [db [_ form attr value]]
  (if (or (nil? value) (str/blank? value) (js/Number.isNaN value))
    (update-in db (flatten [::form form :values]) dissoc attr)
    (assoc-in db (flatten [::form form :values attr]) value)))
(defn get-form-value [db [_ form attr]]                (get-in db (flatten [::form form :values attr])))
(defn reset-form-value [db [_ form attr]]          (-> db
                                                       (update-in (flatten [::form form :values]) dissoc attr)
                                                       (update-in (flatten [::form form :meta]) dissoc attr)))
(defn set-form-values [db [_ form entity]]           (assoc-in db (flatten [::form form :values]) entity))
(defn get-form-values [db [_ form]]                    (get-in db (flatten [::form form :values])))
(defn set-form-original-values [db [_ form values]]  (assoc-in db (flatten [::form form :original-values]) values))
(defn get-form-original-values [db [_ form]]           (get-in db (flatten [::form form :original-values])))
(defn set-form-visited? [db [_ form attr visited?]]  (assoc-in db (flatten [::form form :meta attr :visited?]) visited?))
(defn get-form-visited? [db [_ form attr]]             (get-in db (flatten [::form form :meta attr :visited?])))
(defn set-form-meta [db [_ form attr value]]         (assoc-in db (flatten [::form form :meta attr]) value))
(defn get-form-meta [db [_ form attr]]                 (get-in db (flatten [::form form :meta attr])))
(defn get-form-changed? [db [_ form]]          (not (= (get-in db (flatten [::form form :values]))
                                                       (get-in db (flatten [::form form :original-values])))))
(defn set-form-reset-values [db [_ form]]           (update-in db (flatten [::form form]) dissoc :values))
(defn set-form-reset-meta [db [_ form]]             (update-in db (flatten [::form form]) dissoc :meta))
(defn set-form-reset [db [_ form]]                      (update db ::form dissoc form))
(defn get-form-valid? [db [_ form spec]]
  (->> (flatten [::form form :values])
       (get-in db)
       (s/valid? spec)))

(def form-events
  [{:n :form
    :e set-form
    :s get-form}
   {:n :form-value
    :e set-form-value
    :s get-form-value}
   {:n :form-values
    :e set-form-values
    :s get-form-values}
   {:n :form-original-values
    :e set-form-original-values
    :s get-form-original-values}
   {:n :form-visited?
    :e set-form-visited?
    :s get-form-visited?}
   {:n :form-meta
    :e set-form-meta
    :s get-form-meta}
   {:n :form-changed?
    :s get-form-changed?}
   {:n :form-reset
    :e set-form-reset}
   {:n :form-reset-values
    :e set-form-reset-values}
   {:n :form-reset-meta
    :e set-form-reset-meta}
   {:n :form-valid?
    :s get-form-valid?}
   {:n :reset-form-value
    :e reset-form-value}])

(defn set-input-value [db [_ attribute value]]  (assoc-in db [:values attribute] value))
(defn get-input-value [db [_ attribute]]          (get-in db [:values attribute]))
(defn reset-input-value [db [_ attribute]]     (update-in db [:values] dissoc attribute))

(def input-events
  [{:n :input-value
    :e set-input-value
    :s get-input-value}
   {:n :reset-input-value
    :e reset-input-value}])

(doseq [{:keys [n s e]} (into form-events input-events)]
  (when s
    (re-frame/reg-sub n s))
  (when e
    (re-frame/reg-event-db n e)))

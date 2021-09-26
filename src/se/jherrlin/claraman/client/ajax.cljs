(ns se.jherrlin.claraman.client.ajax
  (:require [ajax.core :as ajax]
            day8.re-frame.http-fx
            [re-frame.core :as re-frame]
            re-frame.db))


(re-frame/reg-event-fx
 ::console-log
 (fn [_ [_ x]] (js/console.log x)))

(re-frame/reg-event-fx
 :http-get
 (fn [{:keys [db]} [_ {:keys [url on-success on-failure params]
                       :or   {on-success [::console-log]
                              on-failure [::console-log]}}]]
   {:http-xhrio {:method          :get
                 :uri             url
                 :params          params
                 :timeout         5000
                 :format          (ajax/transit-request-format)
                 :response-format (ajax/transit-response-format)
                 :on-success      on-success
                 :on-failure      on-failure}}))

(comment
  (re-frame/dispatch [:http-get {:url "/events-demo"}])
  )

(re-frame/reg-event-fx
 :http-post
 (fn [{:keys [db]} [_ {:keys [url on-success on-failure params body]
                       :or   {on-success [::console-log]
                              on-failure [::console-log]}}]]
   {:http-xhrio {:method          :post
                 :uri             url
                 :params          params
                 :body            body
                 :timeout         5000
                 :format          (ajax/transit-request-format)
                 :response-format (ajax/transit-response-format)
                 :on-success      on-success
                 :on-failure      on-failure}}))

(re-frame/reg-event-fx
 :http-delete
 (fn [{:keys [db]} [_ {:keys [url on-success on-failure params body]
                       :or   {on-success [::console-log]
                              on-failure [::console-log]}}]]
   {:http-xhrio {:method          :delete
                 :uri             url
                 :params          params
                 :body            body
                 :timeout         5000
                 :format          (ajax/transit-request-format)
                 :response-format (ajax/transit-response-format)
                 :on-success      on-success
                 :on-failure      on-failure}}))

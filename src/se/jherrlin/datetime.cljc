(ns se.jherrlin.datetime
  #?(:cljs (:import [goog.date Date DateTime UtcDateTime Interval]
                    [goog.i18n DateTimeFormat DateTimeParse TimeZone])))


(defn now []
  #?(:cljs (js/Date.)
     :clj (java.util.Date.)))

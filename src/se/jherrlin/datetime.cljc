(ns se.jherrlin.datetime
  (:refer-clojure :exclude [format])
  #?(:clj (:import (java.text SimpleDateFormat)))
  #?(:cljs (:require [goog.string.format]))
  #?(:cljs (:import [goog.date Date DateTime UtcDateTime Interval]
                    [goog.i18n DateTimeFormat DateTimeParse TimeZone])))


(defn now []
  #?(:cljs (js/Date.)
     :clj (java.util.Date.)))

(defn datetime-format
  ([datetime]
   (datetime-format datetime "yyyy-MM-dd HH:mm"))
  ([datetime format*]
   (when (inst? datetime)
     #?(:clj  (.format (SimpleDateFormat.         format*) datetime)
        :cljs (.format (goog.i18n.DateTimeFormat. format*) datetime)))))

(defn time-format
  "Format inst into a \"HH:mm\" format"
  [datetime]
  (datetime-format datetime "HH:mm"))

(defn date-format
  "Format inst into a \"yyyy-MM-dd\" format"
  [datetime]
  (datetime-format datetime "yyyy-MM-dd"))

#?(:cljs
   (defn parse-date [datetime]
     (js/Date. datetime)))

#?(:clj
   (defn parse-date [datetime]
     (.parse (java.text.SimpleDateFormat. "yyyy-MM-dd") datetime)))

#?(:clj
   (defn inst->local-datetime
     "Convert `#inst` to `java.time.LocalDateTime`."
     [inst]
     (.toLocalDateTime
      (.atZone
       (.toInstant inst)
       (java.time.ZoneId/systemDefault)))))

#?(:clj
   (defn milliseconds-between
     "Count milliseconds between two `#inst`."
     [inst1 inst2]
     (.toMillis
      (java.time.Duration/between
       (inst->local-datetime inst1)
       (inst->local-datetime inst2)))))

#?(:cljs
   (defn milliseconds-between
     [^js/Date inst1 ^js/Date inst2]
     (- (.getTime inst1)
        (.getTime inst2))))

(ns se.jherrlin.clara-labs.datetime)


(defn now! []
  #?(:clj  (java.util.Date.)
     :cljs (js/Date.)))


(defn inst->local-datetime
     "Convert `#inst` to `java.time.LocalDateTime`."
     [inst]
     (.toLocalDateTime
      (.atZone
       (.toInstant inst)
       (java.time.ZoneId/systemDefault))))

(defn milliseconds-between
  "Count milliseconds between two `#inst`."
  [inst1 inst2]
  (.toMillis
   (java.time.Duration/between
    (inst->local-datetime inst1)
    (inst->local-datetime inst2))))

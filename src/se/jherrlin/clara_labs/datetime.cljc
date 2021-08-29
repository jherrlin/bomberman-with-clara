(ns se.jherrlin.clara-labs.datetime)


(defn now! []
  #?(:clj  (java.util.Date.)
     :cljs (js/Date.)))

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

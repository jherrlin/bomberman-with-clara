(ns se.jherrlin.server.resources
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [taoensso.timbre :as timbre]))


(defn read-edn-file
  "Read file from filesystem and parse it to edn."
  [resource-path]
  (try
    (edn/read-string (slurp (io/resource resource-path)))
    (catch java.io.IOException e
      (timbre/error "Couldn't open '%s': %s\n" resource-path (.getMessage e)))
    (catch Exception e
      (timbre/error "Error parsing edn file '%s': %s\n" resource-path (.getMessage e)))))

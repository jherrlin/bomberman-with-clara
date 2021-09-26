(ns se.jherrlin.claraman.server.resources
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

;; https://stackoverflow.com/questions/17991565/clojure-defrecord-serialization-classnotfoundexception/17992918
(defn output
  "Write defrecords to file."
  [filename obj]
  (spit (io/resource filename) (pr-str obj)))

(defn pull
  "Read defrecords from resource file."
  [filename]
  (with-in-str (slurp (io/resource filename))
    (read)))


(comment
  (read-edn-file "test-data/create-game-state.edn")
  (read-edn-file "test-data/create-game-facts.edn")
  )

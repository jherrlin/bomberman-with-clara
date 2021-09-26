(ns se.jherrlin.claraman.server.pages
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [se.jherrlin.claraman.server.resources :as resources]
            [hiccup.page :refer [html5 include-js include-css]]
            [taoensso.timbre :as timbre]))


(defn index-html
  "Create an index page with a CSRF token attached to it."
  [req]
  (html5
   [:head
    [:meta {:charset "UTF-8"}]
    [:meta {:name    "viewport"
            :content "width=device-width, initial-scale=1"}]
    (include-css "//cdn.jsdelivr.net/npm/semantic-ui@2.4.1/dist/semantic.min.css")]
   [:body {:style "height: 100%" }
    [:div#app {:style           "height: 100%"
               :data-csrf-token (:anti-forgery-token req)} "loading..."]
    (->> "public/js/manifest.edn"
         (resources/read-edn-file)
         (map :output-name)
         (mapv #(str "js/" %))
         (apply include-js))]))

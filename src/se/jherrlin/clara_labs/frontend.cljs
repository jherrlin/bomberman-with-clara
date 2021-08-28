(ns se.jherrlin.clara-labs.frontend
  (:require [reagent.dom :as rd]
            ["semantic-ui-react" :as semantic-ui]))


(defn root-component []
  [:> semantic-ui/Container
        {:style {:height         "100%"
                 :display        "flex"
                 :flex-direction "column"}}
   [:div "Hello World"]
   ]
  )

(defn mount-root []
  (rd/render [root-component] (.getElementById js/document "root")))

(defn init []
  (println "Hello World!")
  (mount-root)
  )

;; (js/alert "Hejsan")

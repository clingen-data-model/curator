(ns curator
  (:require [reagent.core :as reagent]
            [reagent.dom :as rdom]))

(def a 1)

(defonce b 2)

(defn simple-component []
  [:div.container
   [:h1.title "I haz a bukkit"]
   [:p.subtitle
    "Noes! " [:strong " you "] " be "
    [:span {:style {:color "red"}} "stealin' "] "mah bukkit!"]])



(defn ^:dev/after-load mount-root []
  (println "[main] reloaded lib:")
  (rdom/render [simple-component]
               (.getElementById js/document "app")))

(defn ^:export init []
  (mount-root))

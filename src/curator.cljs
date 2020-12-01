(ns curator
  (:require ["firebase/app" :as firebase]
            ["firebase/auth"] 
            [reagent.core :as reagent]
            [reagent.dom :as rdom]
            [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [re-graph.core :as re-graph]
            [curator.pages.home.views :as home]
            [curator.common.views :as common-views]
            [curator.routes :as routes]))

(enable-console-print!)

(def firebase-config
  #js {:apiKey "AIzaSyCbBq1o5ZiVB5UflVNN-zCULAIRGgkHtrk"
       :authDomain "clingen-stage.firebaseapp.com"
       :databaseURL "https://clingen-stage.firebaseio.com"
       :projectId "clingen-stage"
       :storageBucket "clingen-stage.appspot.com"
       :messagingSenderId "583560269534"
       :appId "1:583560269534:web:b41fb5f1d09a6ef6fcbc4f"})

(defn main-page []
  [:div
   [:section.section (common-views/navbar)]
   [:section.section.home-search-main (home/home)]])

(defn ^:dev/after-load mount-root []
  (println "[main] reloaded lib:")
  (rdom/render [home/home]
               (.getElementById js/document "app")))

(re-frame/reg-event-db ::initialize-db
  (fn [db _]
    (if db
      db
      {:current-route nil})))

(defn ^:export init []
  (re-frame/dispatch-sync [::initialize-db])
  (re-frame/dispatch [::re-graph/init 
                      {:ws {:url "ws://localhost:8888/ws"}
                       :http {:url "http://localhost:8888/api"}}])
  (firebase/initializeApp firebase-config)
  (-> (firebase/auth)
      .getRedirectResult
      (.then #(dispatch [:common/recieve-auth-result %])))
  (mount-root))

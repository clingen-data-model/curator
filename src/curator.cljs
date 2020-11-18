(ns curator
  (:require ["firebase/app" :as firebase]
            ["firebase/auth"] 
            [reagent.core :as reagent]
            [reagent.dom :as rdom]
            [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [curator.pages.home.views :as home]
            [curator.common.views :as common-views]))

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
  (rdom/render [main-page]
               (.getElementById js/document "app")))

(defn ^:export init []
  (firebase/initializeApp firebase-config)
  (-> (firebase/auth)
      .getRedirectResult
      (.then #(dispatch [:common/recieve-auth-result %])))
  (mount-root))

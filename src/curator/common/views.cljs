(ns curator.common.views
  (:require [curator.common.events :as common-events]
            [curator.common.subs :as common-subs]
            [re-frame.core :as re-frame :refer [subscribe dispatch]]
))

(defn navbar []
  (let [current-user @(subscribe [::common-subs/current-user])]
    [:nav.navbar {:role "navigation"}
     [:div.navbar-brand
      [:div.navbar-item
       [:img {:src "/images/logo.svg"}]]]
     [:div.navbar-end
      [:div.navbar-item
       (if current-user         
         [:div.buttons
          [:button.button
           {:on-click #(dispatch [:common/authenticate])}
           [:strong "sign out"]]]
         [:div.buttons
          [:button.button
           {:on-click #(dispatch [:common/authenticate])}
           [:strong "sign in"]]])]]]))

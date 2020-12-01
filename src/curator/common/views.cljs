(ns curator.common.views
  (:require [curator.common.events :as common-events]
            [curator.common.subs :as common-subs]
            [re-frame.core :as re-frame :refer [subscribe dispatch]]))

(defn navbar []
  (let [user @(subscribe [::common-subs/user])]
    [:nav.navbar {:role "navigation"}
     [:div.navbar-brand
      [:div.navbar-item
       [:img {:src "/images/logo.svg"}]]]
     (if user
       [:div.navbar-end
        [:div.navbar-item (:email user)]
        [:div.navbar-item [:figure.image.is-32x32 
                           [:img.is-rounded {:src (:avatar user)}]]]
        [:div.navbar-item
         [:div.buttons
          [:button.button
           {:on-click #(dispatch [:common/sign-out])}
           [:strong "sign out"]]]]]
       [:div.navbar-end
        [:div.navbar-item
         [:div.buttons
          [:button.button
           {:on-click #(dispatch [:common/authenticate])}
           [:strong "sign in"]]]]])]))

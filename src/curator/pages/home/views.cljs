(ns curator.pages.home.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [curator.common.views :as common-views]
            [curator.pages.home.subs :as subs]
            [curator.common.subs :as common-subs]
            [curator.pages.home.events]))

(defn home [] 
  (let [suggested-genes [] ;;@(subscribe [::subs/suggested-genes])
        topic @(subscribe [::subs/search-topic])]
  [:div
   [:section.section (common-views/navbar)]
   [:section.section.home-search-main 
    [:div.columns.has-text-centered
     [:div.column
      [:div.columns
       [:div.column.is-one-fifth.is-offset-one-fifth
        [:div.dropdown.is-hoverable
         [:div.dropdown-trigger
          [:button.button {:aria-haspopup "true" :aria-controls "dropdown-menu"}
           [:span topic]
           [:span.icon.is-small
            [:i.fas.fa-angle-down {:aria-hidden "true"}]]]]
         [:div.dropdown-menu {:id "dropdown-menu" :role "menu"}
          [:div.dropdown-content.has-text-left
           [:a.dropdown-item {:href "#"
                              :on-click #(dispatch [:home/set-search-topic :gene])} 
            "gene"]
           [:a.dropdown-item {:href "#"
                              :on-click #(dispatch [:home/set-search-topic :disease])} 
            "disease"]
           [:a.dropdown-item {:href "#"
                              :on-click #(dispatch [:home/set-search-topic :affiliation])}
            "affiliation"]]]]]
       [:div.column.is-two-fifths
        [:div.field
         [:div.control
          [:input.input {:id "search-box"
                         :type "text"
                         :placeholder topic
                         :on-change #(dispatch [:home/request-suggestions
                                                (-> % .-target .-value)])}]]]]

       ]
      [:p @(subscribe [::subs/errors])]
      (for [gene suggested-genes]
        [:div.block 
         [:a ](:text gene)
         (when (seq (:curations gene))
           [:span.icon
            [:i.fas.fa-star]])])]]]]))

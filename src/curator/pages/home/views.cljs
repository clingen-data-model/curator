(ns curator.pages.home.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [curator.common.views :as common-views]
            [curator.pages.home.subs :as subs]
            [curator.common.subs :as common-subs]
            [curator.pages.home.events]))


(defn search []
  (let [topic @(subscribe [::subs/search-topic])
        suggested-genes []
        search-results @(subscribe [::subs/search-results])]
    [:section.section.home-search-main
     [:div.columns.has-text-centered
      [:div.column
       [:div.columns
        ; two fifths, left fifth empty, right fifth dropdown menu
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
             "affiliation"]
            [:a.dropdown-item {:href "#"
                               :on-click #(dispatch [:home/set-search-topic :variation])}
             "variation"]
            ]]]]
        ; two fifths for search bar
        [:div.column.is-two-fifths
         [:div.field
          [:div.control
           [:input.input {:id "search-box"
                          :type "text"
                          :placeholder topic
                          :on-change #(dispatch [:home/request-suggestions
                                                 ;(fn [event] (println event) (-> event .-target .-value))
                                                 (-> % .-target .-value) ; passed the Javascript event object
                                                 ])}]]]]
        ; one fifth submit button
        [:div.column.is-one-fifth
         [:button.button {:type "submit"
                          :on-click #(dispatch [:home/request-search
                                                topic
                                                (-> (.getElementById js/document "search-box") .-value)]
                                               )}
          "Search"]]
        ;;;
        ]
       [:p @(subscribe [::subs/errors])]
       [:div.container.is-widescreen
        (case topic
          :gene (for [gene suggested-genes]
                  [:div.block
                   [:a] (:text gene)
                   (when (seq (:curations gene))
                     [:span.icon
                      [:i.fas.fa-star]])])
          :variation [:div.block.table-container ;.is-widescreen
                      {:style {;:width :100%
                               }}
                      (let [fields [;:iri
                                    :id
                                    :subject
                                    :predicate
                                    ;:object
                                    :version
                                    :review_status
                                    :date_updated
                                    :date_validated]]
                        [:table.table ;.is-widescreen
                         {:style {:display :block
                                  :height :600px
                                  :overflow-y :scroll
                                  ;:border-collapse :collapse
                                  }}
                         [:thead
                          [:tr
                           {:style {}}
                           (for [field fields]
                             ^{:key field} [:th
                                            {:style {:background-color :white
                                                     :border-width :1px
                                                     :border-color :white
                                                     :position :sticky
                                                     :top :0px}}
                                            field])]]
                         [:tbody
                          {:style {;:height :100px
                                   ;:overflow-y :scroll
                                   ;:display :block
                                   }}
                          (for [search-result (:clinical_assertions search-results)]
                            ^{:key search-result} [:tr
                                                   ;{:style {:display :table}}
                                                   (for [field fields]
                                                         ^{:key field} [:td (get search-result field)])])
                          ]
                         ])]
          [:div.notification "Unknown search topic"]
          )]
       ]]]))

(defn unauthorized-user []
  [:section.hero
   [:div.hero-body
    [:div.container
     [:h1.title "You are not authorized to access this resource"]
     [:h2.subtitle "Please contact an administrator to gain access if you believe this message is in error."]]]])

(defn login-invitation []
  [:section.hero
   [:div.hero-body
    [:div.container
     [:h1.title "ClinGen admin interface"]
     [:h2.subtitle "Please login to continue"]]]])

(defn home []
  (let [authorization @(subscribe [::common-subs/user-authorization])]
    [:div
     [:section.section (common-views/navbar)]
     ;(case authorization
     ;  :authorized (search)
     ;  ;:unauthorized (unauthorized-user)
     ;  :unauthorized (search)
     ;  (login-invitation))
     (search)]))

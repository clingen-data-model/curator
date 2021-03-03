(ns curator.pages.gene-validity.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [curator.common.views :as common-views]
            [curator.pages.gene-validity.subs :as subs]))


(defn gene-validity-assertion []
  (let [assertion @(subscribe [::subs/assertion])]
    [:div
     [:section.section (common-views/navbar)]
     [:section.hero
      [:div.hero-body
       [:h1.title (get-in assertion [:gene :label])
        " -- "
        (get-in assertion [:disease :label])]
       [:div.subtitle
        (get-in assertion [:mode_of_inheritance :label])]
       [:div.subtitle
        (get-in assertion [:classification :label])
        " -- "
        (get-in assertion [:attributed_to :label])]]]]))

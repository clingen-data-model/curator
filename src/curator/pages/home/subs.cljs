(ns curator.pages.home.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::search-topic
 (fn [{:keys [:home/search-topic]}]
   (or search-topic :gene)))

(re-frame/reg-sub
 ::errors
 (fn [db]
   (:errors db)))

(re-frame/reg-sub
  ::search-results
  (fn [db]
    (:home/search-results db)))

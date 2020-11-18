(ns curator.pages.home.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::search-topic
 (fn [{:keys [:home/search-topic]}]
   (or search-topic :gene)))

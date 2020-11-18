(ns curator.pages.home.events
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]))

(re-frame/reg-event-db
 :home/set-search-topic
 (fn [db [_ topic]]
  (assoc db :home/search-topic topic)))

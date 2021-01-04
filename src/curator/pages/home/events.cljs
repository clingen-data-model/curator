(ns curator.pages.home.events
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]))

(def suggest-query "
query ($text: String, $suggester: Suggester) {
  suggest(suggest: $suggester, text: $text) {
    iri
    text
  }
}")

(re-frame/reg-event-db
 :home/set-search-topic
 (fn [db [_ topic]]
  (assoc db :home/search-topic topic)))

(re-frame/reg-event-fx
 :home/request-suggestions
 (fn [{:keys [db]} [_ search-text]]
   (.log js/console (str "request suggestion for " search-text))
   {:dispatch [::re-graph/query
               suggest-query
               {:text search-text
                :suggester "GENE"}
               [:home/recieve-suggestions]]}))

(re-frame/reg-event-db
 :home/recieve-suggestions
 (fn [db [_ {:keys [data errors] :as payload}]]
   (.log js/console (str data))
   (.log js/console (str errors))
   (assoc db 
          :home/suggested-genes (:suggest data)
          :errors (:message errors))))

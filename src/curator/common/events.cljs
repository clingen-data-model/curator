(ns curator.common.events
  (:require ["firebase/app" :as firebase]
            [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]))

(def user-query
"{
  genes(curation_activity: GENE_DOSAGE) {
    gene_list {
      label
    }
    count
  }
}")

(re-frame/reg-event-fx
 :common/authenticate
 (fn [_ _]
   (-> (firebase/auth) 
       (.signInWithRedirect (firebase/auth.GoogleAuthProvider.)))))

(re-frame/reg-event-db
 :common/recieve-user-query
 (fn [db [_ {:keys [data errors]}]]
   (cljs.pprint/pprint data)))

(re-frame/reg-event-fx
 :common/recieve-auth-result
 (fn [cofx [_ result]]
   (when (.-credential result)
     (let [user  {:email (-> result .-user .-email)
                  :avatar (-> result .-user .-photoURL)
                  :token (-> result .-credential .-idToken)}]
       {:db (assoc (:db cofx) :user user)
       :fx [[:dispatch 
             [::re-graph/re-init
                {:ws {:connection-init-payload (select-keys user [:token])}
                 :http {:impl {:headers {"Authorization"
                                         (str "Bearer "
                                              (:token user))}}}}]]
            [:dispatch
             [::re-graph/query
              user-query
              {}
              [:common/recieve-user-query]]]]}))))

 


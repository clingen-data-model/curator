(ns curator.common.events
  (:require ["firebase/app" :as firebase]
            [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
 :common/authenticate
 (fn [_ _]
   (-> (firebase/auth) 
       (.signInWithRedirect (firebase/auth.GoogleAuthProvider.)))))

(re-frame/reg-event-db
 :common/recieve-auth-result
 (fn [db [_ result-clj]]
   (let [result (js->clj result-clj)]
     (js/console.log (keys result))
     (if (get result "user")
       (do (js/console.log "logged in")
           (assoc db :current-user (get result "user")))
       (do (js/console.log "not logged in")
           db)))))





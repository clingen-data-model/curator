(ns curator
  (:require ["firebase/app" :as firebase]
            ["firebase/auth"]             ; must be loaded for firebase-app.auth to work
            [reagent.core :as reagent]
            [reagent.dom :as rdom]
            [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [re-graph.core :as re-graph]
            [curator.pages.home.views :as home]
            [curator.common.views :as common-views]
            [curator.routes :as routes]
            [curator.config :refer [firebase-config]]
            ))

(enable-console-print!)

(goog-define BACKEND_WS "ws://localhost:8888/ws")
(goog-define BACKEND_HTTP "http://localhost:8888/api")
(goog-define FIREBASE_CONFIG_NAME "clingen-dev")

(defn ^:dev/after-load mount-root []
  (println "[main] reloaded lib:")
  (rdom/render [routes/router-component {:router routes/router}]
               (.getElementById js/document "app")))

(re-frame/reg-event-db
  ::initialize-db
  (fn [db _]
    (if db
      db
      {:current-route nil})))

(defn ^:export init []
  (re-frame/dispatch-sync [::initialize-db])
  (let [app-config (get firebase-config (keyword FIREBASE_CONFIG_NAME))]
    (js/console.log "app-config:" (clj->js app-config))
    (firebase/initializeApp (clj->js app-config)))
  (.log js/console "backend-ws: " BACKEND_WS ", backend-http: " BACKEND_HTTP)
  (re-frame/dispatch [::re-graph/init
                      {:ws {:url BACKEND_WS}
                       :http {:url BACKEND_HTTP}}])
  (routes/init-routes!)
  (-> (firebase/auth) (.onAuthStateChanged #(dispatch [:common/auth-state-change])))
  (mount-root))

(ns curator
  (:require ["firebase" :as firebase]
            ["firebase/app" :as firebase.app]
            ["firebase/auth"]
            ;["firebase/remoteConfig" :as remote-config]
            [reagent.core :as reagent]
            [reagent.dom :as rdom]
            [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [re-graph.core :as re-graph]
            [curator.pages.home.views :as home]
            [curator.common.views :as common-views]
            [curator.routes :as routes]
            [curator.config :refer [firebase-config]]
            [cljs.reader :as edn]
            [clojure.core.async.interop]
            ))

(enable-console-print!)

(goog-define BACKEND_USE_FIREBASE_CONFIG false)
(goog-define BACKEND_HOST "localhost:8888")
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
  (let [remote-config (.remoteConfig firebase)]
    (js/console.log (str "use firebase config: " BACKEND_USE_FIREBASE_CONFIG))
    (cljs.core.async/go
      (let [backend-host
            (if (= true BACKEND_USE_FIREBASE_CONFIG)
              ; fetchAndActivate returns a javascript Promise which can be acquired using cljs async interop
              (do (cljs.core.async.interop/<p! (.fetchAndActivate remote-config))
                  ; remoteConfig.getValue returns a firebase.remoteconfig.Value object
                  (let [remote-config-backend-host (-> firebase .remoteConfig (.getValue "BACKEND_HOST") .asString)]
                    (.log js/console (str "got BACKEND_HOST from firebase remoteConfig: " remote-config-backend-host))
                    remote-config-backend-host))
              ; Default to the BACKEND_HOST defined locally
              BACKEND_HOST)]
        (re-frame/dispatch [::re-graph/init
                            {:ws {:url (str "ws://" backend-host "/ws")}
                             :http {:url (str "http://" backend-host "/api")}}]))))
  (routes/init-routes!)
  (-> (firebase/auth) (.onAuthStateChanged #(dispatch [:common/auth-state-change])))
  (mount-root))

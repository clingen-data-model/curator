(ns curator.common.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::current-user
 (fn [{:keys [:current-user]}]
   current-user))

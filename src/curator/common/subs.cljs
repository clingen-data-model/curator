(ns curator.common.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::user
 (fn [db]
   (:user db)))

(ns stanne.views.home
  (:require [stanne.views.layout :refer [layout]]))

(defn home-view [config]
  (layout
   [:div
    [:h2 "in home view"]
    [:div (str config)]]))

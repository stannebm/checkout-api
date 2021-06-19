(ns stanne.views.error-msg
  (:require
   [stanne.views.layout :refer [layout]]))

(defn error-msg-view [message]
  (layout
   [:div.p-5.text-lg

    [:img {:class "w-36"
           :src "/fpx-logo.png"}]

    [:h2.text-3xl.py-3.mb-6.text-red-500 {:style {:font-family "Lato"}}
     "Failed to Process Request"]

    [:p message]]))

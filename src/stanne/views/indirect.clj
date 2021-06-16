(ns stanne.views.indirect
  (:require
   [stanne.views.layout :refer [layout]]))

(defn indirect-view []
  (layout
   [:div.p-5.text-lg
    [:h2.text-3xl.py-3.mb-12 {:style {:font-family "Roboto"}}
     "Online Banking (Transfer)"]

    [:div.my-3.mb-8.p-3.bg-gray-200.text-gray-600.text-md
     [:p "Order Reference: " 1]
     [:p "Total: RM" 2]]]))

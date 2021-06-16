(ns stanne.views.indirect
  (:require
   [stanne.views.layout :refer [layout]]))

(defn indirect-view []
  (layout
   [:div.p-5.text-lg

    [:img {:class "w-36"
           :src "/fpx-logo.png"}]

    [:h2.text-3xl.py-3.mb-6.text-green-500 {:style {:font-family "Lato"}}
     "Payment Successful"]

    [:div.my-3.mb-8.p-3.bg-gray-200.text-gray-600.text-md
     [:div

      [:div.flex
       [:div {:class "w-5/12 text-gray-400 font-medium"} "label"]
       [:div.flex-1 "value"]]]

     [:div.grid.grid-cols-2.gap-2
      [:span "Recipient:"]
      [:span "Minor Basilica St. Anne BM"]
      [:span "Order Reference:"]
      [:span "asdasdad"]
      [:span "Bank:"]
      [:span "PBB"]
      [:span "Reference ID:"]
      [:span "202020202"]
      [:span "FPX Transaction ID:"]
      [:span "202020202"]
      [:span "Amount:"]
      [:span "RM" 20]]]

    [:button {:onclick "window.print()"
              :type "btn"
              :class "inline-flex justify-center mb-2 py-3 px-6 border border-transparent shadow-sm text-md font-medium rounded-md text-white bg-green-500 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"}
     "Print Receipt"]]))

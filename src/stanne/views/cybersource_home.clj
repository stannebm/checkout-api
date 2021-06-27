(ns stanne.views.cybersource-home
  (:require
   [stanne.views.layout :refer [layout]]))

(defn home-view []
  (layout
   [:div.p-5.text-lg

    [:img {:class "w-36"
           :src "/fpx-logo.png"}]

    [:h2.text-3xl.py-3.mb-6.text-gray-500 {:style {:font-family "Lato"}}
     "Credit/Debit Card"]

    [:div.my-3.mb-8.p-3.bg-gray-200.text-gray-600.text-md
     [:p "Order Reference: " "order ref"]
     [:p "Total: RM" "3001"]]

    [:form.m-0 {:method "post"
                :action "REPLACE URL"}

     #_(for [[field value] form-params]
         [:input {:type "hidden"
                  :value value
                  :name field}])

     [:button {:type "submit"
               :x-bind:disabled "selectedBank.length === 0"
               :x-bind:class "{\"opacity-50\": selectedBank.length === 0, \"pointer-events-none\": selectedBank.length === 0}"
               :class "inline-flex justify-center mb-2 py-3 px-6 border border-transparent shadow-sm text-md font-medium rounded-md text-white bg-green-500 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"}
      "Confirm"]]]))

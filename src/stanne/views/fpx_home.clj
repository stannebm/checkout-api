(ns stanne.views.fpx-home
  (:require
   [jsonista.core :as json]
   [stanne.views.layout :refer [layout]]))

(defn home-view [{:keys [url banks checksums form-params tnc]}]
  (let [x-data {:selectedBank ""
                :checksums checksums}]
    (layout
     [:div.p-5.text-lg {:x-data (json/write-value-as-string x-data)}

      [:img {:class "w-36"
             :src "/fpx-logo.png"}]

      [:h2.text-3xl.py-3.mb-6.text-gray-500 {:style {:font-family "Lato"}}
       "Online Banking"]

      [:div.my-3.mb-8.p-3.bg-gray-200.text-gray-600.text-md
       [:p "Reference No: " (form-params :fpx_sellerOrderNo)]
       [:p "Total: RM" (form-params :fpx_txnAmount)]]

      [:form.m-0 {:method "post"
                  :action url}

       [:div {:class "mb-5"}
        [:label {:for "fpx_buyerBankId",
                 :class "block text-md text-gray-500"}
         "Choose Bank"]
        [:select {:id "fpx_buyerBankId",
                  :name "fpx_buyerBankId",
                  :x-model "selectedBank"
                  :class "mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"}

         [:option {:value ""} "- Select Bank -"]
         (for [{:keys [name code]} (map second banks)]
           [:option {:value code} name])]]

       (for [[field value] form-params]
         [:input {:type "hidden"
                  :value value
                  :name field}])

       [:input {:type "hidden"
                :name "fpx_checkSum"
                :x-model "checksums[selectedBank]"}]

       [:p {:class "text-sm text-gray-500 px-3 py-5 mt-2"}
        "By clicking on the “Proceed” button, you hereby agree with "
        [:br]
        [:a {:class "text-gray-500 underline font-bold"
             :href tnc
             :target "_blank"}
         "FPX’s Terms & Conditions"]]

       [:button {:type "submit"
                 :x-bind:disabled "selectedBank.length === 0"
                 :x-bind:class "{\"opacity-50\": selectedBank.length === 0, \"pointer-events-none\": selectedBank.length === 0}"
                 :class "inline-flex justify-center mb-2 py-3 px-6 border border-transparent shadow-sm text-md font-medium rounded-md text-white bg-green-500 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"}
        "Confirm"]]])))

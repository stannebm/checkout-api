(ns stanne.views.home
  (:require
   [stanne.views.layout :refer [layout]]
   [stanne.fpx.ar :as ar]
   [stanne.fpx.be :as be]
   [stanne.fpx.common :as common]))

(defn home-view [config]
  (let [banks (be/bank-list config (common/bank-mapping (:env config)))
        ar (ar/authorization-request config)
        url (:url ar)
        form-params (-> ar
                        :form-params
                        (dissoc :fpx_buyerBankId))]
    (layout
     [:div.p-5.text-lg
      [:h2.text-4xl.mb-10 {:font-family "Roboto"}
       "Online Banking"]

      [:div.my-3.mb-5.p-3.bg-gray-200
       [:p "Total: RM" (form-params :fpx_txnAmount)]
       [:p "Order Reference: " (form-params :fpx_sellerOrderNo)]]

      [:form {:method "post"
              :action url}

       [:div {:class "mb-10"}
        [:label {:for "fpx_buyerBankId",
                 :class "block text-sm font-medium text-gray-700"}
         "Choose Bank"]
        [:select {:id "fpx_buyerBankId",
                  :name "fpx_buyerBankId",
                  :class "mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"}

         (for [{:keys [name code]} (vals banks)]
           [:option {:value code} name])]]

       (for [[field value] form-params]
         [:input {:type "hidden"
                  :value value
                  :name field}])

       [:div
        [:button {:type "submit",
                  :class "inline-flex justify-center py-3 px-6 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-gray-600 hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500"}
         "Continue"]]]])))

(ns stanne.views.fpx-indirect
  (:require
   [stanne.views.layout :refer [layout]]))

(defn- parse-dt [s]
  (if (empty? s) ""
      (str (subs s 0 4)
           "-"
           (subs s 4 6)
           "-"
           (subs s 6 8)
           " "
           (subs s 8 10)
           ":"
           (subs s 10 12)
           ":"
           (subs s 12 14))))

(defn- to-table [{:keys [bank fpx_fpxTxnId fpx_fpxTxnTime fpx_sellerOrderNo fpx_txnAmount fpx_txnCurrency]}]
  {"Recipient" "Minor Basilica St. Anne BM"
   "Bank" bank
   "Transaction Date/Time" (parse-dt fpx_fpxTxnTime)
   "Seller Order Number" fpx_sellerOrderNo
   "Reference No" fpx_fpxTxnId
   "FPX Transaction ID" fpx_fpxTxnId
   "Amount" (str fpx_txnCurrency fpx_txnAmount)})

(defn indirect-view [{:keys [status relevant-info]}]
  (layout
   [:div.p-5.text-lg

    [:img {:class "w-36"
           :src "/fpx-logo.png"}]

    (case status
      :ok
      [:h2.text-3xl.py-3.mb-6.text-green-500 {:style {:font-family "Lato"}}
       "Thank you"]

      :pending-authorization
      [:h2.text-3xl.py-3.mb-6.text-green-500 {:style {:font-family "Lato"}}
       "Payment Authorized"]

      :failed
      [:h2.text-3xl.py-3.mb-6.text-red-500 {:style {:font-family "Lato"}}
       "Payment Failed"])

    [:div.my-3.mb-8.p-3.bg-gray-200.text-gray-600.text-md
     [:div
      (for [[label value] (to-table relevant-info)]
        [:div.flex.mb-2
         [:div {:class "w-5/12 text-gray-400"}
          label]
         [:div.flex-1
          value]])]]
    (cond
      (contains? #{:ok :pending-authorization} status)
      [:button {:onclick "window.print()"
                :type "btn"
                :class "inline-flex justify-center mb-2 py-3 px-6 border border-transparent shadow-sm text-md font-medium rounded-md text-white bg-green-500 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"}
       "Print Receipt"]

      :else
      [:button {:onclick "window.print()"
                :type "btn"
                :class "inline-flex justify-center mb-2 py-3 px-6 border border-transparent shadow-sm text-md font-medium rounded-md text-white bg-yellow-500 hover:bg-yellow-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"}
       "Print Record"])]))

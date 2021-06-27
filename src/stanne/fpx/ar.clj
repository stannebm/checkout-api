(ns stanne.fpx.ar
  (:require
   [clojure.string :as str]
   [stanne.fpx.be :as be]
   [stanne.fpx.utils :as utils]))

(def ^:private checksum-fields
  [:fpx_buyerAccNo
   :fpx_buyerBankBranch
   :fpx_buyerBankId
   :fpx_buyerEmail
   :fpx_buyerIban
   :fpx_buyerId
   :fpx_buyerName
   :fpx_makerName
   :fpx_msgToken
   :fpx_msgType
   :fpx_productDesc
   :fpx_sellerBankCode
   :fpx_sellerExId
   :fpx_sellerExOrderNo
   :fpx_sellerId
   :fpx_sellerOrderNo
   :fpx_sellerTxnTime
   :fpx_txnAmount
   :fpx_txnCurrency
   :fpx_version])

(defn mk-checksum [params merchant-key]
  (-> (str/join "|" ((apply juxt checksum-fields) params))
      (utils/sign {:private-key merchant-key})
      (str/upper-case)))

(defn authorization-request
  "For UAT testing, use buyerBankId=TEST0021 and username/password = 1234/1234"
  [{:keys [reference-no amount fpx-config bank-mapping]}]
  (let [msg-type "AR"
        {:keys [exchange-id seller-id msg-token fpx-version pki endpoints]} fpx-config
        timestamp (utils/timestamp-id)
        banks (be/bank-list fpx-config bank-mapping)
        bank-codes (map first banks)
        params {:fpx_msgType msg-type
                :fpx_msgToken msg-token
                :fpx_sellerExId exchange-id
                :fpx_sellerExOrderNo reference-no
                :fpx_sellerOrderNo reference-no
                :fpx_productDesc reference-no
                :fpx_sellerTxnTime timestamp
                :fpx_sellerId seller-id
                :fpx_sellerBankCode "01"
                :fpx_txnCurrency "MYR"
                :fpx_txnAmount amount
                :fpx_buyerEmail ""
                :fpx_buyerName ""
                :fpx_buyerBankId ""
                :fpx_buyerBankBranch ""
                :fpx_buyerAccNo ""
                :fpx_buyerId ""
                :fpx_makerName ""
                :fpx_buyerIban ""
                :fpx_version fpx-version}
        checksum-for-bank
        (fn [bank-code]
          [bank-code (mk-checksum (assoc params :fpx_buyerBankId bank-code)
                                  (pki :merchant-key))])]
    {:url (endpoints :auth-request)
     :banks banks
     :checksums (into (array-map) (map checksum-for-bank bank-codes))
     :form-params params
     :tnc (endpoints :tnc)}))

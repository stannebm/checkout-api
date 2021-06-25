(ns stanne.fpx.ar
  (:require
   [clojure.string :as str]
   [stanne.fpx.be :as be]
   [stanne.fpx.core :as fpx]
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
  [{:keys [txn-amount fpx-config bank-mapping]}]
  (let [msg-type "AR"
        {:keys [exchange-id seller-id msg-token fpx-version pki endpoints]} fpx-config
        timestamp (utils/timestamp-id)
        banks (be/bank-list fpx-config bank-mapping)
        bank-codes (map first banks)
        params {:fpx_msgType msg-type
                :fpx_msgToken msg-token
                :fpx_sellerExId exchange-id
                :fpx_sellerExOrderNo timestamp
                :fpx_sellerTxnTime timestamp
                :fpx_sellerOrderNo timestamp
                :fpx_sellerId seller-id
                :fpx_sellerBankCode "01"
                :fpx_txnCurrency "MYR"
                :fpx_txnAmount txn-amount
                :fpx_buyerEmail ""
                :fpx_buyerName ""
                :fpx_buyerBankId ""
                :fpx_buyerBankBranch ""
                :fpx_buyerAccNo ""
                :fpx_buyerId ""
                :fpx_makerName ""
                :fpx_buyerIban ""
                :fpx_productDesc timestamp
                :fpx_version fpx-version}
        checksum-for-bank
        (fn [bank-code]
          [bank-code (mk-checksum (assoc params :fpx_buyerBankId bank-code)
                                  (pki :merchant-key))])]
    {:url (:auth-request endpoints)
     :banks banks
     :checksums (into (array-map) (map checksum-for-bank bank-codes))
     :form-params params}))

(comment
  (authorization-request {:txn-amount "33.99"
                          :fpx-config (fpx/config :dev)
                          :bank-mapping (fpx/bank-mapping :dev)}))

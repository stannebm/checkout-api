(ns stanne.fpx.ar
  (:require
   [clojure.string :as str]
   [org.httpkit.client :as http]
   [stanne.signature :as sig]))

(def checksum-fields [:fpx_buyerAccNo
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

(defn- authorization-request [{:keys [exchange-id seller-id msg-token fpx-version pki endpoints]}]
  (let [msg-type "AR"
        timestamp (sig/timestamp-id)
        form-params {:fpx_msgType "AR"
                     :fpx_msgToken "01"
                     :fpx_sellerExId exchange-id
                     :fpx_sellerExOrderNo timestamp
                     :fpx_sellerTxnTime timestamp
                     :fpx_sellerOrderNo timestamp
                     :fpx_sellerId seller-id
                     :fpx_sellerBankCode "01"
                     :fpx_txnCurrency "MYR"
                     :fpx_txnAmount "1.00"
                     :fpx_buyerEmail ""
                     :fpx_buyerName ""
                     :fpx_buyerBankId ""
                     :fpx_buyerBankBranch ""
                     :fpx_buyerAccNo ""
                     :fpx_buyerId ""
                     :fpx_makerName ""
                     :fpx_buyerIban ""
                     :fpx_productDesc "product description"
                     :fpx_version fpx-version}
        validation (str/join "|" ((apply juxt checksum-fields) form-params))
        checksum (-> validation
                     (sig/sign {:private-key (:merchant-key pki)})
                     (str/upper-case))]
    {:url (:auth-request endpoints)
     :form-params (assoc form-params :fpx_checkSum checksum)}))

(comment
  (let [{:keys [url form-params]} (authorization-request (config :dev))
        resp @(http/post url {:form-params form-params
                              :as :text})]
    resp))

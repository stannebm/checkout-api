(ns stanne.fpx.ae
  (:require
   [clj-http.client :as client]
   [clojure.string :as str]
   [stanne.fpx.ar :as ar]
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

(defn authorization-enquiry
  [ar-message {:keys [pki endpoints]}]
  (let [msg-type "AE"
        form-params (assoc ar-message :fpx_msgType msg-type)
        validation (str/join "|" ((apply juxt checksum-fields) form-params))
        checksum (-> validation
                     (utils/sign {:private-key (:merchant-key pki)})
                     (str/upper-case))]
    {:url (:auth-enquiry endpoints)
     :form-params (assoc form-params :fpx_checkSum checksum)}))

(comment
  (let [{:keys [form-params]} (ar/authorization-request (fpx/config :dev))
        {:keys [url form-params]} (authorization-enquiry form-params (fpx/config :dev))
        resp (client/post url {:form-params form-params})]
    resp))

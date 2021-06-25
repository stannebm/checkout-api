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

(defn call-authorization-enquiry [ar-params config]
  (let [{:keys [url form-params]} (authorization-enquiry ar-params config)]
    (client/post url {:form-params form-params})))

(comment
  (let [ar (ar/authorization-request "100.00" (fpx/config :dev))
        _ (client/post (ar :url) {:form-params (ar :form-params)})]
    (authorization-enquiry (ar :form-params) (fpx/config :dev)))

  (let [config (fpx/config :dev)
        ar (ar/authorization-request "100.00" config)
        _ (client/post (ar :url) {:form-params (ar :form-params)})]
    (prn "First AE call:")
    (-> (call-authorization-enquiry (ar :form-params) config)
        :body
        utils/parse-nvp
        prn)))

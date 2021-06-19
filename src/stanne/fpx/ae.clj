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
  (let [ar (ar/authorization-request "100.00" (fpx/config :dev))
        ar-params (ar :form-params)
        {:keys [url form-params]} (authorization-enquiry ar-params (fpx/config :dev))
        resp (client/post url {:form-params form-params})]
    resp)

  (let [body

        "fpx_debitAuthCode=76&fpx_debitAuthNo=&fpx_sellerExId=EX00011982&fpx_creditAuthNo=&fpx_buyerName=&fpx_buyerId=&fpx_sellerTxnTime=20210619215335&fpx_sellerExOrderNo=20210619215335&fpx_makerName=&fpx_buyerBankBranch=&fpx_buyerBankId=TEST0021&fpx_msgToken=01&fpx_creditAuthCode=&fpx_sellerId=SE00013501&fpx_fpxTxnTime=&fpx_buyerIban=&fpx_sellerOrderNo=20210619215335&fpx_txnAmount=100.00&fpx_fpxTxnId=&fpx_checkSum=4B2868A4C281369B77B5FA496E8E27CFD065243B282CC813297FE8E3AC2F421511B929C23C835CBEFA817D13DDA87A6C620BC9DE7BF0FCBA8F8B9CB214EA8455183BF86CC07ADCC7A3275A76AAC054A00B33BC9999878CF0CCDA199953DA4D537D64149A322BF6DAC70DF52BCC0AD9C8070733A5F6D1914538DEB3B74FD3433FBFAD7218A3128D30F45DCE7F77C26AA08E807645BB1FC38CF7F610D55E1139DB4AAD0EEC8EF36A8D6EB8A0F6128AEA5980B4698D18A18A6F8C1CEB6416681EB9B43D2728636E25505F0748115C41F71A21531CEF2E6B3FE8E1BBEF17EFAC5B27DECB4D294FB4F97FE130F97EBD4DF0DA18941E203FF99F69A348C55F9726594E&fpx_msgType=AC&fpx_txnCurrency=MYR"]
    (doseq [s (str/split body #"&")]
      (prn s))))

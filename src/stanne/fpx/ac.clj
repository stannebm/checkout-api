(ns stanne.fpx.ac
  (:require [clojure.string :as str]
            [stanne.fpx.utils :as utils]))

;; refer src/stubs.clj

(def ^:private checksum-fields
  [:fpx_buyerBankBranch
   :fpx_buyerBankId
   :fpx_buyerIban
   :fpx_buyerId
   :fpx_buyerName
   :fpx_creditAuthCode
   :fpx_creditAuthNo
   :fpx_debitAuthCode
   :fpx_debitAuthNo
   :fpx_fpxTxnId
   :fpx_fpxTxnTime
   :fpx_makerName
   :fpx_msgToken
   :fpx_msgType
   :fpx_sellerExId
   :fpx_sellerExOrderNo
   :fpx_sellerId
   :fpx_sellerOrderNo
   :fpx_sellerTxnTime
   :fpx_txnAmount
   :fpx_txnCurrency])

(defn authorization-confirmation
  "Callback from FPX"
  [{:keys [fpx_checkSum fpx_debitAuthCode]
    :as ac-response}
   {:keys [config bank-mapping]}]
  (let [msg (str/join "|" ((apply juxt checksum-fields) ac-response))
        signature fpx_checkSum
        public-key (-> config :pki :fpx-cert)
        checksum-ok? (utils/verify msg signature {:public-key public-key})
        ac-response' (assoc ac-response
                            :bank (bank-mapping (:fpx_buyerBankId ac-response)))]

    (when-not checksum-ok?
      (throw (ex-info "Invalid AC checksum"
                      {:api :AC
                       :msg msg
                       :signature signature
                       :public-key public-key})))

    {:status
     (case fpx_debitAuthCode
       "00" :ok
       "99" :pending-authorization
       :failed)

     :status-simple
     (case fpx_debitAuthCode
       "00" "OK"
       "99" "OK"
       "FAILED")

     :relevant-info
     (select-keys ac-response' [:bank
                                :fpx_fpxTxnId
                                :fpx_fpxTxnTime
                                :fpx_sellerOrderNo
                                :fpx_txnAmount
                                :fpx_txnCurrency
                                :fpx_debitAuthCode])}))

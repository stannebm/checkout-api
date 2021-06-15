(ns stanne.fpx.ac
  (:require [clojure.string :as str]
            [stanne.fpx.utils :as utils]))

;; UAT form values from FPX
#_{"fpx_sellerExId" "EX00011982",
   "fpx_txnCurrency" "MYR",
   "fpx_creditAuthCode" "00",
   "fpx_buyerId" "",
   "fpx_makerName" "New Simulator",
   "fpx_sellerExOrderNo" "20210615190651",
   "fpx_fpxTxnTime" "20210615190658",
   "fpx_sellerId" "SE00013501",
   "fpx_fpxTxnId" "2106151906580535",
   "fpx_buyerBankId" "TEST0021",
   "fpx_checkSum" "3973B47AE97FAFC2B7820B72DC2BE471FB9801C75DE117FA7F16D80F54C290226D3134A12395375EED12E8A02044F716BD30206A3E6C0B608D5CD0C2B463088824DAAC4F8BA4AC7A0BFCC132C73528E3983B1C3495C6DCF3A93DE3EA65A5BD0AC62D18F0C61EB72E7DA3E1388E325C4713E1E9E2A8301BD52CFE1A80A3895092204FE8ED211236A7BBEA786C5F53AE3C2869034ABF022A7E05F3333F59BACA56B3EB6748CC376CE4020C59D116BF55D1D216F352FC0F18538DB2DEC178A43E14806543B23C34CE8EB51B6527B66C3D21B3CB5EF802CE71D7A47CBB928DF4EAA47DC78453DF197ABECD48E8AFEB22413556510270164B46AC19CEC54BC33433F8",
   "fpx_sellerTxnTime" "20210615190651",
   "fpx_creditAuthNo" "9999999999",
   "fpx_debitAuthCode" "00",
   "fpx_msgToken" "01",
   "fpx_debitAuthNo" "15733223",
   "fpx_buyerIban" "",
   "fpx_txnAmount" "1.00",
   "fpx_sellerOrderNo" "20210615190651",
   "fpx_msgType" "AC",
   "fpx_buyerName" "N@ME()/ .-&BUYER",
   "fpx_buyerBankBranch" "SBI BANK A"}

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
   {:keys [pki]}]
  (let [msg (str/join "|" ((apply juxt checksum-fields) ac-response))
        signature fpx_checkSum
        public-key (pki :fpx-cert)
        checksum-ok? (utils/verify msg signature {:public-key public-key})]
    (when-not checksum-ok?
      (throw (ex-info "Invalid AC checksum"
                      {:api :AC
                       :msg msg
                       :signature signature
                       :public-key public-key}
                      :checksum-error)))
    {:status
     (case fpx_debitAuthCode
       "00" :ok
       "99" :pending-authorization
       :failed)

     :relevant-info
     (select-keys ac-response [:fpx_buyerBankId
                               :fpx_fpxTxnId
                               :fpx_txnAmount
                               :fpx_txnCurrency
                               :fpx_debitAuthCode])}))

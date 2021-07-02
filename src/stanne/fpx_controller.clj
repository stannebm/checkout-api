(ns stanne.fpx-controller
  (:require
   [clojure.edn :as edn]
   [io.pedestal.http.body-params :refer [form-parser]]
   [io.pedestal.log :as log]
   [ring.util.response :as r]
   [stanne.fpx.ac :refer [authorization-confirmation]]
   [stanne.fpx.core :as fpx]
   [stanne.fpx.settings :as settings]
   [stanne.repo :as repo]
   [stanne.views.error-msg :refer [error-msg-view]]
   [stanne.views.fpx-home :refer [home-view]]
   [stanne.views.fpx-indirect :refer [indirect-view]]))

(defn- fpx-settings [env]
  {:config (settings/config env)
   :bank-mapping (settings/bank-mapping env)})

(defn fpx-home
  "Render a form that POST to FPX's AR endpoint"
  [{:keys [app-env params]}]
  (let [{:keys [reference_no amount email name]} params
        amount' (-> amount edn/read-string float)
        amount'' (format "%.2f" amount')
        fpx-params (and (pos? amount')
                        (fpx/mk-params {:reference-no reference_no
                                        :amount amount''
                                        :email email
                                        :name name
                                        :env app-env}))
        render-err #(r/response (error-msg-view %))]
    (cond
      (not (float? amount')) (render-err "Missing transaction amount")
      (< amount' 1) (render-err "Invalid transaction amount (Less than RM1)")
      (> amount' 30000) (render-err "Invalid transaction amount (More than RM30,000)")
      :else (r/response (home-view fpx-params)))))

(defn fpx-callback-direct
  "FPX direct AC callback (text)"
  [{:keys [app-env]
    :as request}]
  (let [form-params (-> request form-parser :form-params)
        ac (authorization-confirmation form-params (fpx-settings app-env))
        {:keys [reference-no status-simple]} ac]
    (log/info :event :fpx-ac-direct
              :form-params form-params
              :reference-no reference-no
              :status status-simple
              :ac-full-info ac)
    (repo/save-txn-info {:env app-env
                         :provider :fpx
                         :status status-simple
                         :reference-no reference-no
                         :info {:ac ac}})
    (r/response status-simple)))

(defn fpx-callback-indirect
  "FPX indirect AC callback (HTML)"
  [{:keys [app-env]
    :as request}]
  (let [form-params (-> request form-parser :form-params)
        ac (authorization-confirmation form-params (fpx-settings app-env))]
    (r/response (indirect-view ac))))

(comment
  {:event :fpx-ac-direct,
   :form-params {:fpx_txnCurrency "MYR",
                 :fpx_sellerId "SE00013501",
                 :fpx_sellerExId "EX00011982",
                 :fpx_buyerBankBranch "SBI BANK A",
                 :fpx_buyerIban "",
                 :fpx_sellerExOrderNo "1625196222784",
                 :fpx_sellerOrderNo "1625196222784",
                 :fpx_fpxTxnTime "20210702112419",
                 :fpx_creditAuthNo "9999999999",
                 :fpx_fpxTxnId "2107021124190125",
                 :fpx_msgToken "01",
                 :fpx_debitAuthNo "15733223",
                 :fpx_debitAuthCode "00",
                 :fpx_sellerTxnTime "20210702052343",
                 :fpx_creditAuthCode "00",
                 :fpx_buyerBankId "TEST0021",
                 :fpx_buyerId "",
                 :fpx_txnAmount "100.01",
                 :fpx_buyerName "N@ME()/ .-&BUYER",
                 :fpx_makerName "New Simulator",
                 :fpx_msgType "AC",
                 :fpx_checkSum "1F7333FAB1B00868B17FBD80F7D9C928BAD24786F694185208E4EEA41168393718A9A89BB50003F6C09C6A590CBB30E75D403079F5A77E2F7E91A15597BE50A0E4252A48BA8366AED1CC0788A5F8764E68FE1712F17CB061891A5630174A48D61EB12065F41793901B6603224CBEE9C8EB3D7259D36D537948F4EBDD9CE88CE2A0E8CF5518D3F74F0DF99827FB3DF11A061F9E8FCADFC84B2BDA346C82674DF1A43D4444324186EA849D6C806D474E82A5D9FD4C84710339609F857EEC9DF5ADEDF37E2F5ADBB7B38BF13D95314802EEF0FFEEC54F038F1D6D9516CA3E720B1557627C658E3264E950198048495DC134D3867E3281165E4F868B48DDC6FC296B"},
   :reference-no "1625196222784",
   :status "OK",
   :ac-full-info {:status :ok,
                  :reference-no "1625196222784",
                  :status-simple "OK",
                  :relevant-info {:bank "SBI Bank A",
                                  :fpx_fpxTxnId "2107021124190125",
                                  :fpx_fpxTxnTime "20210702112419",
                                  :fpx_sellerOrderNo "1625196222784",
                                  :fpx_txnAmount "100.01",
                                  :fpx_txnCurrency "MYR",
                                  :fpx_debitAuthCode "00"}},
   :line 45})

(ns stanne.cybersource-controller
  (:require
   [clojure.edn :as edn]
   [io.pedestal.http.body-params :refer [form-parser]]
   [io.pedestal.log :as log]
   [ring.util.response :as r]
   [stanne.cybersource.core :refer [config mk-params mk-signature]]
   [stanne.repo :as repo]
   [stanne.views.cybersource-home :refer [home-view]]
   [stanne.views.cybersource-receipt :refer [receipt-view]]
   [clojure.string :as str]))

(defn cybersource-home
  "Render a form that POST to CyberSource hosted checkout"
  [{:keys [params]}]
  (let [{:keys [reference_no amount email name]} params
        amount' (-> amount edn/read-string float)
        amount'' (format "%.2f" amount')
        cs-params (and (pos? amount') (mk-params {:reference-no reference_no
                                                  :amount amount''
                                                  :email email
                                                  :name name}))
        cs-signature (mk-signature cs-params)]
    (r/response (home-view {:cs-params cs-params
                            :cs-signature cs-signature
                            :cs-endpoint (config :endpoint)}))))

(defn cybersource-done-notify
  "Host-to-host callback from CyberSource"
  [{:keys [app-env]
    :as request}]
  (let [params (-> request form-parser :form-params)
        {:keys [req_reference_number decision]} params]
    (log/info :event :cybersource-notify
              :params params
              :reference-no req_reference_number
              :status (case decision
                        "ACCEPT" :ok
                        :failed))
    (repo/save-txn-info {:env app-env
                         :provider "cybersource"
                         :status (case decision
                                   "ACCEPT" "OK"
                                   "FAILED")
                         :reference-no req_reference_number
                         :info {:cybersource-notify params}})
    (r/response "OK")))

(defn cybersource-receipt
  "Host-to-host callback from CyberSource"
  [request]
  (let [params (-> request form-parser :form-params)
        {:keys [req_reference_number
                auth_amount decision
                transaction_id
                req_card_number
                signed_date_time
                card_type_name]} params
        transaction-time (-> signed_date_time
                             (str/replace #"T" " ")
                             (str/replace #"Z" ""))]
    (r/response (receipt-view
                 {:reference-no req_reference_number
                  :amount (str "RM" auth_amount)
                  :transaction-time transaction-time
                  :transaction-id transaction_id
                  :card-number req_card_number
                  :card-type card_type_name
                  :status (case decision
                            "ACCEPT" :ok
                            :failed)}))))

(comment
  ;; notify params
  {:req_card_number "xxxxxxxxxxxx1111",
   :req_transaction_type "authorization",
   :auth_amount "100.23",
   :transaction_id "6248109337436017303004",
   :reason_code "100",
   :signed_field_names "auth_cv_result,req_card_number,req_locale,req_payer_authentication_indicator,req_card_type_selection_indicator,auth_trans_ref_no,req_card_expiry_date,auth_cavv_result,card_type_name,reason_code,auth_amount,auth_response,req_payment_method,request_token,req_payer_authentication_merchant_name,auth_cavv_result_raw,auth_time,req_amount,transaction_id,req_currency,req_card_type,decision,message,req_transaction_uuid,auth_avs_code,auth_code,req_transaction_type,req_access_key,auth_cv_result_raw,req_profile_id,req_reference_number,signed_field_names,signed_date_time",
   :req_payer_authentication_indicator "01",
   :req_card_type "001",
   :auth_avs_code "1",
   :card_type_name "Visa",
   :signed_date_time "2021-06-27T16:22:14Z",
   :req_profile_id "1E3AE0C5-596F-4A3D-BD0F-A2F49432805D",
   :auth_cavv_result "2",
   :req_payer_authentication_merchant_name "M. BASILICA OF ST ANNE",
   :signature "5agdyx+GS5ciUNoa+lDJDQ3ym47sUJXNwSQH3DqjFKU=",
   :auth_trans_ref_no "6248109337436017303004",
   :req_currency "MYR",
   :req_access_key "a3400cbb889139118f9319042853f565",
   :request_token "Axj/7wSTUrXvgn77Fp3cABos2ZNHDFg5Zs27Rm2YMW7NgzYMGiiOJJuJhQFRHEk3EwrlDpycSahk0ky9GLHW7iBOTUrXvgn77Fp3cAAA7BMD",
   :req_card_type_selection_indicator "1",
   :req_reference_number "1624810613872",
   :req_amount "100.23",
   :req_card_expiry_date "01-2024",
   :auth_cv_result "M",
   :req_transaction_uuid "6fa70b03eefc4d4d9ad5c85cf489e5cb",
   :auth_time "2021-06-27T162214Z",
   :auth_cv_result_raw "M",
   :req_locale "en",
   :req_payment_method "card",
   :auth_response "00",
   :auth_cavv_result_raw "2",
   :auth_code "831000",
   :decision "ACCEPT",
   :message "Request was processed successfully."})

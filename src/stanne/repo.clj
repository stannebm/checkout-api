(ns stanne.repo
  (:require
   [buddy.core.codecs :as codecs]
   [buddy.core.mac :as mac]
   [clj-http.client :as client]
   [clojure.string :as str]
   [environ.core :as environ]
   [io.pedestal.log :as log]))

(defn- repo-backend [env]
  (case env
    ;; :dev "http://localhost:4000/api/payment_notification"
    :dev "https://admin.minorbasilicastannebm.com/api/payment_notification"
    :prod "https://admin.minorbasilicastannebm.com/api/payment_notification"))

(defn- hmac-sign
  "Assure the backend on msg integrity"
  [data secret]
  (-> (mac/hash data {:key secret
                      :alg :hmac+sha256})
      (codecs/bytes->b64)
      (codecs/bytes->str)))

(defn- notify-backend [{:keys [backend status reference-no sig info]}]
  (let [form-params {:txn_info
                     {:status status
                      :reference_no reference-no
                      :signature sig
                      :info info}}]
    (log/info :event :notify-backend
              :details {:backend backend
                        :form-params form-params})
    (try
      (client/post backend {:form-params form-params
                            :content-type :json})
      (catch Exception e
        (log/warn :event :notify-backend-err
                  :backend backend
                  :form-params form-params
                  :err e)))))

(defn save-txn-info
  "Inform backend about the IPN callback from FPX/Cybersource"
  [{:keys [env provider status reference-no info]}]
  {:pre [(contains? #{:dev :prod} env)
         (string? provider)
         (string? status)
         (string? reference-no)
         (not-empty info)]}

  (let [backend (str (repo-backend env) "/" provider)
        secret (environ/env :checkout-api-secret)
        sig (hmac-sign (str/join "|" [provider reference-no status]) secret)
        api-response (notify-backend {:backend backend
                                      :status status
                                      :reference-no reference-no
                                      :sig sig
                                      :info info})]
    (log/info :event :payment-notification
              :details {:response api-response})))

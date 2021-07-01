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

(defn save-txn-info
  "Inform backend about the IPN callback from FPX/Cybersource"
  [{:keys [env provider reference-no info status]}]
  (let [backend (str (repo-backend env) "/" provider)
        secret (environ/env :checkout-api-secret)
        sig (hmac-sign (str/join "|" [provider reference-no status]) secret)]
    (letfn [(call-api
              []
              (client/post backend
                           {:form-params
                            {:txn_info {:status status
                                        :reference_no reference-no
                                        :signature sig
                                        :info info}}
                            :content-type :json}))]
      (log/info :event :save-txn-callback
                :details {:response (call-api)}))))

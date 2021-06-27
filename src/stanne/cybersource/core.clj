(ns stanne.cybersource.core
  (:require
   [clojure.spec.alpha :as s]
   [clojure.string :as str]
   [environ.core :refer [env]]
   [stanne.cybersource.utils :refer [current-date-time mk-hmac-hash txn-uuid]]))

(def config
  {:endpoint (env :cybersource-post-endpoint)
   :profile-id (env :cybersource-profile-id)
   :access-key (env :cybersource-access-key)
   :secret (env :cybersource-secret)
   :currency "MYR"})

(def ^:private required-fields
  [::access_key
   ::profile_id
   ::transaction_type
   ::transaction_uuid
   ::reference_number
   ::amount
   ::currency
   ::locale
   ::signed_field_names
   ::unsigned_field_names
   ::signed_date_time])

(s/def ::currency #{"MYR"})
(s/def ::locale #{"en"})
(s/def ::reference_number string?)
(s/def ::amount (s/and string? #(re-matches #"\d+\.\d{2}" %)))
(s/def ::signed_date_time string?) ;; 2021-06-27T02:17:41Z
(s/def ::transaction_uuid string?)
(s/def ::required-signed-fields
  (s/keys :req (vec required-fields)))

(defn- default-params [{:keys [access-key profile-id currency]}]
  {::access_key access-key
   ::profile_id profile-id
   ::transaction_type "authorization"
   ::transaction_uuid (txn-uuid)
   ::currency currency
   ::locale "en"
   ::signed_date_time (current-date-time)
   ::signed_field_names (->> (map name required-fields) (str/join ","))
   ::unsigned_field_names ""})

(defn- to-nvp [params]
  (->> (for [f required-fields]
         (str/join "=" [(name f) (params f)]))
       (str/join ",")))

(defn mk-params [reference-number amount]
  (let [params (-> config
                   default-params
                   (merge {::reference_number reference-number
                           ::amount (format "%.2f" amount)}))
        parsed (s/conform ::required-signed-fields params)]
    (if (s/invalid? parsed)
      (throw (ex-info "Invalid input" (s/explain-data ::required-signed-fields params)))
      parsed)))

(defn mk-signature [cs-params]
  (-> (to-nvp cs-params)
      (mk-hmac-hash (config :secret))))

(comment
  (let [cs-params (mk-params "ref1" "12.10")]
    (mk-signature cs-params))

  ;; verify hash against provide SDK
  (let [data "access_key=a3400cbb889139118f9319042853f565,profile_id=1E3AE0C5-596F-4A3D-BD0F-A2F49432805D,transaction_uuid=c23ddac7da1b364bc82e15e923d103ce,signed_field_names=access_key,profile_id,transaction_uuid,signed_field_names,unsigned_field_names,signed_date_time,locale,transaction_type,reference_number,amount,currency,unsigned_field_names=,signed_date_time=2021-06-27T09:41:13Z,locale=en,transaction_type=authorization,reference_number=1624786872993,amount=100.00,currency=USD"]
    (mk-hmac-hash data (config :secret))))

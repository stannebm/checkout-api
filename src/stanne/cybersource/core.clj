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
   ::signed_date_time

   ;; 3Ds Secure V2 fields
   ::bill_to_forename
   ::bill_to_surname
   ::bill_to_email
   ::bill_to_address_line1
   ::bill_to_address_city
   ::bill_to_address_state
   ::bill_to_address_postal_code
   ::bill_to_address_country])

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
   ::transaction_type "sale"
   ::transaction_uuid (txn-uuid)
   ::currency currency
   ::locale "en"
   ::signed_date_time (current-date-time)
   ::signed_field_names (->> (map name required-fields) (str/join ","))
   ::unsigned_field_names ""

   ;; default address to pass payer authentication
   ::bill_to_address_line1 "Charleston Rd"
   ::bill_to_address_city "Mountain View"
   ::bill_to_address_state "CA"
   ::bill_to_address_postal_code "94043"
   ::bill_to_address_country "US"})

(defn- to-nvp [params]
  (str/join
   ","
   (for [f required-fields]
     (str/join "=" [(name f) (params f)]))))

(defn mk-params [{:keys [reference-no amount email name]}]
  (let [params (-> config
                   default-params
                   (merge {::reference_number reference-no
                           ::amount amount
                           ::bill_to_forename name
                           ::bill_to_surname name
                           ::bill_to_email email}))
        parsed (s/conform ::required-signed-fields params)]
    (if (s/invalid? parsed)
      (throw (ex-info "Invalid input" (s/explain-data ::required-signed-fields params)))
      parsed)))

(defn mk-signature [cs-params]
  (let [nvp (to-nvp cs-params)]
    (prn "Signed fields:" (cs-params ::signed_field_names))
    (prn "NVP:" nvp)
    (mk-hmac-hash nvp (config :secret))))

(comment
  (let [cs-params (mk-params {:reference-no "1624810613183"
                              :amount "100.23"
                              :email "null@cybersource.com"
                              :name "noreal"})]
    (mk-signature cs-params))

  ;; verify hash against provide SDK
  (let [data "access_key=a3400cbb889139118f9319042853f565,profile_id=1E3AE0C5-596F-4A3D-BD0F-A2F49432805D,transaction_uuid=c23ddac7da1b364bc82e15e923d103ce,signed_field_names=access_key,profile_id,transaction_uuid,signed_field_names,unsigned_field_names,signed_date_time,locale,transaction_type,reference_number,amount,currency,unsigned_field_names=,signed_date_time=2021-06-27T09:41:13Z,locale=en,transaction_type=authorization,reference_number=1624786872993,amount=100.00,currency=USD"]
    (mk-hmac-hash data (config :secret))))

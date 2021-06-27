(ns stanne.cybersource.core
  (:require [environ.core :refer [env]]
            [clojure.spec.alpha :as s]
            [stanne.cybersource.utils :refer [txn-uuid current-date-time parse-num-str to-nvp mk-hmac-hash]]
            [clojure.string :as str]))

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
(s/def ::amount pos?)
(s/def ::signed_date_time string?) ;; 2021-06-27T02:17:41Z
(s/def ::transaction_uuid uuid?)
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

(defn mk-params [reference-number amount]
  (let [params (-> config
                   default-params
                   (merge {::reference_number reference-number
                           ::amount (parse-num-str amount)}))
        parsed (s/conform ::required-signed-fields params)]

    (if (s/invalid? parsed)
      (throw (ex-info "Invalid input" (s/explain-data ::required-signed-fields params)))
      parsed)))

(comment
  (let [params (mk-params "ref1" "12.10")]
    (-> (to-nvp params)
        (mk-hmac-hash config))))

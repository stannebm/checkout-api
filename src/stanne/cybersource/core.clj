(ns stanne.cybersource.core
  (:require [environ.core :refer [env]]
            [clojure.spec.alpha :as s]))

;;; SPEC ;;;

(s/def ::currency #{"MYR"})
(s/def ::locale #{"en"})
(s/def ::signed_date_time string?) ;; 2021-06-27T02:17:41Z
(s/def ::required-signed-fields
  (s/keys :req-un
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
           ::signed_date_time]))

;;; Main ;;;


(defn config []
  {:endpoint (env :cybersource-post-endpoint)
   :profile-id (env :cybersource-profile-id)
   :access-key (env :cybersource-access-key)
   :secret (env :cybersource-secret)
   :currency "MYR"})

(defn default-params [{:keys [access-key profile-id currency]}]
  {:access_key access-key
   :profile_id profile-id
   :transaction_type "authorization"
   :currency currency
   :locale "en"
   :signed_field_names "access_key,profile_id,transaction_uuid,signed_field_names,unsigned_field_names,signed_date_time,locale,transaction_type,reference_number,amount,currency"
   :unsigned_field_names ""})

(config)
(default-params (config))

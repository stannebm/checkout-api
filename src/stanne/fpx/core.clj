(ns stanne.fpx.core
  (:require
   [clojure.spec.alpha :as s]
   [stanne.fpx.ar :as ar]
   [stanne.fpx.settings :as settings]))

(s/def ::amount-type (s/and string? #(re-matches #"\d+\.\d{2}" %)))
(s/def ::ar-params
  (s/keys :req-un
          [::url ::banks ::checksums ::form-params ::tnc]))

(defn mk-params [reference-no amount env]
  {:pre [(s/valid? string? reference-no)
         (s/valid? ::amount-type amount)
         (s/valid? keyword? env)]
   :post [(s/valid? ::ar-params %)]}
  (let [ar (ar/authorization-request {:reference-no reference-no
                                      :amount amount
                                      :fpx-config (settings/config env)
                                      :bank-mapping (settings/bank-mapping env)})]
    ar))

(comment
  ;; generate refence-no with `new Date().getTime()`
  (mk-params "1624804617508" "10.11" :dev))

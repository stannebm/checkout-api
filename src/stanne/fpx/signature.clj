(ns stanne.fpx.signature
  (:require
   [buddy.core.dsa :as dsa]
   [buddy.core.keys :as keys]
   [buddy.core.codecs :refer [bytes->hex hex->bytes]])
  (:import
   java.security.Signature))

(def algorithm
  ;; BC = bouncycastle
  #(Signature/getInstance "SHA1withRSA" "BC"))

(defn timestamp-id []
  (let [date (java.util.Date.)
        formatter (java.text.SimpleDateFormat. "yyyyMMddHHmmss")]
    (.format formatter date)))

(defn sign [msg {:keys [private-key]}]
  (let [privkey (keys/private-key private-key)]
    (-> msg
        (dsa/sign {:key privkey
                     ;; BC = bouncycastle
                   :alg algorithm})
        bytes->hex)))

(defn verify [msg signature {:keys [public-key]}]
  (let [pubkey (keys/public-key public-key)
        signature-b (hex->bytes signature)]
    (-> msg
        (dsa/verify signature-b {:key pubkey
                                 :alg algorithm}))))

(ns stanne.cybersource.utils
  (:require [clojure.edn :as edn]
            [buddy.core.mac :as mac]
            [clojure.string :as str]
            [buddy.core.codecs :as codecs]))

(defn txn-uuid []
  (java.util.UUID/randomUUID))

(defn current-date-time []
  (let [date (java.util.Date.)
        formatter (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss'Z'")
        tz (java.util.TimeZone/getTimeZone "UTC")
        _ (.setTimeZone formatter tz)]
    (.format formatter date)))

(defn parse-num-str [amount]
  (edn/read-string amount))

(defn to-nvp [params]
  (->> [(name n) v]
       (str/join "=")
       (for [[n v] params])
       (str/join ",")))

(defn mk-hmac-hash [data {:keys [secret]}]
  (-> (mac/hash data {:key secret
                      :alg :hmac+sha256})
      (codecs/bytes->b64)
      (codecs/bytes->str)))

(comment
  (current-date-time)
  (txn-uuid))

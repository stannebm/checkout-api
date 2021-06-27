(ns stanne.cybersource.utils
  (:require
   [buddy.core.codecs :as codecs]
   [buddy.core.mac :as mac]
   [clojure.string :as str]))

(defn txn-uuid []
  (-> (java.util.UUID/randomUUID) .toString (str/replace #"-" "")))

(defn current-date-time []
  (let [date (java.util.Date.)
        formatter (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss'Z'")
        tz (java.util.TimeZone/getTimeZone "UTC")
        _ (.setTimeZone formatter tz)]
    (.format formatter date)))

(defn mk-hmac-hash [data secret]
  (-> (mac/hash data {:key secret
                      :alg :hmac+sha256})
      (codecs/bytes->b64)
      (codecs/bytes->str)))

(comment
  (current-date-time)
  (txn-uuid))

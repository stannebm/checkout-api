(ns stanne.cybersource.utils
  (:require [clojure.edn :as edn]))

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

(comment
  (current-date-time)
  (txn-uuid))

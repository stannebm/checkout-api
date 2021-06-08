(ns stanne.fpx.url
  (:require [clojure.string :as str]))

(def ^:private string-replace-bug?
  (= "x" (str/replace "x" #"." (fn [x] "$0"))))

(defn- double-escape [^String x]
  (.replace (.replace x "\\" "\\\\") "$" "\\$"))

(defmacro fix-string-replace-bug [x]
  (if string-replace-bug?
    `(double-escape ~x)
    x))

(defn- parse-bytes [encoded-bytes]
  (->> (re-seq #"%[A-Za-z0-9]{2}" encoded-bytes)
       (map #(subs % 1))
       (map #(.byteValue (Integer/valueOf % 16)))
       (byte-array)))

(defn percent-decode
  "Decode every percent-encoded character in the given string using the
  specified encoding, or UTF-8 by default."
  ([encoded]
   (percent-decode encoded "UTF-8"))
  ([^String encoded ^String encoding]
   (str/replace encoded
                #"(?:%[A-Za-z0-9]{2})+"
                (fn [chars]
                  (-> ^bytes (parse-bytes chars)
                      (String. encoding)
                      (fix-string-replace-bug))))))

(defn ^String url-decode
  "Returns the url-decoded version of the given string, using either a specified
  encoding or UTF-8 by default. If the encoding is invalid, nil is returned."
  ([encoded]
   (url-decode encoded "UTF-8"))
  ([encoded encoding]
   (percent-decode encoded encoding)))

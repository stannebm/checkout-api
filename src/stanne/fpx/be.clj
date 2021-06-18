(ns stanne.fpx.be
  (:require
   [clojure.string :as str]
   [clj-http.client :as client]
   [stanne.fpx.core :as fpx]
   [stanne.fpx.utils :as utils]))

(def ^:private bank-status {"A" :available
                            "B" :blocked})

(defn- bank-list-request [{:keys [exchange-id msg-token fpx-version pki endpoints]}]
  (let [msg-type "BE"
        url (:bank-list endpoints)
        request-params (str/join "|" [msg-token msg-type exchange-id fpx-version])
        signature (-> request-params
                      (utils/sign {:private-key (:merchant-key pki)}))
        form-params {:fpx_msgType msg-type
                     :fpx_msgToken msg-token
                     :fpx_sellerExId exchange-id
                     :fpx_version fpx-version
                     :fpx_checkSum signature}]
    {:url url
     :form-params form-params}))

(defn bank-list [config bank-mapping]
  (let [{:keys [url form-params]} (bank-list-request config)
        resp (client/post url {:form-params form-params})
        info (-> resp :body utils/parse-nvp)
        response-params (->> info
                             ((apply juxt [:fpx_bankList
                                           :fpx_msgToken
                                           :fpx_msgType
                                           :fpx_sellerExId]))
                             (str/join "|"))
        signature (:fpx_checkSum info)
        public-key (-> config :pki :fpx-cert)
        checksum-ok? (utils/verify response-params signature {:public-key public-key})
        split-by (fn [re] (fn [s] (str/split s re)))
        as-tuple (fn [[a b]]
                   [a {:code a
                       :name (bank-mapping a)
                       :status (bank-status b)}])
        all-banks (->> info
                       :fpx_bankList
                       ((split-by #","))
                       (map (comp as-tuple (split-by #"~")))
                       (sort-by (comp :name second))
                       (filter (fn [[_ v]] (= :available (:status v)))))]
    (when-not checksum-ok?
      (throw (ex-info "Invalid BE checksum"
                      {:api :BE
                       :response-params response-params
                       :signature signature
                       :public-key public-key})))
    all-banks))

(comment
  ;; bank list
  (bank-list (fpx/config :dev)
             (fpx/bank-mapping :dev)))

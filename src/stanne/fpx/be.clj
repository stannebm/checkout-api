(ns stanne.fpx.be
  (:require
   [clojure.string :as str]
   [org.httpkit.client :as http]
   [stanne.fpx.common :as common]
   [stanne.fpx.utils :as utils]))

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

(defn bank-list [config banks]
  (let [{:keys [url form-params]} (bank-list-request config)
        resp @(http/post url {:form-params form-params
                              :as :text})
        info (-> resp :body utils/parse-nvp)
        response-params (->> info
                             ((apply juxt [:fpx_bankList
                                           :fpx_msgToken
                                           :fpx_msgType
                                           :fpx_sellerExId]))
                             (str/join "|"))
        signature (:fpx_checkSum info)
        public-key (-> config :pki :fpx-cert)
        checksum-ok? (utils/verify response-params signature {:public-key public-key})]
    (when-not checksum-ok?
      (throw (ex-info "Invalid checksum"
                      {:api "BE"
                       :response-params response-params
                       :signature signature
                       :public-key public-key})))
    (let [process (comp (fn [[a b]]
                          [a {:code a
                              :name (banks a)
                              :status b}])
                        #(str/split % #"~"))]
      (-> info
          :fpx_bankList
          (str/split #",")
          ((partial map process))
          ((partial into {}))))))

(comment
  ;; bank list
  (bank-list (common/config :dev)
             (common/bank-mapping :dev)))

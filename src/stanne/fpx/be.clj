(ns stanne.fpx.be
  (:require
   [clojure.string :as str]
   [org.httpkit.client :as http]
   [stanne.fpx.common :refer [config]]
   [stanne.fpx.utils :as utils]))

(defn- bank-list-request [{:keys [exchange-id msg-token fpx-version pki endpoints]}]
  (let [msg-type "BE"
        url (:bank-list endpoints)
        validation (str/join "|" [msg-token msg-type exchange-id fpx-version])
        checksum (-> validation
                     (utils/sign {:private-key (:merchant-key pki)}))
        form-params {:fpx_msgType msg-type
                     :fpx_msgToken msg-token
                     :fpx_sellerExId exchange-id
                     :fpx_version fpx-version
                     :fpx_checkSum checksum}]
    {:url url
     :form-params form-params
     :validation validation}))

(comment
  ;; bank list
  (let [{:keys [url form-params]} (bank-list-request (config :dev))
        resp @(http/post url {:form-params form-params
                              :as :text})]
    (:body resp)))

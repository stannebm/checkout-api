(ns stanne.fpx.be
  (:require
   [stanne.fpx.common :refer [config]]
   [clojure.string :as str]
   [org.httpkit.client :as http]
   [stanne.fpx.signature :as sig]))

(defn- bank-list-request [{:keys [exchange-id msg-token fpx-version pki endpoints]}]
  (let [msg-type "BE"
        url (:bank-list endpoints)
        validation (str/join "|" [msg-token msg-type exchange-id fpx-version])
        checksum (-> validation
                     (sig/sign {:private-key (:merchant-key pki)}))
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
    (:body resp))

  (def bank-list "fpx_msgToken=01&fpx_sellerExId=EX00011982&fpx_bankList=MBB0228%7EA%2CABMB0212%7EA%2CCIT0219%7EB%2CBIMB0340%7EA%2CBMMB0341%7EA%2CABB0233%7EA%2COCBC0229%7EA%2CHLB0224%7EA%2CABB0234%7EA%2CBCBB0235%7EA%2CAGRO01%7EA%2CHSBC0223%7EA%2CPBB0233%7EA%2CBKRM0602%7EA%2CMB2U0227%7EA%2CKFH0346%7EA%2CUOB0229%7EA%2CRHB0218%7EA%2CSCB0216%7EB%2CTEST0023%7EA%2CTEST0022%7EA%2CTEST0021%7EA%2CUOB0226%7EB%2CBSN0601%7EA%2CAMBB0209%7EA&fpx_checkSum=60D651A8330005B7139170C93C3883D78C07D79153E95F99F83E475B867A57428838E99964101B4C1C7D1C1A4996B95E505B069F1DF3AFAE7136708258FEE7A6C55F9E90A4148FD578E7CD61CC5F0E4916525AB2F139F0FC213EE8F82CD136C2E1F61FD649DEE2B7654C0A8D83BBC1DF0C3915D2AB8AAEC3FEEA1DA4E1559CBB5D76E3B1442AE2A84E2542A296881D43C48DDDE43C1BC21FCC23521F02E53D95DD83FCAD97FA4C26BDC7EAA87B512D2B667AB1F0CBFD5F70C48E657DF1D0E1618B6FDC90C639AD1BFBB23E2598BE41C7A13D31AD0F9E7EBFA6B9426778C1DC475B874E19B27ED938ED717F5687E91357C625C07AC4087F442738F9F94D05474C&fpx_msgType=BC"))

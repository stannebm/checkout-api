(ns stanne.cybersource-controller
  (:require
   [clojure.edn :as edn]
   [ring.util.response :as r]
   [stanne.cybersource.core :refer [config mk-params mk-signature]]
   [stanne.views.cybersource-home :refer [home-view]]))

(defn cybersource-home
  "Render a form that POST to CyberSource hosted checkout"
  [{:keys [params]}]
  (let [{:keys [reference amount]} params
        amount' (edn/read-string amount)
        cs-params (and (pos? amount') (mk-params reference amount'))
        cs-signature (mk-signature cs-params)]
    (prn "Params: " params)
    (prn "CS Params: " cs-params)
    (prn "Signature" cs-signature)
    (r/response (home-view {:cs-params cs-params
                            :cs-signature cs-signature
                            :cs-endpoint (config :endpoint)}))))

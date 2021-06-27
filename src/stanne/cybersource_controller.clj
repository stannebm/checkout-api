(ns stanne.cybersource-controller
  (:require
   [clojure.edn :as edn]
   [ring.util.response :as r]
   [stanne.cybersource.core :refer [config mk-params mk-signature]]
   [stanne.views.cybersource-home :refer [home-view]]
   [io.pedestal.log :as log]))

(defn cybersource-home
  "Render a form that POST to CyberSource hosted checkout"
  [{:keys [params]}]
  (let [{:keys [reference-no amount]} params
        amount' (-> amount edn/read-string float)
        amount'' (format "%.2f" amount')
        cs-params (and (pos? amount') (mk-params reference-no amount''))
        cs-signature (mk-signature cs-params)]
    (r/response (home-view {:cs-params cs-params
                            :cs-signature cs-signature
                            :cs-endpoint (config :endpoint)}))))

(defn cybersource-done-notify
  "Host-to-host callback from CyberSource"
  [{:keys [params]}]
  (log/info :event :cybersource-notify
            :params params)
  (r/response "OK"))

(defn cybersource-done-receipt
  "Host-to-host callback from CyberSource"
  [{:keys [params]}]
  {:status 200
   :body params
   :headers {"Content-Type" "text/plain"}})

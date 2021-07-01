(ns stanne.fpx-controller
  (:require
   [clojure.edn :as edn]
   [io.pedestal.http.body-params :refer [form-parser]]
   [io.pedestal.log :as log]
   [ring.util.response :as r]
   [stanne.fpx.ac :refer [authorization-confirmation]]
   [stanne.fpx.core :as fpx]
   [stanne.fpx.settings :as settings]
   [stanne.repo :as repo]
   [stanne.views.error-msg :refer [error-msg-view]]
   [stanne.views.fpx-home :refer [home-view]]
   [stanne.views.fpx-indirect :refer [indirect-view]]))

(defn- fpx-settings [env]
  {:config (settings/config env)
   :bank-mapping (settings/bank-mapping env)})

(defn fpx-home
  "Render a form that POST to FPX's AR endpoint"
  [{:keys [app-env params]}]
  (let [{:keys [reference_no amount email name]} params
        amount' (-> amount edn/read-string float)
        amount'' (format "%.2f" amount')
        fpx-params (and (pos? amount')
                        (fpx/mk-params {:reference-no reference_no
                                        :amount amount''
                                        :email email
                                        :name name
                                        :env app-env}))
        render-err #(r/response (error-msg-view %))]
    (cond
      (not (float? amount')) (render-err "Missing transaction amount")
      (< amount' 1) (render-err "Invalid transaction amount (Less than RM1)")
      (> amount' 30000) (render-err "Invalid transaction amount (More than RM30,000)")
      :else (r/response (home-view fpx-params)))))

(defn fpx-callback-direct
  "FPX direct AC callback (text)"
  [{:keys [app-env]
    :as request}]
  (let [form-params (-> request form-parser :form-params)
        ac (authorization-confirmation form-params (fpx-settings app-env))
        {:keys [[reference-no status-simple]]} ac]
    (log/info :event :fpx-ac-direct
              :form-params form-params
              :reference-no reference-no
              :status-simple status-simple
              :ac-full-info ac)
    (repo/save-txn-info {:env app-env
                         :provider :fpx
                         :status status-simple
                         :reference-no reference-no
                         :info {:ac ac}})
    (r/response status-simple)))

(defn fpx-callback-indirect
  "FPX indirect AC callback (HTML)"
  [{:keys [app-env]
    :as request}]
  (let [form-params (-> request form-parser :form-params)
        ac (authorization-confirmation form-params (fpx-settings app-env))]
    (r/response (indirect-view ac))))

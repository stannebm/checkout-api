(ns stanne.fpx-controller
  (:require
   [io.pedestal.http.body-params :refer [form-parser]]
   [io.pedestal.log :as log]
   [ring.util.response :as r]
   [stanne.fpx.core :as fpx]
   [stanne.fpx.ac :refer [authorization-confirmation]]
   [stanne.views.error-msg :refer [error-msg-view]]
   [stanne.views.fpx-home :refer [home-view]]
   [stanne.views.fpx-indirect :refer [indirect-view]]))

(defn- fpx-settings [env]
  {:config (fpx/config env)
   :bank-mapping (fpx/bank-mapping env)})

(defn fpx-home
  "Render a form that POST to FPX's AR endpoint"
  [{:keys [app-env params]}]
  (let [parse-float #(and (seq %) (Float/parseFloat %))
        txn-amount (some-> params :amount parse-float)
        to-price #(format "%.2f" %)
        render-err #(r/response (error-msg-view %))]
    (cond
      (not (float? txn-amount)) (render-err "Missing transaction amount")
      (< txn-amount 1) (render-err "Invalid transaction amount (Less than RM1)")
      (> txn-amount 30000) (render-err "Invalid transaction amount (More than RM30,000)")
      :else (r/response (home-view (to-price txn-amount)
                                   (fpx-settings app-env))))))

(defn fpx-callback-direct
  "FPX direct AC callback (text)"
  [{:keys [app-env]
    :as request}]
  (let [form-params #_(stubs/ac-stub) (-> request form-parser :form-params)
        ac (authorization-confirmation form-params (fpx-settings app-env))
        msg (cond
              (contains? #{:ok :pending-authorization} (:status ac)) "OK"
              :else "FAILED")]
    (log/info :event :callback-direct
              :form-params form-params
              :message (str "DIRECT MESSAGE: " msg))
    (r/response msg)))

(defn fpx-callback-indirect
  "FPX indirect AC callback (HTML)"
  [{:keys [app-env]
    :as request}]
  (let [form-params #_(stubs/ac-stub) (-> request form-parser :form-params)
        ac (authorization-confirmation form-params (fpx-settings app-env))]
    (r/response (indirect-view ac))))

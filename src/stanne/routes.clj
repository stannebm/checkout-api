(ns stanne.routes
  (:require
   [integrant.core :as ig]
   [io.pedestal.http :as http]
   [io.pedestal.interceptor :refer [interceptor]]
   [io.pedestal.http.body-params :refer [form-parser]] ;; decode form/json
   [ring.util.response :as r]
   [stanne.fpx.common :as fpx]
   [stanne.views.home :refer [home-view]]))

(defn test-controller
  [{:keys [query-params fpx-data]}]
  (r/response (case (fpx-data :env)
                :dev (str "query-params" query-params
                          "\n\n"
                          "fpx-data" fpx-data)
                "forbidden")))

(defn confirm-transfer
  "Post to FPX's AR endpoint"
  [{:keys [fpx-data]}]
  (r/response (home-view fpx-data)))

(defn fpx-callback-indirect
  "FPX indirect AC callback (HTML)"
  [{:keys [fpx-data]
    :as request}]
  (let [form-params (-> request form-parser :form-params)]
    (prn "fpx-data:" fpx-data)
    (prn "form-params:" form-params)
    (prn "request:" (keys request))
    (r/response form-params)))

(defmethod ig/init-key ::main
  [_ _]
  (prn "init routes..")
  #{["/" :get [http/html-body `confirm-transfer]]
    ["/indirect" :post [http/json-body `fpx-callback-indirect]]
    ["/test" :get `test-controller]})

(defn fpx-data-interceptor [env]
  (interceptor
   {:name ::fpx-data
    :enter (fn [ctx]
             (update ctx :request assoc :fpx-data {:env env
                                                   :config (fpx/config env)
                                                   :bank-mapping (fpx/bank-mapping env)}))}))

(defmethod ig/init-key ::interceptors
  [_ env]
  [(fpx-data-interceptor (:env env))])

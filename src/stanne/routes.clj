(ns stanne.routes
  (:require
   [clojure.core.match :as core.match]
   [integrant.core :as ig]
   [io.pedestal.http :as http]
   [io.pedestal.http.body-params :refer [form-parser]]
   [io.pedestal.interceptor :refer [interceptor]]
   [ring.util.response :as r]
   [stanne.fpx.ac :refer [authorization-confirmation]]
   [stanne.fpx.core :as fpx]
   [stanne.views.home :refer [home-view]]
   [stanne.views.indirect :refer [indirect-view]]
   [stubs :refer [ac-callback]]
   [io.pedestal.log :as log]))

(defn confirm-transfer
  "Post to FPX's AR endpoint"
  [{:keys [fpx-data]}]
  (r/response (home-view fpx-data)))

(defn fpx-callback-direct
  "FPX direct AC callback (text)"
  [{:keys [fpx-data]
    :as request}]
  (let [form-params (-> request form-parser :form-params)]
    (prn "fpx-data:" fpx-data)
    (prn "form-params:" form-params)
    (prn "request:" (keys request))
    (r/response form-params)))

(defn fpx-callback-indirect
  "FPX indirect AC callback (HTML)"
  [{:keys [fpx-data]}]
  (let [form-params (ac-callback)
        ac (authorization-confirmation form-params fpx-data)]
    (r/response (indirect-view ac))))

;;; Routes ;;;

(defmethod ig/init-key ::main
  [_ _]
  (log/debug :event "init routes")
  #{["/" :get [http/html-body `confirm-transfer]]
    ["/direct" :post [http/json-body `fpx-callback-direct]]
    ["/indirect" :get [http/html-body `fpx-callback-indirect]]})

;;; Interceptors ;;;

(defn service-error-handler []
  (interceptor
   {:error
    (fn [ctx ex]
      ;; ex-data keys:
      ;;  :execution-id
      ;;  :stage
      ;;  :interceptor
      ;;  :exception-type
      ;;  :exception
      (let [ex-data' (ex-data ex)
            cause (-> ex Throwable->map :cause)
            whitelist [:api :response-params :signature]]
        (core.match/match [ex-data']
          ;; in the application space, we always throw ex-info
          [{:exception-type :clojure.lang.ExceptionInfo}]
          (do
            (log/error :error (-> {:cause cause}
                                  (merge (select-keys ex-data' whitelist))
                                  str))
            (assoc ctx :response {:status 500
                                  :body cause}))
          ;; unexpected exception, rethrow
          :else
          (assoc ctx :io.pedestal.interceptor.chain/error ex))))}))

(defn fpx-data-interceptor [env]
  (interceptor
   {:name ::fpx-data
    :enter (fn [ctx]
             (update ctx :request
                     assoc :fpx-data {:env env
                                      :config (fpx/config env)
                                      :bank-mapping (fpx/bank-mapping env)}))}))

(defmethod ig/init-key ::interceptors
  [_ env]
  [(service-error-handler)
   (fpx-data-interceptor (:env env))])

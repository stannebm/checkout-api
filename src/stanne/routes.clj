(ns stanne.routes
  (:require
   [clojure.core.match :as core.match]
   [integrant.core :as ig]
   [io.pedestal.http :as http]
   [io.pedestal.interceptor :refer [interceptor]]
   [io.pedestal.log :as log]
   [stanne.fpx-controller :refer [fpx-callback-direct fpx-callback-indirect fpx-home]]
   [stanne.cybersource-controller :refer [cybersource-home]]
   [stanne.fpx.core :as fpx]))

(defn health
  "Does nothing except 200"
  [_]
  {:status 200
   :body "OK"})

(defmethod ig/init-key ::main
  [_ _]
  (log/info :event "init routes")
  #{["/" :get [http/html-body `health]]
    ;;; FPX
    ["/fpx" :get [http/html-body `fpx-home]]
    ["/direct" :any `fpx-callback-direct]
    ["/indirect" :any [http/html-body `fpx-callback-indirect]]

    ;;; Cybersource
    ["/cybersource" :get [http/html-body `cybersource-home]]})

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
  (let [fpx-data {:env env
                  :config (fpx/config env)
                  :bank-mapping (fpx/bank-mapping env)}]
    (interceptor
     {:name ::fpx-data
      :enter (fn [ctx]
               (update ctx :request
                       assoc :fpx-data fpx-data))})))

(defmethod ig/init-key ::interceptors
  [_ env]
  [(service-error-handler)
   (fpx-data-interceptor (:env env))])

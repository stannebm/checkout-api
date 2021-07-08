(ns stanne.routes
  (:require
   [clojure.core.match :as core.match]
   [ring.util.response :as r]
   [integrant.core :as ig]
   [io.pedestal.http :as http]
   [io.pedestal.interceptor :refer [interceptor]]
   [io.pedestal.log :as log]
   [stanne.cybersource-controller :refer [cybersource-done-notify cybersource-home cybersource-receipt]]
   [stanne.fpx-controller :refer [fpx-callback-direct fpx-callback-indirect fpx-home]]))

(defn health
  "Does nothing except 200"
  [_]
  {:status 200
   :body "OK"
   :headers {"Content-Type" "text/plain"}})

(defn redirect-to-website
  "Render receipt page"
  [_]
  (r/redirect "https://www.minorbasilicastannebm.com"))

(defmethod ig/init-key ::main
  [_ _]
  (log/info :event "init routes")
  #{["/" :get [http/html-body `health]]
;;; FPX
    ["/fpx" :get [http/html-body `fpx-home]]
    ["/direct" :any `fpx-callback-direct]
    ["/indirect" :any [http/html-body `fpx-callback-indirect]]

;;; Cybersource
    ["/cybersource" :get [http/html-body `cybersource-home]]
    ["/cybersource-done-notify" :post `cybersource-done-notify]
    ["/cybersource-done-receipt" :get `redirect-to-website]
    ["/cybersource-receipt" :post [http/html-body `cybersource-receipt]]})

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

(defn fpx-app-env-interceptor [env]
  (interceptor
   {:name ::app-env
    :enter (fn [ctx]
             (update ctx :request
                     assoc :app-env env))}))

(defmethod ig/init-key ::interceptors
  [_ env]
  [(service-error-handler)
   (fpx-app-env-interceptor (:env env))])

(ns stanne.routes
  (:require
   [integrant.core :as ig]
   [io.pedestal.http :as http]
   [io.pedestal.interceptor :refer [interceptor]]
   [io.pedestal.log :as log]
   [ring.util.response :as r]
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

(defn fpx-app-env-interceptor [env]
  (interceptor
   {:name ::app-env
    :enter (fn [ctx]
             (update ctx :request
                     assoc :app-env env))}))

(defmethod ig/init-key ::interceptors
  [_ env]
  [(fpx-app-env-interceptor (:env env))])

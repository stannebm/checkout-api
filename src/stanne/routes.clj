(ns stanne.routes
  (:require
   [stanne.views.layout :refer [main-layout]]
   [integrant.core :as ig]
   [io.pedestal.http :as http]
   [io.pedestal.http.route :as route]
   [ring.util.response :as r]
   [io.pedestal.interceptor :refer [interceptor]]))

(defn about
  [_]
  (r/response (format "Clojure %s - served from %s"
                      (clojure-version)
                      (route/url-for ::about))))
(defn home
  [request]
  (prn (:fpx-config request))
  (r/response (main-layout (str (:fpx-config request)))))

(defmethod ig/init-key ::main
  [_ _]
  (prn "init routes..")
  #{["/" :get [http/html-body `home]]
    ["/about" :get [http/html-body `about]]})

(defn fpx-config-interceptor [fpx-config]
  (interceptor
   {:name ::fpx-config
    :enter (fn [ctx]
             (update ctx :request assoc :fpx-config fpx-config))}))

(defmethod ig/init-key ::interceptors
  [_ fpx-config]
  [(fpx-config-interceptor fpx-config)])

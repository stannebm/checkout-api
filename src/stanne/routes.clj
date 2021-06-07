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
  (r/response (main-layout (str
                            (:app-env request)))))

(defmethod ig/init-key ::main
  [_ _]
  (prn "init routes..")
  #{["/" :get [http/html-body `home]]
    ["/about" :get [http/html-body `about]]})

(defn app-env [env]
  (interceptor
   {:name ::app-env
    :enter (fn [ctx]
             (update ctx :request assoc :app-env env))}))

(defmethod ig/init-key ::interceptors
  [_ env]
  [(app-env (:env env))])

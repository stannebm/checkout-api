(ns stanne.routes
  (:require
   [stanne.views.layout :refer [main-layout]]
   [integrant.core :as ig]
   [io.pedestal.http :refer [html-body]]
   [io.pedestal.http.route :as route]
   [ring.util.response :as r]))

(defn about
  [_]
  (r/response (format "Clojure %s - served from %s"
                      (clojure-version)
                      (route/url-for ::about))))
(defn home
  [_]
  (r/response (main-layout "content goes here")))

(defn routes []
  #{["/" :get [html-body `home]]
    ["/about" :get [html-body `about]]})

(defmethod ig/init-key ::routes
  [_ _]
  (prn "init routes..")
  (routes))

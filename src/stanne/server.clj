(ns fpx-app.server
  (:gen-class) ; for -main method in uberjar
  (:require [io.pedestal.http :as pedestal]
            [io.pedestal.http.route :as route]))

(defn- production-config [routes]
  {:env :prod
   ::pedestal/routes #(route/expand-routes routes)
   ::pedestal/resource-path "/public"
   ::pedestal/type :jetty
   ::pedestal/host "localhost"
   ::pedestal/port 8080
   ::pedestal/container-options {;; Options to pass to the container (Jetty)
                                 :h2c? true
                                 :h2? false
                                 :ssl? false}})

(defn- development-config-extension []
  {:env :dev
   ::pedestal/join? false
   ::pedestal/allowed-origins
   {:creds true
    :allowed-origins (constantly true)}
   ::pedestal/secure-headers
   {:content-security-policy-settings {:object-src "'none'"}}})

(defn pedestal-config [routes env]
  (let [prod-config (production-config routes)
        dev-config (development-config-extension)]
    (case env
      :prod prod-config
      :dev (merge prod-config dev-config))))

(ns stanne.server
  (:gen-class) ; for -main method in uberjar
  (:require
   [integrant.core :as ig]
   [io.pedestal.http :as pedestal]
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

(defn- development-config [routes]
  (merge (production-config routes)
         {:env :dev
          ::pedestal/join? false
          ::pedestal/allowed-origins
          {:creds true
           :allowed-origins (constantly true)}
          ::pedestal/secure-headers
          {:content-security-policy-settings {:object-src "'none'"}}}))

(defmethod ig/init-key ::config
  [_ {:keys [routes env]}]
  (case env
    :prod (production-config routes)
    :dev (development-config routes)))

(defmethod ig/init-key ::server
  [_ {:keys [config]}]
  (-> config
      pedestal/create-server
      pedestal/start))

(defmethod ig/halt-key! ::server
  [_ server]
  (pedestal/stop server))

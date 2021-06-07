(ns stanne.server
  (:gen-class) ; for -main method in uberjar
  (:require
   [integrant.core :as ig]
   [io.pedestal.http :as pedestal]
   [io.pedestal.http.route :as route]))

(defn- production-config [{:keys [routes env]}]
  {:env env
   ::pedestal/routes #(route/expand-routes routes)
   ::pedestal/host "localhost"
   ::pedestal/port 8080
   ::pedestal/resource-path "/public"
   ::pedestal/type :jetty
   ::pedestal/container-options {;; Options to pass to the container (Jetty)
                                 :h2c? true
                                 :h2? false
                                 :ssl? false}})

(defn- development-config [{:keys [env]
                            :as service-config}]
  (merge (production-config service-config)
         {:env env
          ::pedestal/join? false
          ::pedestal/allowed-origins {:creds true
                                      :allowed-origins (constantly true)}
          ::pedestal/secure-headers {:content-security-policy-settings
                                     {:object-src "'none'"}}}))

(defmethod ig/init-key ::config
  [_ {:keys [env interceptors]
      :as service-config}]
  (-> (case env
        :prod (production-config service-config)
        :dev (development-config service-config))
      (pedestal/default-interceptors)
      (update ::pedestal/interceptors into interceptors)))

(defmethod ig/init-key ::server
  [_ {:keys [config]}]
  (-> config
      pedestal/create-server
      pedestal/start))

(defmethod ig/halt-key! ::server
  [_ server]
  (pedestal/stop server))

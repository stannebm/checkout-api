(ns stanne.server
  (:gen-class) ; for -main method in uberjar
  (:require
   [integrant.core :as ig]
   [io.pedestal.http :as pedestal]
   [io.pedestal.http.route :as route]))

(defn- pedestal-config [{:keys [routes env]}]
  {:env env
   ::pedestal/routes #(route/expand-routes routes)
   ::pedestal/host "localhost"
   ::pedestal/port 8080
   ::pedestal/resource-path "/public"
   ::pedestal/type :jetty
   ::pedestal/join? false
   ::pedestal/allowed-origins {:creds true
                               :allowed-origins (constantly true)}
   ::pedestal/secure-headers {:content-security-policy-settings
                              {:object-src "'none'"}}
   ::pedestal/container-options {;; Options to pass to the container (Jetty)
                                 :h2c? true
                                 :h2? false
                                 :ssl? false}})

(defmethod ig/init-key ::config
  [_ {:keys [interceptors]
      :as service-config}]
  (-> (pedestal-config service-config)
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

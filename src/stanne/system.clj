(ns fpx-app.system
  (:gen-class)
  (:require [integrant.core :as ig]
            [io.pedestal.http :as pedestal]
            [fpx-app.server :as server]
            [fpx-app.routes :refer [routes]]))

(defn config [env]
  {:pedestal/routes {}
   :pedestal/config {:routes (ig/ref :pedestal/routes)
                     :env env}
   :pedestal/server {:config (ig/ref :pedestal/config)}})

(defmethod ig/init-key :pedestal/routes
  [_ _]
  (prn "init routes..")
  (routes))

(defmethod ig/init-key :pedestal/config
  [_ {:keys [routes env]}]
  (server/pedestal-config routes env))

(defmethod ig/init-key :pedestal/server
  [_ {:keys [config]}]
  (-> config
      pedestal/create-server
      pedestal/start))

(defmethod ig/halt-key! :pedestal/server
  [_ server]
  (pedestal/stop server))

(defn -main []
  (ig/init (config :prod)))

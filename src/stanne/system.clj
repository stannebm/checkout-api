(ns stanne.system
  (:gen-class)
  (:require
   [integrant.core :as ig]
   [stanne.routes :as routes]
   [stanne.server :as server]))

(defn config [env]
  {::routes/main {}
   ::routes/interceptors {:env env}
   ::server/config {:routes (ig/ref ::routes/main)
                    :interceptors (ig/ref ::routes/interceptors)
                    :env env}
   ::server/server {:config (ig/ref ::server/config)}})

(defn -main []
  ;; FIXME change to :prod after UAT
  (ig/init (config :dev)))

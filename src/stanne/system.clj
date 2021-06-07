(ns stanne.system
  (:gen-class)
  (:require
   [integrant.core :as ig]
   [stanne.fpx :as fpx]
   [stanne.routes :as routes]
   [stanne.server :as server]))

(defn config [env]
  {::fpx/config {:env env}
   ::routes/main {}
   ::routes/interceptors {:fpx-config (ig/ref ::fpx/config)}
   ::server/config {:routes (ig/ref ::routes/main)
                    :interceptors (ig/ref ::routes/interceptors)
                    :env env}
   ::server/server {:config (ig/ref ::server/config)}})

(defn -main []
  (ig/init (config :prod)))

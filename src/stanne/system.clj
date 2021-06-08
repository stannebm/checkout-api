(ns stanne.system
  (:gen-class)
  (:require
   [integrant.core :as ig]
   [org.httpkit.client]
   [org.httpkit.sni-client :as sni-client]
   [stanne.routes :as routes]
   [stanne.server :as server]))

(alter-var-root #'org.httpkit.client/*default-client* (fn [_] sni-client/default-client))

(defn config [env]
  {::routes/main {}
   ::routes/interceptors {:env env}
   ::server/config {:routes (ig/ref ::routes/main)
                    :interceptors (ig/ref ::routes/interceptors)
                    :env env}
   ::server/server {:config (ig/ref ::server/config)}})

(defn -main []
  (ig/init (config :prod)))

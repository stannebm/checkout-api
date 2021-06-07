(ns stanne.system
  (:gen-class)
  (:require [integrant.core :as ig]))

(defn config [env]
  (let [config-map
        {:stanne.fpx/endpoints {:env env}
         :stanne.routes/routes {}
         :stanne.server/config {:routes (ig/ref :stanne.routes/routes)
                                :env env}
         :stanne.server/server {:config (ig/ref :stanne.server/config)}}]
    (ig/load-namespaces config-map)
    config-map))

(defn -main []
  (ig/init (config :prod)))

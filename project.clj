(defproject fpx-app "0.0.1-SNAPSHOT"
  :description "FPX integration"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [io.pedestal/pedestal.service "0.5.9"]
                 [io.pedestal/pedestal.jetty "0.5.9"]
                 [ch.qos.logback/logback-classic "1.2.3" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.26"]
                 [org.slf4j/jcl-over-slf4j "1.7.26"]
                 [org.slf4j/log4j-over-slf4j "1.7.26"]
                 [integrant "0.8.0"]
                 [hiccup "1.0.5"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  :profiles {:dev {:source-paths ["dev/src"]
                   :dependencies [[integrant/repl "0.3.2"]
                                  [io.pedestal/pedestal.service-tools "0.5.9"]]}
             :repl {:repl-options {:init-ns dev}}
             :uberjar {:aot [fpx-app.system]}}
  :main ^{:skip-aot true} fpx-app.system)

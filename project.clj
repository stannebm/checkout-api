(defproject stanne "0.0.1-SNAPSHOT"
  :description "StAnne FPX integration"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [io.pedestal/pedestal.service "0.5.9"]
                 [io.pedestal/pedestal.jetty "0.5.9"]
                 [ch.qos.logback/logback-classic "1.2.3" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.26"]
                 [org.slf4j/jcl-over-slf4j "1.7.26"]
                 [org.slf4j/log4j-over-slf4j "1.7.26"]
                 [integrant "0.8.0"]
                 [hiccup/hiccup "2.0.0-alpha2"]
                 [clj-http "3.12.0"]
                 [metosin/jsonista "0.3.3"]

                 ;; [org.bouncycastle/bcprov-jdk15 "1.46"]
                 ;; [commons-io/commons-io "2.4"]
                 [buddy/buddy-core "1.10.1"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  ;; :java-source-paths ["interop"]
  :profiles {:dev {:source-paths ["dev/src"]
                   :dependencies [[integrant/repl "0.3.2"]
                                  [io.pedestal/pedestal.service-tools "0.5.9"]]}
             :repl {:repl-options {:init-ns dev}
                    :plugins [[cider/cider-nrepl "0.26.0"]]}
             :uberjar {:aot [stanne.system]}}
  :main ^{:skip-aot true} stanne.system)

(ns dev
  (:require [stanne.system :as system]
            [clojure.tools.namespace.repl]
            [integrant.repl :refer [go reset]]))

(clojure.tools.namespace.repl/set-refresh-dirs "dev" "src")

(integrant.repl/set-prep! (constantly (system/config :dev)))

(comment
  (go)
  (reset))

;; (pki.Signature/signData "/etc/fpx/EX00011982.key" "hi" "SHA1withRSA")

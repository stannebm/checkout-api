(ns dev
  (:require [stanne.system :as system]
            [integrant.repl :refer [go reset]]))

(integrant.repl/set-prep! (constantly (system/config :dev)))

(comment
  (go)
  (reset))

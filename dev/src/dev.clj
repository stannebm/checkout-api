(ns dev
  (:require [stanne.system :as system]
            [integrant.repl :refer [go halt reset reset-all]]))

(integrant.repl/set-prep! (constantly (system/config :dev)))

(comment
  (go)
  (halt)
  (reset)
  (reset-all))

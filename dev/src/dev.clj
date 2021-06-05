(ns dev
  (:require [fpx-app.system :as system]
            [integrant.repl :refer [go halt reset reset-all]]))

(integrant.repl/set-prep! (constantly (system/config :dev)))

(comment
  (go)
  (halt)
  (reset)
  (reset-all))

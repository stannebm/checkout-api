(ns stanne.cybersource-controller
  (:require
   [ring.util.response :as r]
   [stanne.views.cybersource-home :refer [home-view]]))

(defn cybersource-home
  "Render a form that POST to CyberSource hosted checkout"
  [{:keys [params]}]
  (prn "Params: " params)
  (r/response (home-view)))

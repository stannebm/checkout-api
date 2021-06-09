(ns stanne.views.home
  (:require
   [stanne.views.layout :refer [layout]]
   [stanne.fpx.ar :as ar]))

(defn home-view [config]
  (let [ar (ar/authorization-request config)
        url (:url ar)
        form-params (:form-params ar)]
    (layout
     [:div
      [:h2 "in home view"]
      [:div (str config)]

      [:form {:method "post"
              :action url}
       (for [[field value] form-params]
         [:input {:type "hidden"
                  :value value
                  :name field}])
       [:input {:type "submit"
                :class "bg-transparent hover:bg-blue-500 text-blue-700 font-semibold hover:text-white py-2 px-4 border border-blue-500 hover:border-transparent rounded"
                :value "Submit"}]]])))

(ns stanne.fpx
  (:require
   [integrant.core :as ig]
   [org.httpkit.client :as http]))

(defmethod ig/init-key ::config
  [_ {:keys [env]}]
  (case env
    :prod
    {:endpoints {:txn-request "https://www.mepsfpx.com.my/FPXMain/seller2DReceiver.jsp"
                 :txn-cancel "https://www.mepsfpx.com.my/FPXMain/FPXMain/sellerReqCancel.jsp"
                 :bank-list "https://www.mepsfpx.com.my/FPXMain/RetrieveBankList"}}
    :dev
    {:endpoints {:txn-request "https://uat.mepsfpx.com.my/FPXMain/seller2DReceiver.jsp"
                 :txn-cancel "https://uat.mepsfpx.com.my/FPXMain/FPXMain/sellerReqCancel.jsp"
                 :bank-list "https://uat.mepsfpx.com.my/FPXMain/RetrieveBankList"}}))

(comment
  (let [{:keys [status body error]} @(http/get "http://localhost:3000/path")]
    (if error
      (println "Failed, exception: " error)
      (println "HTTP GET success: " status body)))

  (let [options {:form-params {:name "http-kit"
                               :features ["async" "client" "server"]}}
        {:keys [status error]} @(http/post "http://localhost:3000" options)]
    (if error
      (println "Failed, exception is " error)
      (println "Async HTTP POST: " status))))

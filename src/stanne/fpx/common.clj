(ns stanne.fpx.common)

(defn config [env]
  (merge {:exchange-id "EX00011982"
          :seller-id "SE00013501"
          :msg-token "01" ;; B2C
          :fpx-version "7.0"}
         (case env
           :prod
           {:pki {:merchant-key "/etc/fpx/EX00011982.key"
                  :fpx-cert "/etc/fpx/fpxprod_Merchant.cer"}
            :endpoints {:auth-request "https://www.mepsfpx.com.my/FPXMain/seller2DReceiver.jsp"
                        :auth-cancel "https://www.mepsfpx.com.my/FPXMain/FPXMain/sellerReqCancel.jsp"
                        :bank-list "https://www.mepsfpx.com.my/FPXMain/RetrieveBankList"}}
           :dev
           {:pki {:merchant-key "/etc/fpx/EX00011982.key"
                  :fpx-cert "/etc/fpx/fpxuat.cer"}
            :endpoints {:auth-request "https://uat.mepsfpx.com.my/FPXMain/seller2DReceiver.jsp"
                        :auth-cancel "https://uat.mepsfpx.com.my/FPXMain/FPXMain/sellerReqCancel.jsp"
                        :bank-list "https://uat.mepsfpx.com.my/FPXMain/RetrieveBankList"}})))

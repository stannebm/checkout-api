(ns stanne.fpx.core
  (:require [clojure.walk :as walk]))

(defn config [env]
  (merge {:exchange-id "EX00011982"
          :seller-id "SE00013501"
          :msg-token "01" ;; B2C
          :fpx-version "7.0"}
         (case env
           :prod {:env :prod
                  :pki {:merchant-key "/etc/fpx/EX00011982.key"
                        :fpx-cert "/etc/fpx/fpxprod_Merchant.cer"}
                  :endpoints {:tnc "https://uat.mepsfpx.com.my/FPXMain/termsAndConditions.jsp"
                              :auth-request "https://www.mepsfpx.com.my/FPXMain/seller2DReceiver.jsp"
                              :auth-cancel "https://www.mepsfpx.com.my/FPXMain/FPXMain/sellerReqCancel.jsp"
                              :bank-list "https://www.mepsfpx.com.my/FPXMain/RetrieveBankList"}}
           :dev {:env :dev
                 :pki {:merchant-key "/etc/fpx/EX00011982.key"
                       :fpx-cert "/etc/fpx/fpxuat.cer"}
                 :endpoints {:tnc "https://www.mepsfpx.com.my/FPXMain/termsAndConditions.jsp"
                             :auth-request "https://uat.mepsfpx.com.my/FPXMain/seller2DReceiver.jsp"
                             :auth-cancel "https://uat.mepsfpx.com.my/FPXMain/FPXMain/sellerReqCancel.jsp"
                             :bank-list "https://uat.mepsfpx.com.my/FPXMain/RetrieveBankList"}})))

(defn bank-mapping [env]
  (walk/stringify-keys
   (case env
     :prod {:ABB0233 "Affin Bank"
            :ABMB0212 "Alliance Bank (Personal)"
            :AGRO01 "AGRONet"
            :AMBB0209 "AmBank"
            :BIMB0340 "Bank Islam"
            :BMMB0341 "Bank Muamalat"
            :BKRM0602 "Bank Rakyat"
            :BSN0601 "BSN"
            :BCBB0235 "CIMB Clicks"
            :HLB0224 "Hong Leong Bank"
            :HSBC0223 "HSBC Bank"
            :KFH0346 "KFH"
            :MBB0228 "Maybank2E"
            :MB2U0227 "Maybank2U"
            :OCBC0229 "OCBC Bank"
            :PBB0233 "Public Bank"
            :RHB0218 "RHB Bank"
            :SCB0216 "Standard Chartered"
            :UOB0226 "UOB Bank"}
     :dev {:ABB0234 "Affin B2C - Test ID"
           :ABB0233 "Affin Bank"
           :ABMB0212 "Alliance Bank (Personal)"
           :AGRO01 "AGRONet"
           :AMBB0209 "AmBank"
           :BIMB0340 "Bank Islam"
           :BMMB0341 "Bank Muamalat"
           :BKRM0602 "Bank Rakyat"
           :BSN0601 "BSN"
           :BCBB0235 "CIMB Clicks"
           :CIT0219 "Citibank"
           :HLB0224 "Hong Leong Bank"
           :HSBC0223 "HSBC Bank"
           :KFH0346 "KFH"
           :MBB0228 "Maybank2E"
           :MB2U0227 "Maybank2U"
           :OCBC0229 "OCBC Bank"
           :PBB0233 "Public Bank"
           :RHB0218 "RHB Bank"
           :TEST0021 "SBI Bank A"
           :TEST0022 "SBI Bank B"
           :TEST0023 "SBI Bank C"
           :SCB0216 "Standard Chartered"
           :UOB0226 "UOB Bank"
           :UOB0229 "UOB Bank - Test ID"})))

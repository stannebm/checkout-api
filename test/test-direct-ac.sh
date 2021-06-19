#!/bin/sh

curl 'https://fpxuat.minorbasilicastannebm.com/direct' \
    -H 'authority: fpxuat.minorbasilicastannebm.com' \
    -H 'pragma: no-cache' \
    -H 'cache-control: no-cache' \
    -H 'upgrade-insecure-requests: 1' \
    -H 'origin: https://uat.mepsfpx.com.my' \
    -H 'content-type: application/x-www-form-urlencoded' \
    -H 'accept-language: en-GB,en-US;q=0.9,en;q=0.8' \
    --data-raw 'fpx_debitAuthCode=00&fpx_debitAuthNo=15733223&fpx_sellerExId=EX00011982&fpx_creditAuthNo=9999999999&fpx_buyerName=N%40ME%28%29%2F+.-%26BUYER&fpx_buyerId=&fpx_sellerTxnTime=20210619094956&fpx_sellerExOrderNo=20210619094956&fpx_makerName=New+Simulator&fpx_buyerBankBranch=SBI+BANK+A&fpx_buyerBankId=TEST0021&fpx_msgToken=01&fpx_creditAuthCode=00&fpx_sellerId=SE00013501&fpx_fpxTxnTime=20210619155026&fpx_buyerIban=&fpx_sellerOrderNo=20210619094956&fpx_txnAmount=1.00&fpx_fpxTxnId=2106191550270891&fpx_checkSum=4679FE7983F59D710D598166B0D120591E0010DB08A2DCFB3AE32EEEB8E028AC98108D01B848E1050A0FA9DE470069D03C7A9052791AC5511FEB356F794FA09929956E05A1AE2A0A7A70ACAD8486FD2151DC0F84CE06BD4E144D7367235FF9C3F57CA6F73561FA067509247C8F5592E9017633CAE97C8E986E334DDF65DDDDAA5D9A3D3FED71884954FAB924D5CB0C1C1F26ED6EB3CEC13E0E9A7EF35D1CAEC6341459BCBAD220A209A398CC88A4D10CB167768A04A42A0E0ACA49F4E58FABBB0585E16EF89050CF5F1FD3D15720994A63091BD6DBA33DAF2E92912772AE9EA07E63FCD840F914A6A86E4D490C4BF66628570202AF4C253BF012D29440EF5E0F&fpx_msgType=AC&fpx_txnCurrency=MYR'

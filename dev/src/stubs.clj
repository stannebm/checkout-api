(ns stubs
  (:require [clojure.walk :as walk]))

(defn ac-stub []
  (walk/keywordize-keys
   {"fpx_sellerExId" "EX00011982",
    "fpx_txnCurrency" "MYR",
    "fpx_creditAuthCode" "00",
    "fpx_buyerId" "",
    "fpx_makerName" "New Simulator",
    "fpx_sellerExOrderNo" "20210615190651",
    "fpx_fpxTxnTime" "20210615190658",
    "fpx_sellerId" "SE00013501",
    "fpx_fpxTxnId" "2106151906580535",
    "fpx_buyerBankId" "TEST0021",
    "fpx_checkSum" "3973B47AE97FAFC2B7820B72DC2BE471FB9801C75DE117FA7F16D80F54C290226D3134A12395375EED12E8A02044F716BD30206A3E6C0B608D5CD0C2B463088824DAAC4F8BA4AC7A0BFCC132C73528E3983B1C3495C6DCF3A93DE3EA65A5BD0AC62D18F0C61EB72E7DA3E1388E325C4713E1E9E2A8301BD52CFE1A80A3895092204FE8ED211236A7BBEA786C5F53AE3C2869034ABF022A7E05F3333F59BACA56B3EB6748CC376CE4020C59D116BF55D1D216F352FC0F18538DB2DEC178A43E14806543B23C34CE8EB51B6527B66C3D21B3CB5EF802CE71D7A47CBB928DF4EAA47DC78453DF197ABECD48E8AFEB22413556510270164B46AC19CEC54BC33433F8",
    "fpx_sellerTxnTime" "20210615190651",
    "fpx_creditAuthNo" "9999999999",
    "fpx_debitAuthCode" "00",
    "fpx_msgToken" "01",
    "fpx_debitAuthNo" "15733223",
    "fpx_buyerIban" "",
    "fpx_txnAmount" "1.00",
    "fpx_sellerOrderNo" "20210615190651",
    "fpx_msgType" "AC",
    "fpx_buyerName" "N@ME()/ .-&BUYER",
    "fpx_buyerBankBranch" "SBI BANK A"}))

(defn ar-req []
  {:url "https://uat.mepsfpx.com.my/FPXMain/seller2DReceiver.jsp",
   :banks '(["ABB0234" {:code "ABB0234",
                        :name "Affin B2C - Test ID",
                        :status :available}]
            ["ABB0233" {:code "ABB0233",
                        :name "Affin Bank",
                        :status :available}]
            ["AGRO01" {:code "AGRO01",
                       :name "AGRONet",
                       :status :available}]
            ["ABMB0212" {:code "ABMB0212",
                         :name "Alliance Bank (Personal)",
                         :status :available}]
            ["AMBB0209" {:code "AMBB0209",
                         :name "AmBank",
                         :status :available}]
            ["BIMB0340" {:code "BIMB0340",
                         :name "Bank Islam",
                         :status :available}]
            ["BMMB0341" {:code "BMMB0341",
                         :name "Bank Muamalat",
                         :status :available}]
            ["BKRM0602" {:code "BKRM0602",
                         :name "Bank Rakyat",
                         :status :available}]
            ["BSN0601" {:code "BSN0601",
                        :name "BSN",
                        :status :available}]
            ["BCBB0235" {:code "BCBB0235",
                         :name "CIMB Clicks",
                         :status :available}]
            ["HLB0224" {:code "HLB0224",
                        :name "Hong Leong Bank",
                        :status :available}]
            ["HSBC0223" {:code "HSBC0223",
                         :name "HSBC Bank",
                         :status :available}]
            ["KFH0346" {:code "KFH0346",
                        :name "KFH",
                        :status :available}]
            ["MBB0228" {:code "MBB0228",
                        :name "Maybank2E",
                        :status :available}]
            ["MB2U0227" {:code "MB2U0227",
                         :name "Maybank2U",
                         :status :available}]
            ["OCBC0229" {:code "OCBC0229",
                         :name "OCBC Bank",
                         :status :available}]
            ["PBB0233" {:code "PBB0233",
                        :name "Public Bank",
                        :status :available}]
            ["RHB0218" {:code "RHB0218",
                        :name "RHB Bank",
                        :status :available}]
            ["TEST0021" {:code "TEST0021",
                         :name "SBI Bank A",
                         :status :available}]
            ["TEST0022" {:code "TEST0022",
                         :name "SBI Bank B",
                         :status :available}]
            ["TEST0023" {:code "TEST0023",
                         :name "SBI Bank C",
                         :status :available}]
            ["UOB0226" {:code "UOB0226",
                        :name "UOB Bank",
                        :status :available}]),
   :checksums
   {"BSN0601" "8705C9238CEEB45468A12C6BC68FD60C93B3382056F73463AAA750C5F86153741F650517C913DD812567A2E06761CDE3ED71CEB792B0D9A7E65469139F49E0BA806606440650D7689C049CD69EDC45284195ECE247D352343C2864C665F2CC47EB583C2B2D9A6D39D12DE3CACC855A0A9B54486C294F1EC8D35D522439A7DB0CB103203ADD7090ADED8F95FA98A07E8A83D3AD6C92766EDF9A8BBBB9AC7C24F793644654AA76B851FECE9A94D9448AAD8FEE11F50B731366EE1766702717C03840AE2A325D41F24E423FA2EC2DC09C80A05C66E253C6D58D6680C2A8F9E19D686604AABF371167C332FC476D7264E8B5B3BA708873638F46EA7D4D72BFE66F17",
    "RHB0218" "3BEE49B3385EE2B49144A527C18FFD0F426F02588B463001852908C501CF411A73BF0B3FD41EDB431D11CC781728D9E60253999A484B5BBC32E800BB6C1CA7562C53696D6927482EB3E75FEDE8A73BC882EF580A19B58CDEA3D7326A507FAD50C073D98B1535E827F89C97B9E11069AD9465A2E07592FF36CA27977159549757733C6380B53633E5516DDB8437B4B748AE77492CBEEDE434D84D2E51F594D46D479099C75269D41267696257E49D374B47967E71FC23BACB5F53B0B9006889ACA686F467C5C3867C54E37885D2FF29DE4B67DD900D444300A42810FE629E175E13D08C1944D1A7AB8D47356B2F1CBC60F686447C90A589FD500712B06FE139DB",
    "OCBC0229" "A467D53014242E27E644C93D7CEBBBA9E5C7C4DBA2B09F98325EE75038C79F2AE4A81B5898D4441C96C3AD3B5828AF6F14DA3D6F28C3A691220563A420CFF26A722AC8BB8FB70A7EE9BF210C61839B6DAC1060EFE2DD49A1CB8301BE6E55FA08B33908D0A1BEFB2B6A595A99FEB72580E111D9AD92F47B34482F97292F2AE78737299BDA04562527526B0D0EF0B4642221E9C0B30726F5DB0C1CF780CC0AB5D497E7888456E4F3FE550613D6D10F1BB47E774246A0D2FD58EF7180275384B5543685A116D533F69FE04DE87D6A0B4D7FDE16684D68F8DF6C11829235D4F29D7ACD9EAFB998C11E34F2DDEB1F79CD3C061F7E6B79B6EC55184EA205A1BA8EF849",
    "HSBC0223" "90C54461EAF7CFBB0014D362E7DC7C22F43D92882AE6ED2EEE2183DEE6FE74E5BAD50F0459CC4AC397E90FB5F5DB1965929CC96D44088B6C4FA32D7ABC2DB0D0334047B33793BBF856ABCB2C3F088D741763500F6B8A5E92DA2DB548F8F58597B1CD30162F073F967D4C6840294B1585277F1E1A59D66F291E0B2F367B11521367664380C1740EA38C7CF85B260F7AC9520637CF0FBFA18B34E9E90BC2F6FD123DF3C9850EC244ECBFF6B3E7833357725049BCE0D5AF2A35760D1F56D725A45B8CBD8BD0D77BDEBC5E10C7E3E273D3F01DC80780B6F51F00DF9893D95178B26CDAD8AF27E9CF4FAD655CB4A244CF50879ECACE30699584C35DDC0EDE1CA51A01",
    "HLB0224" "3643CE90883BEDEED94402480CA4B96A8D1BD170E7912AF23BCB49AE9BDBEBEC4183901475F953EDB516EF2B4F3026EA250EA2DE81CF18281F772B05A2857FC28AF33BB4710D9F0B52B665195B382DF1E412CE6909130BF96B88B5E1D91D79DE6B3DE6A06F17CE37B686A719390792D62E2A4FFE0B5BFF3241C643CF70E85C5471F58A8BFD12BC6D694EFA3F22F6B03A3E2094A26232E7CD0F89CB7FBEE154593C85286D4C8E52139F26BF4B2A5A1D5810D3F873FC970FBBB090D72E265A5DF5BBDB18E06F8B0AFEBBD1A2AE53CD7A0C3FB15BA84600E5285A52915C2117CC69F7AC78EABAEDAC150D5BD41B3B06206E747BA5A0093B69AAAD94A9E90992FD26",
    "UOB0226" "0032138920509428D24195B4C5B8E616A81816565BB57FA6616212337A11CD890E6A8960FE3429E6FA33DADC1A9A27D6611B513C90D043CEC6AF764C62DDFCB2636D9DB0F96FDC9114C929AE74958CB2176D12EBA939608B1B5735F4167E7BAFBF5844F1583C16AF87BAE27C726678C211D1ED685D8787E5CE3BADAB826A1DD03E4B93A2EDEE2E272FC1031CFFAF4EFF0CCC030410DAC538BDAD055CF28D6D6760F9D548804E4868E1EE7E9EA3D30E7A096A1A210198EACCCCA088F0157328C9C7E220B1297742FF6EF999C448431914B85B590236E946717F767989769E80EF9F9CAFAB3DFE73E42BDF186D23ABFC8FDCBD25A39CC647312701F737858A27B6",
    "BMMB0341" "8D9716D254505346931541AD647C92548CA2D9495B0AE8077B887DABBEA82EC90E41DC170E43FF76640216FA0C6FB90253D733BF5C59364B60210C292EE0818AD2CE9CA620B77F3CCD59F708CBEEE98C2EB3F81F4588C4EDCFF736AEDF0EE3A4436E2825951E91981CC5F4D3848D6EF6069C33E3B4AF7D00AA92FDEDC98C69F356A0B97A8ED9AC95806FD3AF59F7832C4B1D9762C30EB86591A50A3449EE2F1E8A022ECB0FB7C8ACD41AA56E4A45F1246E5A5F0EC87DA414F54014DBAA59D1E14C62D1F8DBA2758F8D443A0D46F615A16F50BD33D6AEB3116BBA2A54D71AF06665CEB817718A50EEA77E461E0B4409AB7BED933E24F3317AE0B3BD1D3DEB3D40",
    "AMBB0209" "08D47A4799B157AC12151064C641D5B1CA91BC7FC82E2235A62D7193A6F112A83A56FB6F9DDB672A2C2A02052D3EDAD5F8FCC64F331BCD66942ED5747D572C8E8C9F9F8966AEB9ADCC57B43BAA3C77C13C71685298235644EB9AA714DCE2F5693C7A712C73E47DEEE52C74E9E096C4DCB4AD0FA8ADFAE4E4331271E79C1596B2E06DBB93EF7E84DB33C6A8BF0E85F56D23557FFE38BEF606AF728B0BE7A30D6E7798BF588F8E5E5C3A6292D337515D63FC3420A00CE72D2C21EFE2702749C9FBB8EBCF6FC07178B8D794DB502CAFBE69597596D9E5AD50FF090F55ED8E67D3C5B074A4A470D460B564D167BA1BB3B1B095D689612A7521CD5F01643CC6182654",
    "KFH0346" "21BFFDA65FA8AE347776615E411FCD5AB82DDB5A35BDABCE14564B21AD1B0D55919D6E1781217AB23BB0BB44A4AFABD1CE00D21D36CD254B706745EB68D43C5F12AACD34512CCC22D58DEF7E0C919E70518A71E90DDC64925EABA8D939D29DBA0642403FD20E7DF9845B01D49E6F486A6A81F27EF78D7D23201E9F5BC94BBB102E303AFBBAC36DFA2713927925519C2DD76F9BE6D705F0868413221E8BF1C4AFD6426B2B88D8B661D9DE6877C4ED4FC16C9BEBE942EB102EC193EE845DAB038541CC193B83717B7A6EAFBE88FCFAD09C46A9BFE541E072DD30E8A2077DBB58F27DEC41C014392BD13B6344059A8D204DCFC61A1B6215680DF9C8E510105C637A",
    "MBB0228" "94AA05E98B8203F4DC4105C5BB8D7BDFFE0CB3EDF8D46BBB5B2E7C95E6D59F360896110F556A1C9394DE1321E430EB956C269138F2803410E9DD7A40F9ABCDA2CA2E4BAC239C89E37AF846A20CDB71BD68C9C40014212FC9746E105B327CA76B16C79DFFE1BD28EA925FEA6BE612B3CC53E164D56B8E201F07EB9F01A2C2914C33CDFA1DD0493630708BB8B34D080DF88C2F3CD360FFB911B2D35CAECDA3BC439FBD5B6329F9DEBEED5BCAE6C7C1DF0175CA8D4141D01D7ABC05D3B89BDF03FCA7F4B39D0C33F2659C626EA8403B92A0660725308DC9255F9426C85A7324489FBC4AB31C937FC9815B8B4FDC29755F59B33CE9585E1DD426EC02F6C6CD41F08D",
    "MB2U0227" "2F0F2860BCCC13CB7182194269543A5DBD26A769359BAA707E432EFA4947E221B0F014852F67569C1C580824EEFF599203A9D91588A16A911597DE76517C087ACA1E61CFFA9B8DB81AFE96812CCDE47B594C6E93D38CF5B9182E4EEA0F0505FBF19B6D11D59FDD86FCAC037963734C6CB24A49666DD78A6BB6A155E11203FAB78BAD31C5A244E0ACF1AC31D613DC07861E3287D27C5091209FB0E21C95E94D46012971667E23F29697DFA565D5F49E6C884B336F3AF62839836E8EE3865492A4C7B49E9B2553005C6B14DFE5A82B7F46D8449ABCB727CDA142B6E0C06B60FF5044295A42304D02A87D5B302B01A825F734E80ED9883A10FFD4B5A8AFE9C6E2FB",
    "AGRO01" "AE0141E97F1064616831725F114334FCA65A8269AFF3C2F48866D7558A07F091F1DEC1C1469AC6F47CDB55E886CB6708A5FA34B9E668C06DCC8F34EDBB71D82C2EACE88EDF534BB68161CBEFAC79DEC7F804F072F0F57667A4DF84CC22A48290F2EEC2D6AE52E66431BFD95FF89BA010A2251AE1FCB714271E6793E7B03BAB5213684DCABCE1F2B864BD58087C35370FC032303DF77AE99EBB627DFE5F85B76B2971DFC3808A274FE6DA22BEAC50D19AA96D8407896E2F2F32CEB6A8AF3393940CB2335DF6EF7092DE143CB46609D10388EBEBEC7E16038D5E57427D35009881E250EC1458D4CB1B4EED535C1E6C4F9079F83693C9015461E3A5F88A469E05C8",
    "BCBB0235" "97397C293105BD3E4872E9DAC544C46D580CA10EF977B245C13B3990A96DFFD414C7333DE29CB131B7E6632980471B52F868CC0C12A683F14C87E16F3BD283F06183FA6006366804A07721FB32361F83FF51ADFF623D377FF74AB7CBA4C822A06CCFF6F674528D6C6C68642AE0DED2FB1D1297CCA4415545F5EFBBCDD0ADAD6F8FD1BFDE599FA0A1D6CC56E41F7E241CC828B683B0D980F6286023F8CD8C22BA7FD1D08D077C435C1C2FF7A578442E051CA1A94AAB25A8CD39D5640A2C3E0A8ECBAC1E6D7B12EBB13593587E2A67B6BFC2C34FF5BAE0D7E61F3E8E6FB29A661A4446253C487FEE7201EB7B1AAEAA77C9D17B0211CDF3256317AB6749852C5CB4",
    "TEST0023" "A0F15F699F30B2D2478910ABE3474A447F6A7F78F01571E688BFB84101F1887B32116F4A47888DE54ABC95298C10159CC71DF90C0E2D32622ABEC74A17E27964C09279D74D86D6EEE9E3AD2EE715D5716955B1275D7A379184C6E15427384D6711952ED4A7604B6A35411D69EA6DBCCDCE98991D8E1C5E44DC425F8489FB3A5E1A95B8F2DFD306D2219293629452137F653D269F0B353569966BA71672E483523CA54DFDE111FDA7CD21201DF3A82C386F8E3066ED35D0AF10D84E8B1585DD86881462294EE7BAC75B39278A6A4C0DC54A0AA71D4906FEA1C31B2E17A5C34DC6933E1F3E994A822C566868BEDCC389043663DA6E692EDD64FA6DB4E03F3BA7E5",
    "TEST0022" "80DB689200B420F25C10018597692C84605E6BA6B4A19563A75C88A2DA943E74AD7548BACA572C762E583A15F4E8509C11B9D1B286457F0A4A408E00E8DFFB0EABD6A54C4804219FE2B612DCB0BAB18198C50ECC65859790D0E2BDBCA3B8EF12892AD8423D88A6274203DEEFC3D6FEF3EF598E346F9DBA00532843B388ED1E34E2A01AA571FDDB5AD88204963CAFCE9F1AD5EE68EF86312CB3844CE39ECD5BCBDC89573E8C16B144027EF27A3D8FE28B92E16D28CE2B9A6CA48B672F59EE41A0F5C34B2BA6F9ED36E19EA3C6A8202F430ED50F937B72DD7610065E6E9B5F0D84A965DA1957AF4BC2643CF9CF1B73108235E88347061FF81CB5130CE9C896A8D6",
    "ABB0233" "142F0269828DEEBC39B16699E90E7749439D396CF61C56ACCB1BCB41D807573E36B26BA8FA3082BD3E295641033479252675DE5BEC088E66D85312A2B191DA832AB6A638A30AFE69AB3787BD146D5EF74A475BDFAB1709B63171C9AFCA5BBF90376B0B2F452EE2637E15C1FBC1803C1D65E1BB7BBD59707FCDE68817A4C1379A993306652AC4B15F9775C7465D71C5333AA5C96BB8B672AAB9D222428A5A37963E3B5A9EE117401F7AB753FA3C2C5DFAF25D34AB7D2AD38A83999E885B8E031927C2525F50A4DECE7CC93511EC2F74C7F209C0A0C5E0F604480E66669C07AD7F4CDFC03149C03D59EEB013892B3B7009597473CAAFEC707A9AF3B00B6AFF954F",
    "BKRM0602" "270CE62635A132101143D6601AA17C3B0EBEF0F20F45ED817D41E0F0ADBA517330307F848AA74D02778E12672D2F25B9F449490ADFE2DC6437B2F421481839AA6379C8B9A6B7DBD36CFBCA2C50C578B1668BE2B50EE3CD777199E1A3034013D054EF437E5B4B59C6323A0514D44D8E50D7C9689671DD4B5B444604426727BCC84BE5A4F0E4C4098C939832C9A96EE753EC4E5EBD7A5D09338C22037846FEBD82749E6F44442B939566BE633B3E550D307B9190255B58F2E6EDB10D5CAAE87C46EDB29FC05E58AC09BA75F5A4FB69ADF967BDAF4633BC10646ABE64CF9C3068A7D45D12C601B5A39FD5C36341863334808E9079BCFD96F5BF039418E897BEC7D2",
    "TEST0021" "02C4B7D25881F42C84E47AA985616E650712F6833265FF7CAB9BFA66110FB6D3F5DB024C613426FCC926FE81843D4513DCBBFA632CF7AC58CFF72ACC643971F8BF8DF2CFA93486B74821A9FDD9B896A5A8982C95076E691A34A4C619887D6CAFA72AD866CA1593B9141CCB854E85DCBF5AB1AA72AB5F1B1575DEC71BBFA38AD05A82A257A5761322D6C66C145E5D49BA0BE4C438ADE43E58A7C2754857D89E637CE4BDF36ED191D79614D569FF139A9C454F28F130006A0D38ECC08D37E45E4D1A004F72FF0CAFA81EBB83E0491DE21D6BE8F7BE213D418E3C5ADAB27DD0F16AC0DA5BCFEFBB86E21BF39B906D4F9EBEDF767830FF57B28DBF333E396DF9425D",
    "BIMB0340" "604F3DF0989B6870C48FF780D2E0D06F09FAA20EB54CA752B0FC49F0FF204395F596FD19BB6ACE9178259D26AC2FC0F38AE0CE16950E5E75AFE7C589232D52A7687DC1C2E46F475DC32BFB5C9FB095BCF0404E28468126D86C66FD706E646C0A6B6808305F878245111ACF92D5E4E04630C53100E7A6040D1F4BF2B53CFF96642F2674C2DDECF9DA69CB9AD6348A31B2828374ACFAB632F1F3C118E0CDF7CAA45335DDBAD37D66D56BC0F9306D9B6DBF3162300F0151D959FBC47A8F77526B3DE6393141C91FF565BCA812C7908BE3EAC9930E7EAF5C65A44A0529E75ADB92328F62ECF4E6B7D59E326677E5071E4F119E5D6DE90EA2A570457676996B743937",
    "PBB0233" "9816F4B237173A3EDE4EB9AD42A2DF5B7B3B4BA6DB84AA86EF40BAFBC5E7DEE58651649A49C3E8933A60B531317187756254B1F4884B01DEF06BC68FB6CE51D1F95D929484006092C6D91DF80989301E681CD7187A17799812EF9BACB74D630EAD55F59667B9E73ACBF8B701B2AA456E8A78D690747C0ED55AA0D5E0CF06AC776ABF2ABA1FB1CF5FE71474D44A9AD0A3588DD0E375BB1170FAB6AF51746B29FB62FEEB5D5231CC3C1E980DAC869580C6761CB64947DFC6FDA69B1280078A74D15DC32A571061460FBA4DA642872093EF09C0138D4DE0699D40C0340797CFF6563A8285102BB43DE8BF5F62368BCA9B9C05B436F5AEF8535D3032774C7C589D81",
    "ABMB0212" "7DBB9D154691B58DC1DE7945C5CB34627EBC71E447FD8CD53F359AEED54C01588694F3C2F3C7F1A967EFE8E79B2250CB7718C5DCB7CA6251E532DDA90E8780AFA6838DBAD584693A4743F7B51E36F6F56BB35F4FBCA89687D735853106E81307A4094313013AC97BAA417095A50DE0CE4E891ECD9C666AA06129A2C3B810A10E32850608B1B5FD6BA627ABD88AE7C128925EE6C86785D4D096BACD3767C81EA6932E91CDDA0536F648247E88D1274A7E4AC35D5A1E352EF8B55B071A0DA8AFBC612C28020A6EB312107B30FBFD51FC12D4B51EE74E6E98B604D790A7861A360304304DFF414F1072C9A2709613A5BC5A0FBF5AEBD05EDA8D03B4C7653616DCCF",
    "ABB0234" "A7F5762ED758400A2EFA2D09ED60BB2DD2B8402CEDA4A0BB7351247C6B2AD5DE3A4FBDC947002368954A601FEAA83132F05A21DB6893FEAAA463F37130AFDA0A829A7E1CE5F4D3651332E9A49BC8484B1AE1E7E5B04C0B95F8C379D37B8835168F71607C0E083C08809E500929934B27B849AFEC7E12A6F940130CA852B8EA42B03AF5B26C805F35E4E1C2197825A3CBDB9C976B84489784C1A1DC3B6DB27E9DD69BC170A3C006AD5C87B00BD0E02A1691D3604D5F50A0580C3A8EB3D6DA233C0CA708304AF06AD10F6752A9DC6F932C0C581E888221754A9F30DF18ECCE17377CC45708959D903632E1283873AB387CBEF4E96ECE76B77B72EE82DA4E23C4A9"},
   :form-params
   {:fpx_txnCurrency "MYR",
    :fpx_sellerId "SE00013501",
    :fpx_sellerExId "EX00011982",
    :fpx_buyerBankBranch "",
    :fpx_buyerIban "",
    :fpx_sellerExOrderNo "20210625182933",
    :fpx_productDesc "20210625182933",
    :fpx_sellerOrderNo "20210625182933",
    :fpx_msgToken "01",
    :fpx_buyerEmail "",
    :fpx_buyerAccNo "",
    :fpx_sellerBankCode "01",
    :fpx_sellerTxnTime "20210625182933",
    :fpx_buyerBankId "",
    :fpx_version "7.0",
    :fpx_buyerId "",
    :fpx_txnAmount "888.00",
    :fpx_buyerName "",
    :fpx_makerName "",
    :fpx_msgType "AR"}})

(comment
  ;; Pending
  {:fpx_txnCurrency "MYR",
   :fpx_sellerId "SE00013501",
   :fpx_sellerExId "EX00011982",
   :fpx_buyerBankBranch "SBI+BANK+A",
   :fpx_buyerIban "",
   :fpx_sellerExOrderNo "20210625182933",
   :fpx_sellerOrderNo "20210625182933",
   :fpx_fpxTxnTime "20210626004639",
   :fpx_creditAuthNo "",
   :fpx_fpxTxnId "2106260046390518",
   :fpx_msgToken "01",
   :fpx_debitAuthNo "",
   :fpx_debitAuthCode "09",
   :fpx_sellerTxnTime "20210625182933",
   :fpx_creditAuthCode "",
   :fpx_buyerBankId "TEST0021",
   :fpx_buyerId "",
   :fpx_txnAmount "888.00",
   :fpx_buyerName "",
   :fpx_makerName "",
   :fpx_msgType "AC",
   :fpx_checkSum "67B6FF9D9C61131585171E4B7576B5AB1F62DCD9D5EE01A0E2B4C1C381D3B1065ECBB586813B4D08BD1C7F2FA1A90CC70FA0D3D6CF3FA00CA735E4CEB3B6D01580357D80690A9744EB12E11FA5D4F905A7B575375E7F21870C30D207E40F16BCFF2E3A38AE3A89F9EE43FCEE607F23FE8DDF9A55A7B2BE4FE04E6149214086D5B5A5A29B2CF52AB1BAEAD02F2F094E221CFEBC6C85A546397CEE1AB855C002340E3D1D5D752AA79C2A8BCEF86ADC121556363CA5AF32F766DF4EABDB35FED932410104315F463B8D829C9AA88477FBC671EF6C887D3A5FAD64AB855300AECE44D5094E8E000EE6ABE0E469CA3DD859D958B19943B57BE2B0CEAA9890F8C1AD10"}

  ;; Successful
  {:fpx_txnCurrency "MYR",
   :fpx_sellerId "SE00013501",
   :fpx_sellerExId "EX00011982",
   :fpx_buyerBankBranch "SBI+BANK+A",
   :fpx_buyerIban "",
   :fpx_sellerExOrderNo "20210625182933",
   :fpx_sellerOrderNo "20210625182933",
   :fpx_fpxTxnTime "20210626004639",
   :fpx_creditAuthNo "9999999999",
   :fpx_fpxTxnId "2106260046390518",
   :fpx_msgToken "01",
   :fpx_debitAuthNo "15733223",
   :fpx_debitAuthCode "00",
   :fpx_sellerTxnTime "20210625182933",
   :fpx_creditAuthCode "00",
   :fpx_buyerBankId "TEST0021",
   :fpx_buyerId "",
   :fpx_txnAmount "888.00",
   :fpx_buyerName "N@ME()/+.-&BUYER",
   :fpx_makerName "New+Simulator",
   :fpx_msgType "AC",
   :fpx_checkSum "23C3E1D189A58EB9EFC995849436636B6532D2FAE2B507E14841F89A971938DB3A3ECDE500063BF7B30708367112F50DAA9FDEFF6154D27D76AA991C581395B0A279EE18096C96D7351BCB1D454D862A780C359DCB817116682ADBC781C6458B4E08EFB681A7DC0A3D0CB70DEE8E5E6C96F91D8FEFD02FC66FE3BFE5F2CCF26A85B633F58BC49D41C4B8BA7D8976A3F28EEC6D4C82B2A30353A7EAA1DC99E9F961D8C44635B2B321D92781BCAD68ECCCC67C66FDE55E8817BD4AB70878A4ABA80E55B287DD41A57D9AB6608371E81BA855FF5E2EDF9FA2EFC346D60CAB65D2B1E59268329AE36B99C28431BC7676F6D1172D758674ED61A687E19CF087A2D3DF"})

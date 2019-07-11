package com.example.demo.util.pingan;


/**
 * @author wangzy
 * @date 2019/5/29
 */
public class PingAnHttpConfig {

    public static final String OPEN_ID = "28363a84f274b6e7fe9f57ac10a7c76c";//聚合支付参数

    public static final String OPEN_KEY = "48c95a50a9a7ae31d39e3dc309b42faf";//聚合支付参数

    public static final String OPEN_URL = "https://api_uat.orangebank.com.cn/mct1/";//聚合支付参数

    public static final String SHOPPASS = TLinxSHA1.SHA1("123456");// 主管密码，对密码进行sha1加密，默认为123456

    public static final String PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC1zYh2PBQPPnuu" +
            "xQasZIaZNRscKiMfCU89p5Pcx33aPOPG2vVGupLHGpmxyu/vqFfXeBXnuZ1el30m" +
            "ToutRQNfUvtPr1PGX24POUP1+CxbDF6ximE0bpO3Gyl2fb4eaJ9i06G8328UXNL9" +
            "1xEv3eiWotItK50EK857FTizmLenABsICBMvTJ2qn0rxdVijLqeonjog8Dj0c+p0" +
            "v7utXNqhkvHXVMxpa6YYCbAqBjJt+0i4aNT33pDrxagjHvK6j2Yh4V+5vD2hNpbd" +
            "QJuEkITd5458vcJW9uFxB/gWRqDQRtumy+xgDI+TaqPDvgfWhKtcOGsFehLvpQ2N" +
            "Oow4JhTDAgMBAAECggEBALGkTFPjbmJa1nW2NUgbzd1EI+JIVhsMeK4w4W0YTKQD" +
            "XTnAV2wuAB+moUk1l28gmfyDbTwIhXmZIoNcumSrC5mINuHs2jn6o0si1IIyZfzt" +
            "QFXQdhzKc3G2i7QpN6z049CoI+CunhZSgSPhkvyno44VOLNEa19ZAqAH42ixr7nv" +
            "3BRVM+DYevl3gcXmE5qeOnKUM4H9te2JNoHwRgDIeyq9bN2/Ha2yDDdi48ajF2s+" +
            "ubdgII4mnaWDjhrTURpN1javt7kq7JBvofGCT7NtMwfvEJilqJdY+LbybxC7NwZk" +
            "MJcejMLV/RPG+RtCGmqL/qYgJi2O9ihN0KuCZsukQvkCgYEA7JZ77sGfEYuRDWpA" +
            "1klsPGTUouFVtmWIP3T9N5xdN9xRyOhfY7/Xk65Z2A5EAGy7Aw9g98s5aTxnv7OZ" +
            "pKs/BKxXwlg7zCtR2z6OywMLlBa2ju49nb8PIoaHEyp66Uuj9pF5NT1m2wxB1cez" +
            "KwHBmliazf/eoyv8dKvi/FSmPKcCgYEAxLhLHG3J1fFePLT9wkJHFwzdV9ICkfXc" +
            "SecQH41ifanAhyBUhSigROwB0vovv24GCrvjcPjKWa8Akkm0dDYWeugFB5+4G6w0" +
            "jqY6rbL4mpVZC4TVR+CAQ+1t4I4QmRfg2eJwoWwfLu0XnniY+1SzznbQWtVVo1hs" +
            "uEv54gyLHoUCgYEAtmUJpLcXCDK+IEaDN/EhBaqCmBP6vYSnNqzdrurYnwE2+BnV" +
            "uJQlAdDPqRbObDlnL+PCUScW2r/cCFnRILd4/QRVIBpf93aKQ+mFspNlcMTV93lK" +
            "fXvXkjl+l1MPsR5EiQn8FQSCcGuRsdukkIppFLIsnpYdsVRu7A+8DebAxk8CgYBn" +
            "eGQRfPHHtSATIEPE9KV9y0d1FMJDUaVfvchuQCiI3kulB21NaNP7zIMdHHkW4GEP" +
            "LkpwNnwAdhr/1wKyiWyDkxxqKSlmbMpsKaT9jgBTFrDybRjdqIjusai0jyTN0ZB0" +
            "KjWBwmQYg56DvP0CXUgCFd57mHBl7XGp8lRqAJ0AHQKBgQDnEE95qEtWEmkK9ZGk" +
            "nOuEBgzIfNVYQMqycTc702khIi1wR18uo6LSh7D0Z5qp9LRzrbe6HLuR80wbmdP9" +
            "1+JG7nZKxgNjczkMIZe1163DWE0XdqCzVBElugDcpbMD4dCmWZhEKd0lVlQAoP9z" +
            "BCXihpgg+atAKC3TkSLaIRnuRA==";//聚合支付私钥


    public static final String PaymentStatusQuery = "PaymentStatusQuery";// 支付单笔查询

    public static final String OrderDailyStatement = "OrderDailyStatement";// 订单每日对账单查询

    public static final String OrderRefund = "OrderRefund";// 单笔退款

    public static final String OrderRefundQuery = "OrderRefundQuery";// 退款交易查询

    public static final String OpenCustAcctId = "OpenCustAcctId";// 会员子账户开立

    public static final String BindRelateAcctUnionPay = "BindRelateAcctUnionPay";// 会员绑定提现账户银联鉴权

    public static final String BindRelateAccReUnionPay = "BindRelateAccReUnionPay";// 会员绑定提现账户回填银联鉴权短信码

    public static final String BindRelateAcctSmallAmount = "BindRelateAcctSmallAmount";// 会员绑定提现账户小额鉴权

    public static final String CheckAmount = "CheckAmount";// 会员绑定提现账户验证鉴权金额

    public static final String SmallAmountTransferQuery = "SmallAmountTransferQuery";// 会员绑定提现账户验证鉴权金额

    public static final String MemberBindQuery = "MemberBindQuery";// 查询小额鉴权转账结果

    public static final String MemberWithdrawCash = "MemberWithdrawCash";// 会员绑定信息查询

    public static final String CustAcctIdBalanceQuery = "CustAcctIdBalanceQuery";// 会员提现支持手续费

    public static final String CustAcctIdHistoryBalanceQuery = "CustAcctIdHistoryBalanceQuery";// 查询子帐号历史余额及待转可提现状态信息

    public static final String SingleTransactionStatusQuery = "SingleTransactionStatusQuery";// 查询银行单笔交易状态

    public static final String BankWithdrawCashBackQuery = "BankWithdrawCashBackQuery";// 查询银行提现退单信息

    public static final String BankWithdrawCashDetailsQuery = "BankWithdrawCashDetailsQuery";// 查询银行时间段内清分提现明细

    public static final String QueryCustAcctIdByThirdCustId = "QueryCustAcctIdByThirdCustId";// 根据会员代码查询会员子账号

    public static final String MntMbrBindRelateAcctBankCode = "MntMbrBindRelateAcctBankCode";// 维护会员绑定提现账户联行号

    public static final String ReconciliationDocumentQuery = "ReconciliationDocumentQuery";// 查询对账文件信息

    public static final String AccountRegulation = "AccountRegulation";// 平台调账

    public static final String ChargeDetailQuery = "ChargeDetailQuery";// 查询充值明细

    public static final String PlatformAccountSupply = "PlatformAccountSupply";// 平台补账

    public static final String UnbindRelateAcct = "UnbindRelateAcct";// 会员解绑提现账户

    public static final String PAYORDER = "payorder";//平安聚合支付

    public static final String PAYLIST = "paylist";//3.2	获取门店支付方式列表

    public static final String ORDER = "order";// 3.3	获取订单列表

    public static final String ORDERVIEW = "order/view";// 3.4	查询订单明细

    public static final String PAYSTATUS = "paystatus";// 3.5	查询付款状态

    public static final String PAYCANCEL = "paycancel";// 3.6	订单取消接口

    public static final String PAYREFUND = "payrefund";// 3.7	订单退款接口

    public static final String PAYREFUNDQUERY = "payrefundquery";// 3.9	订单退款查询接口

    public static final String BILLDOWNLOADBILL = "bill/downloadbill";//	3.10	下载商户对账单

}

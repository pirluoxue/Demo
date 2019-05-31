package com.example.demo.model.entity.ali;

import com.alibaba.fastjson.JSONObject;

/**
 * @Classname AliPayAgreementConstants
 * @Description 支付宝代扣静态参数类
 * @Date 2019-05-15
 * @Created by chen_bq
 */
public class AliPayAgreementConstants {

    public static String PRODUCT_CODE_OAUTH = "ONE_TIME_AUTH";
    public static String PRODUCT_CODE_TOPAY = "GENERAL_WITHHOLDING";
    public static String PRODUCT_CODE_QUICK_WAP_WAY = "QUICK_WAP_WAY";
    public static String PERSONAL_PRODUCT_CODE = "ONE_TIME_AUTH_P";
    public static String SIGN_SCENE = "INDUSTRY|VENDING_MACHINE";
    public static String THIRD_PARTY_TYPE = "PARTNER";
    public static String CHANNEL = "ALIPAYAPP";
    public static String CHANNEL_SCANFACE = "SCANFACE";

    public static String ORDERINFO_SYNC_BIZ_TYPE = "CREDIT_DEDUCT";
    public static String ORDERINFO_SYNC_ORDER_BIZ_INFO_COMPLETE;
    public static String ORDERINFO_SYNC_ORDER_BIZ_INFO_CLOSED;
    public static String ORDERINFO_SYNC_ORDER_BIZ_INFO_VIOLATED;

    static {
        JSONObject o = new JSONObject();
        o.put("status", "COMPLETE");
        ORDERINFO_SYNC_ORDER_BIZ_INFO_COMPLETE = o.toString();
        o.put("status", "CLOSED");
        ORDERINFO_SYNC_ORDER_BIZ_INFO_CLOSED = o.toString();
        o.put("status", "VIOLATED");
        ORDERINFO_SYNC_ORDER_BIZ_INFO_VIOLATED = o.toString();
    }

    public static void main(String[] args) {
        System.out.println(ORDERINFO_SYNC_ORDER_BIZ_INFO_COMPLETE);
        System.out.println(ORDERINFO_SYNC_ORDER_BIZ_INFO_CLOSED);
        System.out.println(ORDERINFO_SYNC_ORDER_BIZ_INFO_VIOLATED);
    }

}

package com.example.demo.model.entity.common;

import com.google.common.collect.ImmutableMap;

/**
 * @author Zhao Junjian
 */
public enum StatusCode implements RestStatus {

    OK(20000, "请求成功"),

    NO_CONTENT(20400,"No_Content"),
    // 40xxx 客户端不合法的请求
    INVALID_MODEL_FIELDS(40001, "字段校验非法"),

    /**
     * 参数类型非法，常见于SpringMVC中String无法找到对应的enum而抛出的异常
     */
    INVALID_PARAMS_CONVERSION(40002, "参数类型非法"),

    SAVE_EXCEPTION(40003,"入库异常"),

    AUTH_FAIL(40100,"授权失败"),

    AUTH_TOKEN_NOT_EXISTS(40101,"TOKEN不存在"),

    AUTH_TOKEN_INVALID(40102,"TOKEN无效"),

    AUTH_DATA_PERMIT_FAIL(40103,"数据授权失败"),

    // 41xxx 请求方式出错
    /**
     * http media type not supported
     */
    HTTP_MESSAGE_NOT_READABLE(41001, "HTTP消息不可读"),

    /**
     * 请求方式非法
     */
    REQUEST_METHOD_NOT_SUPPORTED(41002, "不支持的HTTP请求方法"),

    // 成功接收请求, 但是处理失败
    /**
     * Duplicate Key
     */
    DUPLICATE_KEY(42001, "操作过快, 请稍后再试"),

    /**
     * 用于登录时用户不存在的情况
     */
    USER_NOT_EXISTS(42002, "用户不存在"),

    /**
     * 用于下订单时的产品检查
     */
    PRODUCT_NOT_EXISTS(42003, "产品不存在"),

    /**
     * 订单不存在
     */
    ORDER_NOT_EXISTS(42004, "订单不存在"),

    /**
     * 库存不足
     */
    INSUFFICIENT_PRODUCT(42005, "库存不足"),

    /**
     * 余额不足
     */
    INSUFFICIENT_BALANCE(42006, "余额不足"),


    //订单相关
    PROCESS_ORDER_EXISTS(42201,"您有订单尚未完成"),

    PLATE_DEBT_ORDER_EXISTS(42202,"此车牌存在欠款订单未缴清"),

    PERIOD_NOT_BOOKINGSTATE(42203,"当前时段不可预约"),

    ACCOUNT_DEBT_EXISTS(42204,"账户存在欠款未缴清"),

    BERTHSTATE_NOT_BOOKING(42205,"车位当前不可预约"),

    RESERVE_ORDER_CREATE_FAIL(42206,"预订单生成失败"),

    USER_XBCOUPON_NOT_EXISITS(42207,"优惠券不存在"),

    ORDER_PREPAY_FAIL(42208,"订单预交费失败"),

    WECHATPAY_NOTIFY_CHECK_ERROR(42209,"微信支付消息校验出错"),

    ORDER_CONFIRM_ERROR(42210,"订单资源确认出错"),

    USER_TEL_NOT_EXISITS(42211,"请绑定手机号"),

    ORDER_STATUS_MODIFY_FAIL(42212,"订单处于不可更改状态"),

    ORDER_STATUS_CANCEL_FAIL(42214,"订单处于不可取消状态"),

    ORDER_CANCEL_FAIL(42215,"订单取消失败"),

    INVALID_ORDER_PAYCHANNEL(42217,"订单支付通道数据异常"),

    ORDER_REFUND_SUCCESS_EXISTS(42218,"订单退款成功记录已存在"),

    ORDER_STATE_CHANGE_FAIL(42219,"订单状态更新失败"),

    ORDER_PAYCHANNEL_ERROR(42221,"订单支付通道错误"),

    ORDER_ALLOCATION_NOT_SUPPORT(42222,"订单不支持调配"),

    ORDER_ALLOCATION_STATE_ERROR(42223,"订单不处于可调配状态"),

    ORDER_DEBTED(42224,"订单已欠费"),

    XBCOUPONE_CONSUME_FAIL(42225,"优惠券消费失败"),

    ORDER_NOT_DONE(42226,"订单未完成"),

    /**
     * 区域不存在
     */
    AREA_NOT_EXISTS(42110, "区域不存在"),


    // 50xxx 服务端异常
    /**
     * 用于处理未知的服务端错误
     */
    SERVER_UNKNOWN_ERROR(50001, "服务端异常, 请稍后再试"),

    /**
     * 用于远程调用时的系统出错
     */
    SERVER_IS_BUSY_NOW(50002, "系统繁忙, 请稍后再试");

    private final int code;

    private final String message;

    private static final ImmutableMap<Integer, StatusCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, StatusCode> builder = ImmutableMap.builder();
        for (StatusCode statusCode : values()) {
            builder.put(statusCode.code(), statusCode);
        }
        CACHE = builder.build();
    }

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static StatusCode valueOfCode(int code) {
        final StatusCode status = CACHE.get(code);
        if (status == null) {
            throw new IllegalArgumentException("No matching constant for [" + code + "]");
        }
        return status;
    }

    public static ImmutableMap getStatusList(){
        return CACHE;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

}

package com.example.demo.model.entity.ali.common;

import com.alipay.api.internal.mapping.ApiField;
import lombok.Data;

/**
 * @Classname ALiNotifyEntity
 * @Description TODO
 * @Date 2019-07-01
 * @Created by chen_bq
 */
@Data
public class ALiNotifyEntity {

    @ApiField("ord_no")
    private String ord_no;//订单号
    @ApiField("amount")
    private String amount;//交易金额（分为单位）
    @ApiField("out_no")
    private String out_no;
    @ApiField("rand_str")
    private String rand_str;
    @ApiField("trade_result")
    private TradeResult tradeResult;
    @ApiField("open_id")
    private String openId;
    @ApiField("open_key")
    private String openKey;
    @ApiField("pay_time")
    private long payTime; //交易成功时间（yyyymmddhhiiss）
    @ApiField("status")
    private String status;//订单状态（1交易成功，2待支付，9待输入密码，4已取消）
    @ApiField("timestamp")
    private long timestamp;

}

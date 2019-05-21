package com.example.demo.controller.ali;

import com.alibaba.fastjson.JSON;
import com.example.demo.model.entity.ali.AliPayCommonConstant;
import com.example.demo.model.entity.form.OrderForm;
import com.example.demo.model.entity.form.UserForm;
import com.example.demo.service.Impl.OrderServiceImpl;
import com.example.demo.service.Impl.UserServiceImpl;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import com.example.demo.util.SpringContextUtils;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname AliPayNotify
 * @Description TODO
 * @Date 2019-05-21
 * @Created by chen_bq
 */
@Controller
public class AliPayNotifyController {

    private static Logger logger = LoggerFactory.getLogger(AliPayNotifyController.class);

    @RequestMapping(value = "/api/notify")
    public String aliNotify(HttpServletRequest request, HttpServletResponse response){
        System.out.println("支付宝回调");
        Map<String, String> params = new HashMap<String, String>();

        // 取出所有参数是为了验证签名
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            params.put(parameterName, request.getParameter(parameterName));
        }
        System.out.println("支付宝返回参数：" + JSON.toJSONString(params));
        String code = params.get("code");
        if(!"10000".equals(code)){
            return "index";
        }
        //代扣协议中标示用户的唯一签约号
        String external_agreement_no = params.get("external_agreement_no");
        //支付宝系统中用以唯一标识用户签约记录的编号
        String agreement_no = params.get("agreement_no");
        //用户的芝麻信用openId，供商户查询用户芝麻信用使用
        String zm_open_id = params.get("zm_open_id");
        //用户签约的支付宝账号对应的支付宝唯一用户号。以2088开头的16位纯数字组成
        String alipay_user_id = params.get("alipay_user_id");
        //协议的当前状态。
        String status = params.get("status");
        //返回脱敏的支付宝账号，如需要返回不脱敏的支付宝用户账号，需要用户在签约页面上授权
        String alipay_logon_id = params.get("alipay_logon_id");
//        UserService userService = SpringContextUtils.getBean(UserServiceImpl.class);
//        UserForm userForm = new UserForm();
//        userForm.setUserUserid(alipay_user_id);
//        userForm.setUserExternalagreementno(external_agreement_no);
//        userForm.setUserAgreementno(agreement_no);
//        userForm.setUserStatus(status);
//        userForm.setUserLogonid(alipay_logon_id);
//        userService.saveAndUpdate(userForm);
//        System.out.println("更新信息：" + userForm);
        return "index";
    }

    @RequestMapping(value = "/api/asyncOauthNotify")
    @ResponseBody
    public String asyncOauthNotify(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("支付宝异步授权回调");
        Map<String, String> params = new HashMap<String, String>();

        // 取出所有参数是为了验证签名
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            params.put(parameterName, request.getParameter(parameterName));
        }
        System.out.println("支付宝异步授权返回参数：" + JSON.toJSONString(params));
        //代扣协议中标示用户的唯一签约号
        String external_agreement_no = params.get("external_agreement_no");
        //支付宝系统中用以唯一标识用户签约记录的编号
        String agreement_no = params.get("agreement_no");
        //用户的芝麻信用openId，供商户查询用户芝麻信用使用
        String zm_open_id = params.get("zm_open_id");
        //用户签约的支付宝账号对应的支付宝唯一用户号。以2088开头的16位纯数字组成
        String alipay_user_id = params.get("alipay_user_id");
        //协议的当前状态。
        String status = params.get("status");
        //返回脱敏的支付宝账号，如需要返回不脱敏的支付宝用户账号，需要用户在签约页面上授权
        String alipay_logon_id = params.get("alipay_logon_id");
        //异步类型，用于和status一起判断用户合约是否正常
        String dut_user_sign = params.get("dut_user_sign");
        UserService userService = SpringContextUtils.getBean(UserServiceImpl.class);
        UserForm userForm = new UserForm();
        userForm.setUserUserid(alipay_user_id);
        userForm.setUserExternalagreementno(external_agreement_no);
        userForm.setUserAgreementno(agreement_no);
        userForm.setUserStatus(status);
        userForm.setUserLogonid(alipay_logon_id);
        userForm.setUserUpdatetime(new Timestamp(System.currentTimeMillis()));
        if (Strings.isNullOrEmpty(alipay_user_id)) {
            //不存在user_id则直接新建入库，避免数据丢失
            System.out.println("异常信息入库");
            userForm.setUserRemarks("异常信息入库");
            userService.addUser(userForm);
        } else {
            System.out.println("更新信息");
            userService.saveAndUpdate(userForm);
        }
        return AliPayCommonConstant.SUCCESS;
    }



    @RequestMapping(value = "/api/asyncTradePayNotify")
    @ResponseBody
    public String asyncTradePayNotify(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("支付宝异步支付回调");
        Map<String, String> params = new HashMap<String, String>();

        // 取出所有参数是为了验证签名
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            params.put(parameterName, request.getParameter(parameterName));
        }
        System.out.println("支付宝异步返回参数：" + JSON.toJSONString(params));

        OrderForm orderForm = getOrderFormByAliPayMap(params);
        OrderService orderService = SpringContextUtils.getBean(OrderServiceImpl.class);
        orderService.addOrder(orderForm);

        UserService userService = SpringContextUtils.getBean(UserServiceImpl.class);
        UserForm user = userService.getUserByUserId(orderForm.getBuyeruserid());
        user.setUserStatus("STOP");
        System.out.println("支付回调完成！更新订单状态");

        return AliPayCommonConstant.SUCCESS;
    }

    private OrderForm getOrderFormByAliPayMap(Map<String, String> params){
        //支付宝交易号
        String trade_no = params.get("trade_no");
        //商户订单号
        String out_trade_no = params.get("out_trade_no");
        //买家支付宝账号
        String buyer_logon_id = params.get("buyer_logon_id");
        //卖家支付宝用户号
        String seller_id = params.get("seller_id");
        //订单金额
        String total_amount = params.get("total_amount");
        //买家的支付宝用户Uid
        String buyer_id = params.get("buyer_id");
        //交易成功：TRADE_SUCCESS，交易完结：TRADE_FINISHED，
        // 交易创建：WAIT_BUYER_PAY，交易关闭：TRADE_CLOSED。
        String trade_status = params.get("trade_status");
        //用户在交易中支付的可开具发票的金额
        String invoice_amount = params.get("invoice_amount");
        //商家在交易中实际收到的款项，单位为元
        String receipt_amount = params.get("receipt_amount");
        //使用集分宝支付的金额
        String point_amount = params.get("point_amount");
        //退款金额
        String refund_fee = params.get("refund_fee");
        //用户在交易中支付的金额
        String buyer_pay_amount = params.get("buyer_pay_amount");
        //订单标题
        String subject = params.get("subject");
        //商品描述
        String body = params.get("body");
        //交易创建时间
        String gmt_create = params.get("gmt_create");
        //交易付款时间
        String gmt_payment = params.get("gmt_payment");
        //交易退款时间
        String gmt_refund = params.get("gmt_refund");
        //交易结束时间
        String gmt_close = params.get("gmt_close");
        //支付金额信息
        String fund_bill_list = params.get("fund_bill_list");
        //本交易支付时所使用的所有优惠券信息
        String voucher_detail_list = params.get("voucher_detail_list");
        //本次交易支付所使用的单品券优惠的商品优惠信息
        String discount_goods_detail = params.get("discount_goods_detail");

        OrderForm orderForm = new OrderForm();
        orderForm.setBuyerid(buyer_id);
        orderForm.setBuyerlogonid(buyer_logon_id);
        orderForm.setBuyerpayamount(new BigDecimal(buyer_pay_amount));
        orderForm.setReceiptamount(receipt_amount);
        orderForm.setTradeno(trade_no);
        orderForm.setTotalamount(new BigDecimal(total_amount));
        orderForm.setGmtpayment(Timestamp.valueOf(gmt_payment));
        orderForm.setOuttradeno(out_trade_no);
        orderForm.setBuyeruserid(buyer_id);
        if ("TRADE_SUCCESS".equals(trade_status)){
            orderForm.setOrderstatus(OrderForm.ORDER_STATUS_PAID);
        }else{
            orderForm.setOrderstatus(OrderForm.ORDER_STATUS_UNABLE_PAY);
        }
        return orderForm;
    }





}

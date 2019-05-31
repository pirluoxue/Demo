package com.example.demo.controller.ali;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.AlipayTradeOrderinfoSyncRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeOrderinfoSyncResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.example.demo.model.entity.ali.AliPayAgreementConstants;
import com.example.demo.model.entity.form.OrderForm;
import com.example.demo.model.entity.form.UserForm;
import com.example.demo.model.entity.jooq.tables.pojos.Order;
import com.example.demo.service.Impl.UserServiceImpl;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import com.example.demo.util.SpringContextUtils;
import com.example.demo.util.ali.AlipayConfig;
import com.example.demo.util.ali.DefaultAlipayClientFactory;
import com.example.demo.util.ali.Result;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Classname AliPayController
 * @Description TODO
 * @Date 2019-05-15
 * @Created by chen_bq
 */

@Data
@Controller
public class AliPayController {

    private final static Logger logger = LoggerFactory.getLogger(AliPayController.class);

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/api/aliAgreementTradePay", method = RequestMethod.POST)
    @ResponseBody
    public Object AliAgreementTradePay(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, String userId){
        Result<AlipayTradePayResponse> result = new Result<AlipayTradePayResponse>();
        Properties prop = AlipayConfig.getProperties();
        AlipayTradePayModel alipayModel = getAlipayTradePayModelByUserId(userId);

        //初始化请求类
        AlipayTradePayRequest alipayRequest = new AlipayTradePayRequest();
        //设置业务参数，alipayModel为前端发送的请求信息，开发者需要根据实际情况填充此类
        alipayRequest.setBizModel(alipayModel);
        alipayRequest.setReturnUrl(prop.getProperty("RETURN_URL"));
        alipayRequest.setNotifyUrl(prop.getProperty("NOTIFY_URL_TRADE_PAY"));
        //sdk请求客户端，已将配置信息初始化
        AlipayClient alipayClient = DefaultAlipayClientFactory.getAlipayClient();
        try {
            //因为是接口服务，使用exexcute方法获取到返回值
            AlipayTradePayResponse alipayResponse = alipayClient.execute(alipayRequest);
            if(alipayResponse.isSuccess()){
                System.out.println("调用成功");
                //TODO 实际业务处理，开发者编写。可以通过alipayResponse.getXXX的形式获取到返回值
                //尽量不要在这里写订单逻辑，因为至此，支付还未完成，需要等待异步回调。
                // 如果在这里直接处理订单信息，则存在数据重复等异常
                UserService userService = SpringContextUtils.getBean(UserServiceImpl.class);
                UserForm userForm = userService.getUserByUserId(alipayResponse.getBuyerUserId());
                userForm.setUserUpdatetime(new Timestamp(System.currentTimeMillis()));
                userForm.setUserStatus("UNSIGN");
                userService.editUser(userForm);
            } else {
                System.out.println("调用失败");
                System.out.println(alipayResponse.getSubMsg());
            }
            result.setSuccess(true);
            result.setValue(alipayResponse);
            return result;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            if(e.getCause() instanceof java.security.spec.InvalidKeySpecException){
                result.setMessage("商户私钥格式不正确，请确认配置文件Alipay-Config.properties中是否配置正确");
                return result;
            }
        }
        return null;
    }

    @RequestMapping(value = "/api/aliAgreementRepaymentAsnc", method = RequestMethod.POST)
    @ResponseBody
    public Object AliAgreementRepaymentAsnc(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, String userId){
        Result<AlipayTradeOrderinfoSyncResponse> result = new Result<AlipayTradeOrderinfoSyncResponse>();
        Properties prop = AlipayConfig.getProperties();
        AlipayTradeOrderinfoSyncModel alipayTradeOrderinfoSyncModel = getAlipayTradeOrderinfoSyncModelByUserId(userId);
        if(alipayTradeOrderinfoSyncModel == null){
            result.setSuccess(false);
            result.setMessage("不存在需要同步的订单");
            return result;
        }

        //初始化请求类
        AlipayTradeOrderinfoSyncRequest alipayRequest = new AlipayTradeOrderinfoSyncRequest();
        //设置业务参数，alipayModel为前端发送的请求信息，开发者需要根据实际情况填充此类
        alipayRequest.setBizModel(alipayTradeOrderinfoSyncModel);
        //sdk请求客户端，已将配置信息初始化
        AlipayClient alipayClient = DefaultAlipayClientFactory.getAlipayClient();
        try {
            //因为是接口服务，使用exexcute方法获取到返回值
            AlipayTradeOrderinfoSyncResponse alipayResponse = alipayClient.execute(alipayRequest);
            if(alipayResponse.isSuccess()){
                System.out.println("同步支付宝订单。调用成功");
                OrderForm orderForm = new OrderForm();
                orderForm.setBuyeruserid(alipayResponse.getBuyerUserId());
                orderForm.setOuttradeno(alipayResponse.getOutTradeNo());
                orderForm.setTradeno(alipayResponse.getTradeNo());
                List<OrderForm> orderForms = orderService.queryOrderListByCondition(orderForm);
                if(orderForms == null || orderForms.size() <= 0){
                    logger.debug("同步信息异常，直接暂时存库");
                    orderForm.setOrderstatus(OrderForm.ORDER_STATUS_SYNC_EXCEPTION);
                    orderForm.setGmtpayment(new Timestamp(System.currentTimeMillis()));
                    orderService.addOrder(orderForm);
                }else{
                    orderForm.setId(orderForms.get(0).getId());
                    orderForm.setOrderstatus(OrderForm.ORDER_STATUS_PAID);
                    orderForm.setGmtpayment(new Timestamp(System.currentTimeMillis()));
                    orderService.editOrder(orderForm);
                    System.out.println("更新同步订单信息：" + (Order)orderForm);
                }
                //TODO 实际业务处理，开发者编写。可以通过alipayResponse.getXXX的形式获取到返回值
            } else {
                System.out.println("调用失败");
                System.out.println(alipayResponse.getSubMsg());
            }
            result.setSuccess(true);
            result.setValue(alipayResponse);
            return result;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            if(e.getCause() instanceof java.security.spec.InvalidKeySpecException){
                result.setMessage("商户私钥格式不正确，请确认配置文件Alipay-Config.properties中是否配置正确");
                return result;
            }
        }
        return null;
    }

    @RequestMapping(value = "/api/aliTradeRePay", method = RequestMethod.POST)
    @ResponseBody
    public Object AliTradeRePay(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, String userId){
        Result<AlipayTradeWapPayResponse> result = new Result<AlipayTradeWapPayResponse>();
        Properties prop = AlipayConfig.getProperties();

        AlipayTradeWapPayModel alipayModel = getAliTradeRePay();

        //初始化请求类
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        //设置业务参数，alipayModel为前端发送的请求信息，开发者需要根据实际情况填充此类
        alipayRequest.setBizModel(alipayModel);
        alipayRequest.setReturnUrl(prop.getProperty("RETURN_URL"));
        alipayRequest.setNotifyUrl(prop.getProperty("NOTIFY_URL_TRADE_PAY"));
        //sdk请求客户端，已将配置信息初始化
        AlipayClient alipayClient = DefaultAlipayClientFactory.getAlipayClient();
        try {
            //因为是接口服务，使用exexcute方法获取到返回值
            AlipayTradeWapPayResponse alipayResponse = alipayClient.pageExecute(alipayRequest);
            if(alipayResponse.isSuccess()){
                System.out.println("调用成功");
                result.setSuccess(true);
                //TODO 实际业务处理，开发者编写。可以通过alipayResponse.getXXX的形式获取到返回值
            } else {
                System.out.println("调用失败");
            }
            result.setSuccess(true);
            result.setValue(alipayResponse);
            return result;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            if(e.getCause() instanceof java.security.spec.InvalidKeySpecException){
                result.setMessage("商户私钥格式不正确，请确认配置文件Alipay-Config.properties中是否配置正确");
                return result;
            }
        }
        return null;
    }

    private AlipayTradeWapPayModel getAliTradeRePay(){
        AlipayTradeWapPayModel alipayModel = new AlipayTradeWapPayModel();

        alipayModel.setBody("欠款还款");
        alipayModel.setOutTradeNo(getOutTradeNo());
        alipayModel.setProductCode(AliPayAgreementConstants.PRODUCT_CODE_QUICK_WAP_WAY);
        //默认不填
        alipayModel.setSubject("测试欠款还款");
        alipayModel.setTimeoutExpress("3d");
        alipayModel.setTotalAmount("0.01");
        return alipayModel;
    }

    private AlipayTradeOrderinfoSyncModel getAlipayTradeOrderinfoSyncModelByUserId(String userId){
        OrderForm condition = new OrderForm();
        //暂时只搜查询无法购买的订单进行同步
        condition.setBuyeruserid(userId);
        condition.setOrderstatus(OrderForm.ORDER_STATUS_UNABLE_PAY);
        List<OrderForm> orderForms = orderService.queryOrderListByCondition(condition);
        if(orderForms == null || orderForms.size() <= 0){
            return null;
        }
        OrderForm syncOrder = orderForms.get(0);
        AlipayTradeOrderinfoSyncModel alipayModel = new AlipayTradeOrderinfoSyncModel();
        alipayModel.setTradeNo(syncOrder.getTradeno());
        alipayModel.setOutRequestNo(syncOrder.getOuttradeno());
        alipayModel.setBizType(AliPayAgreementConstants.ORDERINFO_SYNC_BIZ_TYPE);
        alipayModel.setOrderBizInfo(AliPayAgreementConstants.ORDERINFO_SYNC_ORDER_BIZ_INFO_COMPLETE);
        return alipayModel;
    }

    private AlipayTradePayModel getAlipayTradePayModelByUserId(String userId){
        UserService userService = SpringContextUtils.getBean(UserServiceImpl.class);
        UserForm userForm = userService.getUserByUserId(userId);
        AlipayTradePayModel alipayModel = new AlipayTradePayModel();
        AgreementParams agreementParams = new AgreementParams();
        agreementParams.setAgreementNo(userForm.getUserAgreementno());
        alipayModel.setAgreementParams(agreementParams);

        alipayModel.setBody("智能货柜免密代扣");
        alipayModel.setBuyerId(userId);
        List<GoodsDetail> goodsDetails = getGoodsDetails();
        alipayModel.setGoodsDetail(goodsDetails);
        alipayModel.setOutTradeNo(getOutTradeNo());
        alipayModel.setProductCode(AliPayAgreementConstants.PRODUCT_CODE_TOPAY);
        //默认不填
//        alipayModel.setSellerId();
        alipayModel.setSubject("智能货柜免密代扣");
        alipayModel.setTerminalId("1010101");
        alipayModel.setTimeoutExpress("3d");
        alipayModel.setTotalAmount("0.01");
        alipayModel.setUndiscountableAmount("0.01");
        return alipayModel;
    }

    /**
     * 商品详情
     * @return
     */
    private List<GoodsDetail> getGoodsDetails(){
        List<GoodsDetail> goodsDetails= new ArrayList<>();
        GoodsDetail goodsDetail = new GoodsDetail();
        goodsDetail.setBody("可乐");
        goodsDetail.setGoodsId("233");
        goodsDetail.setGoodsName("可口可乐");
        goodsDetail.setGoodsCategory("饮料");
        goodsDetail.setPrice("0.01");
        goodsDetail.setQuantity(1L);
        goodsDetails.add(goodsDetail);
        return goodsDetails;
    }

    /**
     * 商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
     * @return
     */
    private String getOutTradeNo(){
        String outTradeNo = "ASD" + System.currentTimeMillis();
        return outTradeNo;
    }





}

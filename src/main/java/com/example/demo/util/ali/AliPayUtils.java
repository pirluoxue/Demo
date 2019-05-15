package com.example.demo.util.ali;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayUserAgreementPageSignModel;
import com.alipay.api.request.AlipayUserAgreementPageSignRequest;
import com.alipay.api.response.AlipayUserAgreementPageSignResponse;
import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

/**
 * @Classname AliPayUtils
 * @Description 支付宝支付工具类
 * @Date 2019-05-13
 * @Created by chen_bq
 */
public class AliPayUtils {

    private Logger log = Logger.getLogger(AliPayUtils.class);


    public Object alipayUserAgreementPageSign(AlipayUserAgreementPageSignModel alipayModel){
        Result<AlipayUserAgreementPageSignResponse> result = new Result<AlipayUserAgreementPageSignResponse>();
        Properties prop = AlipayConfig.getProperties();

        String charset = prop.getProperty("CHARSET");

        //初始化请求类
        AlipayUserAgreementPageSignRequest alipayRequest = new AlipayUserAgreementPageSignRequest();
        //添加demo请求标示，用于标记是demo发出
//        alipayRequest.putOtherTextParam(AlipayConfig.ALIPAY_DEMO, AlipayConfig.ALIPAY_DEMO_VERSION);
        //设置业务参数，alipayModel为前端发送的请求信息，开发者需要根据实际情况填充此类
        alipayRequest.setBizModel(alipayModel);
        alipayRequest.setReturnUrl(prop.getProperty("RETURN_URL"));
        alipayRequest.setNotifyUrl(prop.getProperty("NOTIFY_URL"));
        System.out.println("alipayRequest="+alipayRequest.getBizContent()+alipayRequest.getReturnUrl()+alipayRequest.getBizModel());
        //sdk请求客户端，已将配置信息初始化
        AlipayClient alipayClient = DefaultAlipayClientFactory.getAlipayClient();
        try {
            //因为是页面跳转类服务，使用pageExecute方法得到form表单后输出给前端跳转
            AlipayUserAgreementPageSignResponse alipayResponse = alipayClient.pageExecute(alipayRequest);

            // 调用SDK生成表单
            String form = alipayResponse.getBody();
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



}

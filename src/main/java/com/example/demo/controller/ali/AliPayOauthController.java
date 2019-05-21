package com.example.demo.controller.ali;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayUserAgreementPageSignModel;
import com.alipay.api.domain.AlipayUserAgreementQueryModel;
import com.alipay.api.domain.AlipayUserAgreementUnsignModel;
import com.alipay.api.request.AlipayUserAgreementPageSignRequest;
import com.alipay.api.request.AlipayUserAgreementQueryRequest;
import com.alipay.api.request.AlipayUserAgreementUnsignRequest;
import com.alipay.api.response.AlipayUserAgreementPageSignResponse;
import com.alipay.api.response.AlipayUserAgreementQueryResponse;
import com.alipay.api.response.AlipayUserAgreementUnsignResponse;
import com.example.demo.model.entity.ali.AliPayAgreementConstants;
import com.example.demo.model.entity.ali.AlipayServiceEnvConstants;
import com.example.demo.model.entity.common.ObjectDataResponse;
import com.example.demo.model.entity.form.UserForm;
import com.example.demo.service.Impl.UserServiceImpl;
import com.example.demo.service.UserService;
import com.example.demo.util.SpringContextUtils;
import com.example.demo.util.ali.AliUtils;
import com.example.demo.util.ali.AlipayConfig;
import com.example.demo.util.ali.DefaultAlipayClientFactory;
import com.example.demo.util.ali.Result;
import com.example.demo.util.common.UrlUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Properties;

/**
 * @Classname TestALiPayController
 * @Description 测试支付宝控制层
 * @Date 2019-05-13
 * @Created by chen_bq
 */
@Controller
public class AliPayOauthController {

    private Logger logger = Logger.getLogger(AliPayOauthController.class);

    @RequestMapping(value = "index")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/api/getOauth", method = RequestMethod.POST)
    @ResponseBody
    public ObjectDataResponse<String> getOauthAli(String redirectUrl){
        StringBuilder sb = new StringBuilder();
        sb.append("https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=" + AlipayServiceEnvConstants.APP_ID);
        sb.append("&scope=auth_user&redirect_uri=" + UrlUtils.encode(redirectUrl));
        ObjectDataResponse objectDataResponse = ObjectDataResponse.builder()
                .data(sb.toString())
                .build();
        return objectDataResponse;
    }

    @RequestMapping(value = "/api/aliLogin")
    public String aliLogin(String auth_code, HttpServletRequest httpServletRequest) {
        AliUtils aLiUtils = new AliUtils();
        if(aLiUtils.authTokenForUser(auth_code, httpServletRequest)){
            return "index";
        }else{
            return "index";
        }
    }


    @RequestMapping(value = "/api/alipayUserAgreementPageSign", method = RequestMethod.POST)
    @ResponseBody
    public Object AlipayUserAgreementPageSign(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        AlipayUserAgreementPageSignModel alipayModel = new AlipayUserAgreementPageSignModel();
        Result<String> result = new Result();
        Properties prop = AlipayConfig.getProperties();

        String charset = prop.getProperty("CHARSET");

        //初始化请求类
        AlipayUserAgreementPageSignRequest alipayRequest = new AlipayUserAgreementPageSignRequest();
        alipayRequest.setBizContent(getBizContent());
        //设置业务参数，alipayModel为前端发送的请求信息，开发者需要根据实际情况填充此类
        alipayRequest.setBizModel(alipayModel);
        //可以通过设定默认值，也可以通过动态传入
        alipayRequest.setReturnUrl(prop.getProperty("RETURN_URL"));
        alipayRequest.setNotifyUrl(prop.getProperty("NOTIFY_URL_OAUTH"));
        System.out.println("alipayRequest="+alipayRequest.getBizContent()+alipayRequest.getReturnUrl()+alipayRequest.getBizModel());
        //sdk请求客户端，已将配置信息初始化
        AlipayClient alipayClient = DefaultAlipayClientFactory.getAlipayClient();
        try {

            //请求form方式
            //因为是页面跳转类服务，使用pageExecute方法得到form表单后输出给前端跳转
//            AlipayUserAgreementPageSignResponse alipayResponse = alipayClient.pageExecute(alipayRequest);
//            Result<AlipayUserAgreementPageSignResponse> result = new Result<AlipayUserAgreementPageSignResponse>();
//            // 调用SDK生成表单
//            String form = alipayResponse.getBody();
//            result.setSuccess(true);
//            result.setValue(alipayResponse);
            //另一种方法
            AlipayUserAgreementPageSignResponse alipayResponse = alipayClient.pageExecute(alipayRequest, "get");
            result.setSuccess(true);
            String schema = "alipays://platformapi/startapp?appId=60000157&appClearTop=false&startMultApp=YES&bizType=ONE_TIME_AUTH&sign_params=";
            String param = alipayResponse.getBody();
            String subString = "https://openapi.alipay.com/gateway.do?";
            param = param.substring(subString.length(), param.length());
            result.setValue(schema + URLEncoder.encode(param, "UTF-8"));
            return result;

        } catch (AlipayApiException | UnsupportedEncodingException e) {
            e.printStackTrace();
            if(e.getCause() instanceof java.security.spec.InvalidKeySpecException){
                result.setMessage("商户私钥格式不正确，请确认配置文件Alipay-Config.properties中是否配置正确");
                return result;
            }
        }

        return null;
    }

    @RequestMapping(value = "/api/alipayUserAgreementUnsign", method = RequestMethod.POST)
    @ResponseBody
    public Object AlipayUserAgreementUnsign(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, String userId){
        AlipayUserAgreementUnsignModel alipayModel = new AlipayUserAgreementUnsignModel();
        alipayModel.setAlipayUserId(userId);

        alipayModel.setPersonalProductCode("ONE_TIME_AUTH_P");
        alipayModel.setSignScene("INDUSTRY|VENDING_MACHINE");
        alipayModel.setThirdPartyType("PARTNER");

        UserService userService = SpringContextUtils.getBean(UserServiceImpl.class);
        UserForm conditionFrom = new UserForm();
        conditionFrom.setUserUserid(userId);
        List<UserForm> userForms = userService.queryUserListByConditionNoPage(conditionFrom);
        if(userForms == null || userForms.size() <= 0){
            return null;
        }
        alipayModel.setExternalAgreementNo(userForms.get(0).getUserExternalagreementno());

        Result<AlipayUserAgreementUnsignResponse> result = new Result<AlipayUserAgreementUnsignResponse>();
        Properties prop = AlipayConfig.getProperties();

        //初始化请求类
        AlipayUserAgreementUnsignRequest alipayRequest = new AlipayUserAgreementUnsignRequest();
        //设置业务参数，alipayModel为前端发送的请求信息，开发者需要根据实际情况填充此类
        alipayRequest.setBizModel(alipayModel);
        alipayRequest.setReturnUrl(prop.getProperty("RETURN_URL"));
        alipayRequest.setNotifyUrl(prop.getProperty("NOTIFY_URL_OAUTH"));
        System.out.println("alipayRequest="+alipayRequest.getBizContent()+alipayRequest.getReturnUrl()+alipayRequest.getBizModel());
        //sdk请求客户端，已将配置信息初始化
        AlipayClient alipayClient = DefaultAlipayClientFactory.getAlipayClient();
        try {
            //因为是接口服务，使用exexcute方法获取到返回值
            AlipayUserAgreementUnsignResponse alipayResponse = alipayClient.execute(alipayRequest);

            if(alipayResponse.isSuccess()){
                System.out.println("调用成功");
                //TODO 实际业务处理，开发者编写。可以通过alipayResponse.getXXX的形式获取到返回值
                UserForm queryForm = userService.getUserByUserId(userId);
                if(queryForm != null){
                    queryForm.setUserStatus("1");
                    userService.editUser(queryForm);
                }
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



    @RequestMapping(value="/api/alipayUserAgreementQuery", method = RequestMethod.POST)
    @ResponseBody
    public Object AlipayUserAgreementQuery(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,  String userId){
        AlipayUserAgreementQueryModel alipayModel = new AlipayUserAgreementQueryModel();
        alipayModel.setAlipayUserId(userId);

        alipayModel.setPersonalProductCode("ONE_TIME_AUTH_P");
        alipayModel.setSignScene("INDUSTRY|VENDING_MACHINE");
        alipayModel.setThirdPartyType("PARTNER");

        UserService userService = SpringContextUtils.getBean(UserServiceImpl.class);
        UserForm conditionFrom = new UserForm();
        conditionFrom.setUserUserid(userId);
        List<UserForm> userForms = userService.queryUserListByConditionNoPage(conditionFrom);
        if(userForms == null || userForms.size() <= 0){
            return "不存在该用户";
        }
        alipayModel.setExternalAgreementNo(userForms.get(0).getUserExternalagreementno());

        Result<AlipayUserAgreementQueryResponse> result = new Result<AlipayUserAgreementQueryResponse>();
        Properties prop = AlipayConfig.getProperties();

        //初始化请求类
        AlipayUserAgreementQueryRequest alipayRequest = new AlipayUserAgreementQueryRequest();
        //设置业务参数，alipayModel为前端发送的请求信息，开发者需要根据实际情况填充此类
//        alipayRequest.setBizContent(getBizContentForQuery(userId));

        alipayRequest.setBizModel(alipayModel);
        alipayRequest.setReturnUrl(prop.getProperty("RETURN_URL"));
        alipayRequest.setNotifyUrl(prop.getProperty("NOTIFY_URL_OAUTH"));
        //sdk请求客户端，已将配置信息初始化
        AlipayClient alipayClient = DefaultAlipayClientFactory.getAlipayClient();
        try {
            //因为是接口服务，使用exexcute方法获取到返回值
            AlipayUserAgreementQueryResponse alipayResponse = alipayClient.execute(alipayRequest);
            if(alipayResponse.isSuccess()){
                System.out.println("调用成功");
                //TODO 实际业务处理，开发者编写。可以通过alipayResponse.getXXX的形式获取到返回值
            } else {
                System.out.println(alipayResponse.getSubCode());
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

    private String getBizContent(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personal_product_code",AliPayAgreementConstants.PERSONAL_PRODUCT_CODE);
        jsonObject.put("sign_scene",AliPayAgreementConstants.SIGN_SCENE);
        JSONObject access_params=new JSONObject();
        access_params.put("channel",AliPayAgreementConstants.CHANNEL);
//        access_params.put("channel",AliPayAgreementConstants.CHANNEL_SCANFACE);
        jsonObject.put("access_params",access_params);
        JSONObject deviceNo=new JSONObject();
        deviceNo.put("out_device_id","1010101");
        jsonObject.put("device_params",deviceNo);
        jsonObject.put("product_code", AliPayAgreementConstants.PRODUCT_CODE_OAUTH);
        jsonObject.put("external_agreement_no","TEST" + System.currentTimeMillis());
        jsonObject.put("third_party_type",AliPayAgreementConstants.THIRD_PARTY_TYPE);
        return jsonObject.toString();
    }

    private String getBizContentForQuery(String userId){
        //personal_product_code+sign_scene+external_agreement_no+alipay_logon_id+alipay_user_id来查询
        // ，入参值按照商户调签约接口(alipay.user.agreement.page.sign)中的对应值传入
        // ；如果传入external_agreement_no，则alipay_logon_id与alipay_user_id可不传
        // ，否则alipay_logon_id与alipay_user_id必须传其中一个。
        //注：sign_scene按照签约时的值传入
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personal_product_code",AliPayAgreementConstants.PERSONAL_PRODUCT_CODE);
        jsonObject.put("sign_scene", AliPayAgreementConstants.SIGN_SCENE);
        jsonObject.put("external_agreement_no","TEST1557820046751");
        jsonObject.put("product_code", AliPayAgreementConstants.PRODUCT_CODE_OAUTH);
        jsonObject.put("third_party_type",AliPayAgreementConstants.THIRD_PARTY_TYPE);
        jsonObject.put("alipay_user_id", userId);
        return jsonObject.toString();
    }



}

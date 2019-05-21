package com.example.demo.util.ali;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.example.demo.model.entity.ali.AlipayServiceEnvConstants;
import com.example.demo.model.entity.form.UserForm;
import com.example.demo.service.Impl.UserServiceImpl;
import com.example.demo.service.UserService;
import com.example.demo.util.SpringContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

/**
 * @Classname ALiUtils
 * @Description 支付宝工具类
 * @Date 2019-05-13
 * @Created by chen_bq
 */
public class AliUtils {

    /**
     * 授权获取用户信息
     */
    public boolean authTokenForUser(String authCode, HttpServletRequest httpServletRequest){

        try {
            //3. 利用//**获得authToken
            AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
            oauthTokenRequest.setCode(authCode);
            oauthTokenRequest.setGrantType(AlipayServiceEnvConstants.GRANT_TYPE);
            AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient
                    .execute(oauthTokenRequest);

            //成功获得authToken
            System.out.println(oauthTokenResponse.getAccessToken());
            if (null != oauthTokenResponse && oauthTokenResponse.isSuccess()) {

                //4. 利用authToken获取用户信息
                AlipayUserInfoShareRequest userinfoShareRequest = new AlipayUserInfoShareRequest();
                AlipayUserInfoShareResponse userinfoShareResponse = alipayClient.execute(
                        userinfoShareRequest, oauthTokenResponse.getAccessToken());

                //成功获得用户信息
                if (null != userinfoShareResponse && userinfoShareResponse.isSuccess()) {
                    //这里仅是简单打印， 请开发者按实际情况自行进行处理
                    System.out.println("获取用户信息成功：" + userinfoShareResponse.getBody());
                    JSONObject jsonObject = JSONObject.parseObject(userinfoShareResponse.getBody())
                            .getJSONObject("alipay_user_info_share_response");
                    UserService userService = SpringContextUtils.getBean(UserServiceImpl.class);
                    UserForm userForm = new UserForm();
                    userForm.setUserAddress(jsonObject.getString("city"));
                    userForm.setUserGender(jsonObject.getString("gender"));
                    userForm.setUserName(jsonObject.getString("nick_name"));
                    userForm.setUserUserid(jsonObject.getString("user_id"));
                    userForm.setUserUpdatetime(new Timestamp(System.currentTimeMillis()));
                    userForm.setUserStatus(UserForm.STSTUS_TEMP);
                    userForm.setUserEnable(UserForm.ENABLE);
                    userService.saveAndUpdate(userForm);
                    httpServletRequest.getSession().setAttribute("userId", userForm.getUserUserid());
                    return true;

                } else {
                    //这里仅是简单打印， 请开发者按实际情况自行进行处理
                    System.out.println("获取用户信息失败");
                    return false;
                }
            } else {
                //这里仅是简单打印， 请开发者按实际情况自行进行处理
                System.out.println("authCode换取authToken失败");
                return false;
            }
        } catch (AlipayApiException alipayApiException) {
            //自行处理异常
            alipayApiException.printStackTrace();
        }
        return false;
    }

}

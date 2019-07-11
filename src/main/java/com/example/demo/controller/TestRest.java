package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.entity.ali.common.ALiNotifyEntity;
import com.example.demo.model.entity.jooq.tables.pojos.User;
import com.example.demo.util.pingan.TLinx2Util;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * @Classname TestRest
 * @Description 一个专用于测试连接相关的控制类
 * @Date 2019-05-10
 * @Created by chen_bq
 */
@RestController
public class TestRest {

    @RequestMapping("test/restSimple")
    public String testRest() {
        System.out.println("收到rest~~~");
        return "hello world";
    }

    @RequestMapping("test/rest")
    public String testRest(HttpServletRequest request) throws IOException {
//        Map<String, String[]> map = request.getParameterMap();
//        String[] simpleEntity = map.get("SimpleEntity(map");
//        System.out.println(simpleEntity[0]);
        /*仅能选择一种方法获取HttpServletRequest内的参数，哪怕使用getParameterMap也会清空请求参数*/
        printlnParam(request);
        return "hello world";
    }

    private void printlnParam(HttpServletRequest request) throws IOException {
        //获得输入流
        ServletInputStream servletInputStream = request.getInputStream();
        //创建StringBuilder暂存信息
        StringBuilder content = new StringBuilder();
        byte[] b = new byte[1024];
        int lens = -1;
        //读入流
        while ((lens = servletInputStream.read(b)) > 0) {
            //写入StringBuilder
            content.append(new String(b, 0, lens));
        }
        String strcont = content.toString();// 内容
        System.out.println(strcont);
    }

    @RequestMapping(value = "test/restpost", method = RequestMethod.POST)
    public String restpost(@RequestBody User user){
        return user.toString();
    }


    @RequestMapping(value = "test/notify")
    public JSONObject notify(@RequestBody String jsonParams){
        System.out.println(jsonParams);
        try {
            ALiNotifyEntity aLiNotifyEntity = JSON.toJavaObject(JSONObject.parseObject(jsonParams), ALiNotifyEntity.class);
            System.out.println(aLiNotifyEntity);
        }catch (Exception e){
            System.out.println("转化失败");
        }
        
//        Map paramsMap = aLiNotifyEntity.getTradeResult().getParamsMap();
//        System.out.println(paramsMap);

        boolean flag = TLinx2Util.verifySign(JSONObject.parseObject(jsonParams));
        System.out.println("验签结果： " + flag);
//        try {
//            boolean flag = AlipaySignature.rsaCheckV1(aLiNotifyEntity.getTradeResult().getParamsMap(), AlipayServiceEnvConstants.ALIPAY_PUBLIC_KEY, "utf-8", "RSA2");
//            System.out.println(flag);
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//        Iterator it = jsonParam.keySet().iterator();
//        while (it.hasNext()){
//            String key = (String)it.next();
//            System.out.println("参数名： " + key + "  值为：" + jsonParam.get(key));
//            if (key.equalsIgnoreCase("data")){
//                System.out.println("解析data参数值 ");
//                LinkedHashMap map = (LinkedHashMap) jsonParam.get(key);
//                Iterator its = map.keySet().iterator();
//                while (its.hasNext()){
//                    String keys = (String)its.next();
//                    System.out.println("参数名： " + keys + "  值为：" + map.get(keys));
//                }
//            }
//        }
        JSONObject success = new JSONObject();
        success.put("result", "notify_success");
        return success;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        User Null = new User();
        User user = Objects.requireNonNull(Null, "空指针User");
    }

    public static String unicodeEncode(String string) {
        char[] utfBytes = string.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

}

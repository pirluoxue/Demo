package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.components.annotation.OperationLog;
import com.example.demo.model.entity.ali.common.ALiNotifyEntity;
import com.example.demo.model.entity.common.Description;
import com.example.demo.model.entity.common.LogType;
import com.example.demo.model.entity.jooq.tables.pojos.User;
import com.example.demo.model.entity.simple.TestHttpPostEntity;
import com.example.demo.service.Impl.SparkServiceImpl;
import com.example.demo.util.common.CommonUtil;
import com.example.demo.util.pingan.TLinx2Util;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;

/**
 * @Classname TestRest
 * @Description 一个专用于测试连接相关的控制类
 * @Date 2019-05-10
 * @Created by chen_bq
 */
@RestController
public class TestRest {

    @Autowired
    private SparkServiceImpl sparkService;

    @RequestMapping("test/restSimple")
    public String testRest() {
        System.out.println("收到rest~~~");
        return "hello world";
    }

    @RequestMapping("test/rest")
    public String testRest(HttpServletRequest request) {
//        Map<String, String[]> map = request.getParameterMap();
//        String[] simpleEntity = map.get("SimpleEntity(map");
//        System.out.println(simpleEntity[0]);
        /*仅能选择一种方法获取HttpServletRequest内的参数，哪怕使用getParameterMap也会清空请求参数
         * 现在通过过滤器实现多次读取*/
        String str = CommonUtil.getPostBodyToStringByRequest(request);
        System.out.println(str);
        return "hello world";
    }

    @RequestMapping(value = "test/longPolling", method = RequestMethod.POST)
    public DeferredResult testLongPolling(){
        // 初始化并设备1000ms的超时时间
        DeferredResult output = new DeferredResult<>(2000L);
        // DeferredResult支持设置超时，即在后端一致没有数据的情况下，多长时间断开连接。使用的是onTimeout()方法。
        output.onTimeout(() ->
            output.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body("Request timeout occurred.")));
        // 如果后端出现了错误，可以设置onError()方法修改返回状态码：
        output.onError(o -> {
            output.setErrorResult(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred."));
        });

        // 模拟6s后才有数据返回
//        ForkJoinPool.commonPool().submit(() -> {
//            System.out.println("Processing in separate thread");
//            try {
//                Thread.sleep(6000);
//            } catch (InterruptedException e) {
//            }
//            output.setResult(ResponseEntity.ok("ok"));
//        });

        // 模拟出现数据后返回
        ForkJoinPool.commonPool().submit(() -> {
            if (longPolling()){
                try {
                    output.setResult(ResponseEntity.ok("longpolling return 喵喵喵".getBytes("utf8")));
                    System.out.println("成功返回");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else {
                // 模拟超时
                try {
                    Thread.sleep(6000L);
                    output.setResult(ResponseEntity.ok("timeout 6000L".getBytes("utf8")));
                } catch (InterruptedException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                System.out.println("timeout 6000L");
            }
        });

        System.out.println("servlet thread freed");
        return output;
    }

    private static int longpolling = 0;
    private boolean longPolling(){
        longpolling ++;
        if (longpolling < 90){
            return false;
        }
        if (longpolling >= 100){
            longpolling = 0;
        }
        return true;
    }

    @RequestMapping(value = "test/post_entity", method = RequestMethod.POST)
    public String testPostEntity(HttpServletRequest request, @Valid @RequestBody TestHttpPostEntity entity) {
        if (entity != null) {
            System.out.println(entity);
            return "success";
        }
        String str = CommonUtil.getPostBodyToStringByRequest(request);
        System.out.println(str);
        return "can't received";
    }

    @RequestMapping(value = "test/serializable", method = RequestMethod.POST)
    public String testSerializable(@Valid @RequestBody TestHttpPostEntity entity) {
        if (entity != null) {
            System.out.println(entity);
            // 手动序列化返回，否则JsonField的配置可能无效
            return JSON.toJSONString(entity);
        }
        return "can't received";
    }

    @RequestMapping(value = "test/post_entity/delay", method = RequestMethod.POST)
    public String testPostEntityForDelay(HttpServletRequest request, @RequestBody TestHttpPostEntity entity) {
        Long delayMillSecond = 3000L;
        try {
            Thread.sleep(delayMillSecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (entity != null) {
            System.out.println(entity);
            return "success";
        }
        String str = CommonUtil.getPostBodyToStringByRequest(request);
        System.out.println(str);
        return "can't received";
    }

    @OperationLog(logtype = LogType.CommonType, description = Description.TEST_GET_PARAM)
    @RequestMapping(value = "test/get_param", method = RequestMethod.GET)
    public String testGetParam(HttpServletRequest request, @RequestParam String name, @RequestParam Integer number) {
        if (!Strings.isNullOrEmpty(name) && number != null) {
            System.out.println("name " + name + "  number " + number);
            return "success";
        }
        return "can't received";
    }

    @RequestMapping(value = "test/restpost", method = RequestMethod.POST)
    public String restpost(@RequestBody User user) {
        return user.toString();
    }


    @RequestMapping(value = "test/notify")
    public JSONObject notify(@RequestBody String jsonParams) {
        System.out.println(jsonParams);
        try {
            ALiNotifyEntity aLiNotifyEntity = JSON.toJavaObject(JSONObject.parseObject(jsonParams), ALiNotifyEntity.class);
            System.out.println(aLiNotifyEntity);
        } catch (Exception e) {
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

    @RequestMapping(value = "test/spark", method = RequestMethod.GET)
    public JSONObject testSpark() {
        JSONObject success = new JSONObject();
        success.put("result", sparkService.test());
        return success;
    }

    public static void main(String[] args) {
        User nullUser = new User();
        User user = Objects.requireNonNull(nullUser, "空指针User");
    }

    public static String unicodeEncode(String string) {
        char[] utfBytes = string.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexByte = Integer.toHexString(utfBytes[i]);
            if (hexByte.length() <= 2) {
                hexByte = "00" + hexByte;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexByte;
        }
        return unicodeBytes;
    }

}

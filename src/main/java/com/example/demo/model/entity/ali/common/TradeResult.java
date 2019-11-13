package com.example.demo.model.entity.ali.common;

import com.alipay.api.internal.mapping.ApiField;
import com.alipay.api.response.AlipayTradeQueryResponse;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname TradeResult
 * @Description TODO
 * @Date 2019-07-01
 * @Created by chen_bq
 */
@Data
public class TradeResult {

    @ApiField("sing")
    private String sign;
    @ApiField("alipay_trade_query_response")
    private AlipayTradeQueryResponse alipayTradeQueryResponse;

    public Map<String, String> getParamsMap() {
        Map<String, String> params = new HashMap<>();
        params.put("sign", this.sign);
        Field[] fields = AlipayTradeQueryResponse.class.getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                if (!Modifier.isStatic(fields[i].getModifiers()) && !Modifier.isFinal(fields[i].getModifiers())) {
                    String name = fields[i].getName();
                    String firstLetter = name.substring(0, 1).toUpperCase();
                    String getter = "get" + firstLetter + name.substring(1);
                    Method method = this.alipayTradeQueryResponse.getClass().getMethod(getter, new Class[]{});
                    Object value = method.invoke(this.alipayTradeQueryResponse, new Object[]{});
                    if(value != null){
                        params.put(name, value.toString());
                    }else{
                        params.put(name, null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }
}

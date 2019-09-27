package com.example.demo.components.annotation;

import com.example.demo.components.exception.ParamException;
import com.example.demo.util.StringUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;

/**
 * @author chen_bq
 * @description 自定义@myRequestparam，错误请求返回
 * @create: 2019-03-13 14:05
 **/
public class MyRequestParamMethodArgumentResolver implements HandlerMethodArgumentResolver {

    public static String DEFAULT_FORMAT = "utf-8";

    public MyRequestParamMethodArgumentResolver() {
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        if (methodParameter.hasParameterAnnotation(MyRequestParam.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        MyRequestParam myRequestParamAnnotation = methodParameter.getParameterAnnotation(MyRequestParam.class);
        String key = "";
        String value = "";
        if(StringUtil.isBlank(myRequestParamAnnotation.value())){
            key = methodParameter.getParameterName();
        }else{
            key = myRequestParamAnnotation.value();
        }
        value = nativeWebRequest.getParameter(key);
        if(StringUtil.isNullOrEmpty(value)){
            throw new ParamException();
        }
        return getParameterFormatTypeByString(methodParameter.getParameterType(), value);
    }

    /**
     * @Author chen_bq
     * @Description 根据类型，将string数据转换成对应类型的数据
     * @Date 2019/3/14 10:19
     * @Param []
     * @return java.lang.Object
     **/
    private Object getParameterFormatTypeByString(Class<?> cls, String value){
        if(cls.isInstance(String.class)){
            return decodeString(value);
        }else if(cls.isInstance(boolean.class)){
            return asBoolean(value);
        }else if(cls.isInstance(double.class)){
            return asDouble(value);
        }else if(cls.isInstance(int.class)){
            return asInt(value);
        }else if(cls.isInstance(Long.class)){
            return asLong(value);
        }else if(cls.isInstance(BigDecimal.class)){
            return asBigDecimal(value);
        }
        return value;
    }

    private String decodeString(String string){
        if (StringUtil.isNullOrEmpty(string)){
            return null;
        }
        try {
            URLDecoder.decode(string, DEFAULT_FORMAT);
            string.trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

    private boolean asBoolean(String string){
        if(StringUtil.isNullOrEmpty(string)){
            return false;
        }
        if("true".equals(string.toLowerCase())){
            return true;
        }else {
            return false;
        }
    }

    private double asDouble(String string){
        if(StringUtil.isNullOrEmpty(string)){
            return 0.0;
        }else{
            return Double.parseDouble(string);
        }
    }

    private int asInt(String string){
        if(StringUtil.isNullOrEmpty(string)){
            return 0;
        }else {
            return Integer.parseInt(string);
        }
    }

    private Long asLong(String string){
        if(StringUtil.isNullOrEmpty(string)){
            return 0L;
        }else {
            return Long.parseLong(string);
        }
    }

    private BigDecimal asBigDecimal(String string){
        if(StringUtil.isNullOrEmpty(string)){
            return new BigDecimal(0.0);
        }else{
            return new BigDecimal(string);
        }
    }
}

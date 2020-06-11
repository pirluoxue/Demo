package com.example.demo.util.common;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class ReflectUtils {

    /**
     * @Author bangqiang_chen
     * @Description 根据属性名调用 getter方法
     * @Date 17:37 2020/6/10
     * @Param [fieldName 属性名, obj 执行对象]
     * @return java.lang.String
     **/
    public static String getValueByFiled(String fieldName, Object obj) throws Exception {
        //属性扫描器
        PropertyDescriptor pd = new PropertyDescriptor(fieldName, obj.getClass());
        //从属性描述器中获取 get 方法
        Method method = pd.getReadMethod();
        //结果
        Object value = method.invoke(obj);
        //执行方法并返回结果
        return value == null ? "" : String.valueOf(value);
    }

    /**
     * @Author bangqiang_chen
     * @Description 根据属性名调用 setter方法
     * @Date 17:46 2020/6/10
     * @Param [fieldName 属性名, obj 执行对象, value 输入值]
     * @return void
     **/
    public static void setValudByFiled(String fieldName, Object obj, Object value) throws Exception {
        //属性扫描器
        PropertyDescriptor pd = new PropertyDescriptor(fieldName, obj.getClass());
        //从属性描述器中获取 get 方法
        Method method = pd.getWriteMethod();
        //结果
        method.invoke(obj, value);
    }


}



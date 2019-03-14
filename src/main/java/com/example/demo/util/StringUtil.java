package com.example.demo.util;

/**
 * @author chen_bq
 * @description
 * @create: 2019-03-13 15:23
 **/
public class StringUtil {

    public static boolean isNullOrEmpty(String str){
        if(str == null || "".equals(str)){
            return true;
        }
        return false;
    }

    public static boolean isBlank(String str){
        if("".equals(str)){
            return true;
        }
        return false;
    }
}

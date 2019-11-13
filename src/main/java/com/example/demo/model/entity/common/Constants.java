package com.example.demo.model.entity.common;

/**
 * @author chen_bq
 * @description
 * @create: 2019/11/7 17:37
 **/
public class Constants {

    public static String funProperty = getFunProperty();
    public static int i = 0;

    public static String getFunProperty(){
        return "funProperty" + i++;
    }

}

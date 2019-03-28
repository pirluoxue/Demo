package com.example.demo.util;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class DateUtilsTest {

    public static void main(String[] args) {
        //LocalDateTime获得当前时间
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        /*格式化测试开始*/
        String formatter = "格式化:yyyy-MM-dd HH:mm:ss";
        String formatterDateTime = DateUtils.getDateTimeFormatterByString(localDateTime, formatter);
        System.out.println(formatterDateTime);
        /*格式化测试结束*/
        /*反格式化开始*/
        LocalDateTime antiFormatter = DateUtils.getDateTimeFormatterByString(formatterDateTime, formatter);
        System.out.println("反格式化" + antiFormatter);
        /*反格式化结束*/

    }

}
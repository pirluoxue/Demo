package com.example.demo.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.*;

public class DateUtilsTest {

    public static void main(String[] args) {
        //LocalDateTime获得当前时间
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        /*格式化测试开始*/
        String formatter = "格式化 : yyyy-MM-dd HH:mm:ss";
        String formatterDateTime = DateUtils.getDateTimeFormatterByString(localDateTime, formatter);
        System.out.println(formatterDateTime);
        /*格式化测试结束*/
        /*反格式化开始*/
        LocalDateTime antiFormatter = DateUtils.getDateTimeFormatterByString(formatterDateTime, formatter);
        System.out.println("反格式化 : " + antiFormatter);
        /*反格式化结束*/
        /*timestamp转localdatetime开始*/
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        LocalDateTime timestamp2LocalDateTime = timestamp.toLocalDateTime();
        System.out.println(timestamp2LocalDateTime);
        /*timestamp转localdatetime结束*/
        /*localdatetime转timestamp开始*/
        Timestamp localDateTime2Timestamp = Timestamp.valueOf(antiFormatter);
        System.out.println(localDateTime2Timestamp);
        /*localdatetime转timestamp结束*/
        /*Instant.now()和System.currentTimeMillis()测试开始*/
        System.out.println("System.currentTimeMillis() : " + System.currentTimeMillis());
        System.out.println("Instant.now() : " + Instant.now());
        System.out.println("LocalDateTime.ofInstant : " + LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        System.out.println("Timestamp.from : " + Timestamp.from(Instant.now()));
        /*Instant.now()和System.currentTimeMillis()测试结束*/
        /*LocalDateTime简单方法测试开始*/
        System.out.println("localDateTime.getDayOfYear() 今年的第" + localDateTime.getDayOfYear() + "天");
        System.out.println("localDateTime.getDayOfMonth() 这个月的第" + localDateTime.getDayOfMonth() + "天");
        System.out.println("localDateTime.getDayOfWeek() 这周" + localDateTime.getDayOfWeek());
        System.out.println("localDateTime.getDayOfWeek().getValue() 这周第" + localDateTime.getDayOfWeek().getValue() + "天");
        System.out.println("localDateTime.getYear() " + localDateTime.getYear() + "年");
        System.out.println("localDateTime.getMonth() " + localDateTime.getMonth() + "月");
        System.out.println("localDateTime.getMonth() 第" + localDateTime.getMonth().getValue() + "月");
        /*LocalDateTime简单方法测试结束*/


    }

}
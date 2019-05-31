package com.example.demo.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.time.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DateUtils.class)
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class DateUtilsTest {

    public static void main(String[] args) {
        //LocalDateTime获得当前时间
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        /*格式化测试开始*/
        String formatter = "格式化 : yyyy-MM-dd HH:mm:ss";
        String formatterDateTime = DateUtils.getDateTimeFormatterByFormatter(localDateTime, formatter);
        System.out.println(formatterDateTime);
        /*格式化测试结束*/
        /*反格式化开始*/
        LocalDateTime antiFormatter = DateUtils.getDateTimeFormatterByFormatter(formatterDateTime, formatter);
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
        System.out.println("localDateTime.getMonth() " + localDateTime.getMonth());
        System.out.println("localDateTime.getMonth() " + localDateTime.getMonth().getValue() + "月");
        /*LocalDateTime简单方法测试结束*/
    }

    @Test
    public void LocalDateAndLocalDateTimeTest() {
        String date = "2018-01-15";
        /*不含时间*/
        String formatter = "yyyy-MM-dd";
        LocalDate localDate = LocalDate.now();
        try {
        localDate = DateUtils.getDateByFormatter(date, formatter);
        System.out.println(localDate);
        System.out.println("localDate.atStartOfDay() " + localDate.atStartOfDay());
        System.out.println("localDate.minusDays(1) " + localDate.minusDays(1));
        System.out.println("localDate.minusDays(-1) " + localDate.minusDays(-1));
        }catch (Exception e) {
            System.out.println("不含时间 LocalDate转化失败");
        }
        /*不含日*/
        formatter = "yyyy-MM";
        try {
        localDate = DateUtils.getDateByFormatter(date, formatter);
        System.out.println(localDate);
        System.out.println("localDate.atStartOfDay() " + localDate.atStartOfDay());
        System.out.println("localDate.minusDays(1) " + localDate.minusDays(1));
        System.out.println("localDate.minusDays(-1) " + localDate.minusDays(-1));
        }catch (Exception e) {
            System.out.println("不含日 LocalDate转化失败");
        }
        /*含部分时间*/
        date = "2018-01-01 12";
        formatter = "yyyy-MM-dd HH";
        try {
            localDate = DateUtils.getDateByFormatter(date, formatter);
            System.out.println(localDate);
        }catch (Exception e){
            System.out.println("含部分时间 LocalDate转化失败");
        }
        /*含时间*/
        date = "2018-01-01 12:01:01";
        formatter = "yyyy-MM-dd HH:mm:ss";
        try {
            localDate = DateUtils.getDateByFormatter(date, formatter);
            System.out.println(localDate);
        }catch (Exception e){
            System.out.println("含时间 LocalDate转化失败");
        }
        /*不含时间*/
        date = "2018-01-01";
        formatter = "yyyy-MM-dd";
        LocalDateTime localDateTime = LocalDateTime.now();
        try {
            localDateTime = DateUtils.getDateTimeFormatterByFormatter(date, formatter);
            System.out.println(localDateTime);
        }catch (Exception e){
            System.out.println("不含时间 LocalDateTime转化失败");
        }
        /*含部分时间*/
        date = "2018-01-01 12";
        formatter = "yyyy-MM-dd HH";
        try {
            localDateTime = DateUtils.getDateTimeFormatterByFormatter(date, formatter);
            System.out.println(localDateTime);
        }catch (Exception e){
            System.out.println("LocalDateTime转化失败");
        }
        /*含时间*/
        date = "2018-01-01 12:01:01";
        formatter = "yyyy-MM-dd HH:mm:ss";
        try {
            localDateTime = DateUtils.getDateTimeFormatterByFormatter(date, formatter);
            System.out.println(localDateTime);
        }catch (Exception e){
            System.out.println("LocalDateTime转化失败");
        }
    }

    @Test
    public void testMilli(){
        long localdatetime = TimeUtil.getNowMilliSecond();
        long timestamp = System.currentTimeMillis();
        System.out.println(localdatetime);
        System.out.println(timestamp);
        timestamp += 10000000;
        Instant instant = Instant.ofEpochMilli(timestamp);
        long date = Duration.between(LocalDateTime.now(), LocalDateTime.ofInstant(instant, ZoneOffset.of("+8"))).toMillis();
        System.out.println("毫秒数 ： " + date);
    }

    @Test
    public void testLocaldatetime(){
        long milliSecond = TimeUtil.getNowMilliSecond();
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(1);
        System.out.println("毫秒数 ： " + (localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli() - milliSecond));
    }

    @Test
    public void LocalDateTimeIsTest(){
        LocalDateTime localDateTime = LocalDateTime.now();
        //时间增加一天
        LocalDateTime compare = localDateTime.plusDays(1);
        //比较大小
        System.out.println(localDateTime + " 比 " + compare + " 早 " + localDateTime.isBefore(compare));


    }


}
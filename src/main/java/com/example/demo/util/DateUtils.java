package com.example.demo.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * @author chen_bq
 * @description 时间日期工具类
 * @create: 2019-03-28 13:41
 **/
public class DateUtils {

    /**
     * 标准通过字符串，自定义formatter格式化
     * @param localDateTime
     * @param formatter
     * @return
     */
    public static String getDateTimeFormatterByString(LocalDateTime localDateTime, String formatter){
//        DateTimeFormatter[] formatters = new DateTimeFormatter[]{
//                // 直接使用常量创建DateTimeFormatter格式器
//                DateTimeFormatter.ISO_LOCAL_DATE,
//                DateTimeFormatter.ISO_LOCAL_TIME,
//                DateTimeFormatter.ISO_LOCAL_DATE_TIME,
//                // 使用本地化的不同风格来创建DateTimeFormatter格式器
//                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.MEDIUM),
//                DateTimeFormatter.ofLocalizedTime(FormatStyle.LONG),
//                // 根据模式字符串来创建DateTimeFormatter格式器
//                DateTimeFormatter.ofPattern("Gyyyy%%MMM%%dd HH:mm:ss")
//        };
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     * 通过字符串，自定义formatter反格式化
     * @param dateTime
     * @param formatter
     * @return
     */
    public static LocalDateTime getDateTimeFormatterByString(String dateTime, String formatter){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
        return LocalDateTime.parse(dateTime, dateTimeFormatter);
    }


}

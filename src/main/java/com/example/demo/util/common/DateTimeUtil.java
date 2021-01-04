package com.example.demo.util.common;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getNowTimeStr(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static String getTimeStr(Long timestamp){
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneOffset.of("+8")).toString();
    }

    public static String getTimeStrByDate(Date date){
        if (date == null){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        return sdf.format(date);
    }

    public static void main(String[] args) {
        Long timestamp = System.currentTimeMillis();
        System.out.println(getTimeStr(timestamp));
        System.out.println(getTimeStrByDate(new Date(timestamp)));
    }

}

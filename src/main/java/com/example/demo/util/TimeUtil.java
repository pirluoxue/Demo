package com.example.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author chen_bq
 * @description 时间相关的工具来
 * @create: 2019-03-06 14:09
 **/
public class TimeUtil {

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String BASE_UNIT_MONTH = "1";
    public static final String BASE_UNIT_WEEK = "2";
    public static final String BASE_UNIT_DAY = "3";

    @Deprecated
    public static String[] getSplitIntervalTimeByRegex(String intervalTime, String regex, String checkFormat, String standardFormat){
        String startFormat = standardFormat;
        String endFormat = standardFormat;
        if(checkFormat.indexOf("M") < 0 ){
            startFormat = startFormat.replaceAll("MM","1");
            endFormat = endFormat.replaceAll("MM","12");
        }
        if(checkFormat.indexOf("d") < 0 ){
            startFormat = startFormat.replaceAll("dd","1");
            endFormat =  endFormat.replaceAll("dd","31");
        }
        if(checkFormat.indexOf("H") < 0 ){
            startFormat = startFormat.replaceAll("HH","00");
            endFormat =  endFormat.replaceAll("HH","23");
        }
        if(checkFormat.indexOf("m") < 0 ){
            startFormat = startFormat.replaceAll("mm","00");
            endFormat = endFormat.replaceAll("mm","59");
        }
        if(checkFormat.indexOf("s") < 0 ){
            startFormat = startFormat.replaceAll("ss","00");
            endFormat = endFormat.replaceAll("ss","59");
        }
        SimpleDateFormat startSDF = new SimpleDateFormat(startFormat);
        SimpleDateFormat endSDF = new SimpleDateFormat(endFormat);
        String[] time = intervalTime.split(regex);
        String[] BETIme = new String[2];
        if(time != null){
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(checkFormat);
                String Begindate = startSDF.format(dateFormat.parse(time[0]));
                String Enddate = endSDF.format(dateFormat.parse(time[1]));
                BETIme[0] = Begindate;
                BETIme[1] = Enddate;
                return BETIme;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @Author chen_bq
     * @Description 根据正则表达式将时间区间拆分成格式化时间字符串数组
     * @Date 2019/3/28 14:53
     * @Param [intervalTime, regex]
     * @return java.lang.String[]
     **/
    @Deprecated
    public static String[] getSplitIntervalTimeByRegex(String intervalTime, String regex){
        return getSplitIntervalTimeByRegex(intervalTime, regex, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * @Author chen_bq
     * @Description 根据正则表达式拆分时间，再通过字符串格式化时间，将时间区间查分成时间字符串数组
     * @Date 2019/3/28 14:53
     * @Param [intervalTime, regex]
     * @return java.lang.String[]
     **/
    @Deprecated
    public static String[] getSplitIntervalTimeByRegex(String intervalTime, String regex, String checkFormat){
        return getSplitIntervalTimeByRegex(intervalTime, regex, checkFormat, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 推荐拆分日期区间方法
     * @param intervalTime
     * @param regex
     * @param inputFormatter
     * @param outputFormatter
     * @return
     */
    public static String[] splitIntervalDateByRegex(String intervalTime, String regex, String inputFormatter, String outputFormatter){
        String[] date = intervalTime.split(regex);
        if(date == null){
            throw new RuntimeException("拆分日期区间分割符异常。regex：" + regex);
        }
        DateTimeFormatter dtfInput = DateTimeFormatter.ofPattern(inputFormatter);
        DateTimeFormatter dtfOutput = DateTimeFormatter.ofPattern(outputFormatter);
        LocalDate localDate = LocalDate.parse(date[0], dtfInput);
        date[0] = localDate.format(dtfOutput);
        localDate = LocalDate.parse(date[1], dtfInput);
        date[1] = localDate.format(dtfOutput);
        return date;
    }

    public static String[] splitIntervalDateByRegex(String intervalTime, String regex, String inputFormatter){
        return splitIntervalDateByRegex(intervalTime, regex, inputFormatter, inputFormatter);
    }

    public static String[] splitIntervalDateByRegex(String intervalTime, String regex){
        return splitIntervalDateByRegex(intervalTime, regex, DATE_FORMAT, DATE_FORMAT);
    }

    /**
     * 获得用于统计的时间区间的扩大范围值
     * @param intervalDate
     * @param regex
     * @param inputFormatter
     * @param outputFormatter
     * @param baseUnit
     * @return
     */
    public static String[] getBeginEndDateByIntervalDate(String intervalDate, String regex, String inputFormatter, String outputFormatter, String baseUnit){
        String[] date = splitIntervalDateByRegex(intervalDate, regex, inputFormatter, outputFormatter);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(outputFormatter);
        LocalDate startDate = LocalDate.parse(date[0], dateTimeFormatter);
        LocalDate endDate = LocalDate.parse(date[1], dateTimeFormatter);
        //按日
        if(BASE_UNIT_DAY.equalsIgnoreCase(baseUnit)){
            return date;
        }
        //按周分割
        if(BASE_UNIT_WEEK.equalsIgnoreCase(baseUnit)){
            //当前周 第几日
            int dayOfWeek = startDate.getDayOfWeek().getValue();
            date[0] = startDate.minusDays(dayOfWeek - 1).format(dateTimeFormatter);
            //一周7天
            int weekDay = 7;
            dayOfWeek = endDate.getDayOfWeek().getValue();
            date[1] = endDate.minusDays(dayOfWeek - 1 - weekDay).format(dateTimeFormatter);
        }
        //按月
        if(BASE_UNIT_MONTH.equalsIgnoreCase(baseUnit)){
            //当前月 第几日
            int dayOfMonth = startDate.getDayOfMonth();
            date[0] = startDate.minusDays(dayOfMonth - 1).format(dateTimeFormatter);
            //获得该月天数
            int monthDay = startDate.lengthOfMonth();
            dayOfMonth = endDate.getDayOfMonth();
            date[1] = endDate.minusDays(dayOfMonth - 1 - monthDay).format(dateTimeFormatter);
        }
        return date;
    }

    public static String[] getBeginEndDateByIntervalDate(String intervalDate, String regex, String inputFormatter, String baseUnit){
        return getBeginEndDateByIntervalDate(intervalDate, regex, inputFormatter, inputFormatter, baseUnit);
    }

    public static String[] getBeginEndDateByIntervalDate(String intervalDate, String regex, String baseUnit){
        return getBeginEndDateByIntervalDate(intervalDate, regex, DATE_FORMAT, DATE_FORMAT, baseUnit);
    }

    /**
     * 通过字符串时间戳区间获得格式化的起止时间戳
     * @param intervalDate 字符串时间戳区间
     * @param regex 分割符
     * @param inputFormatter 格式化字符串
     * @param outputFormatter 输出格式化字符串
     * @param baseUnit 基本单位
     * @return
     */
    public static String[] getBeginEndDateTimeByIntervalDateTime(String intervalDate, String regex, String inputFormatter, String outputFormatter, String baseUnit){
        String[] date = splitIntervalDateTimeByRegex(intervalDate, regex, inputFormatter, outputFormatter);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(outputFormatter);
        LocalDateTime startDateTime = LocalDateTime.parse(date[0], dateTimeFormatter);
        LocalDateTime endDateTime = LocalDateTime.parse(date[1], dateTimeFormatter);
        return supplementBeginEndDateTime(startDateTime, endDateTime, dateTimeFormatter, baseUnit);
    }

    public static String[] getBeginEndDateTimeByIntervalDateTime(String intervalDate, String regex, String inputFormatter, String baseUnit){
        return getBeginEndDateTimeByIntervalDateTime(intervalDate, regex, inputFormatter, inputFormatter, baseUnit);
    }

    public static String[] getBeginEndDateTimeByIntervalDateTime(String intervalDate, String regex, String baseUnit){
        return getBeginEndDateTimeByIntervalDateTime(intervalDate, regex, DATETIME_FORMAT, DATETIME_FORMAT, baseUnit);
    }

    /**
     * 补充时间戳的时间部分
     * @param startDateTime
     * @param endDateTime
     * @param dateTimeFormatter
     * @param baseUnit
     * @return
     */
    private static String[] supplementBeginEndDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime, DateTimeFormatter dateTimeFormatter, String baseUnit){
        //按日
        if(BASE_UNIT_DAY.equalsIgnoreCase(baseUnit)){
            String start = emptyTime(startDateTime)
                    .format(dateTimeFormatter);
            String end = emptyTime(endDateTime)
                    .format(dateTimeFormatter);
            return new String[]{start, end};
        }
        //按周分割
        if(BASE_UNIT_WEEK.equalsIgnoreCase(baseUnit)){
            //当前周 第几日
            int dayOfWeek = startDateTime.getDayOfWeek().getValue();
            String start = emptyTime(startDateTime.minusDays(dayOfWeek - 1))
                    .format(dateTimeFormatter);
            //一周7天
            int weekDay = 7;
            dayOfWeek = endDateTime.getDayOfWeek().getValue();
            String end = emptyTime(endDateTime.minusDays(dayOfWeek - 1 - weekDay))
                    .format(dateTimeFormatter);
            return new String[]{start, end};
        }
        //按月
        if(BASE_UNIT_MONTH.equalsIgnoreCase(baseUnit)){
            //当前月 第几日
            int dayOfMonth = startDateTime.getDayOfMonth();
            String start = emptyTime(startDateTime.minusDays(dayOfMonth - 1))
                    .format(dateTimeFormatter);
            //获得该月天数
            int monthDay = startDateTime.toLocalDate().lengthOfMonth();
            dayOfMonth = endDateTime.getDayOfMonth();
            String end = emptyTime(endDateTime.minusDays(dayOfMonth - 1 - monthDay))
                    .format(dateTimeFormatter);
            return new String[]{start, end};
        }
        return new String[]{startDateTime.format(dateTimeFormatter), endDateTime.format(dateTimeFormatter)};
    }

    /**
     * 推荐拆分时间区间方法
     * @param intervalTime
     * @param regex
     * @param inputFormatter
     * @param outputFormatter
     * @return
     */
    public static String[] splitIntervalDateTimeByRegex(String intervalTime, String regex, String inputFormatter, String outputFormatter){
        String[] dateTime = intervalTime.split(regex);
        if(dateTime == null){
            throw new RuntimeException("拆分时间区间分割符异常。regex：" + regex);
        }
        DateTimeFormatter dtfInput = DateTimeFormatter.ofPattern(inputFormatter);
        DateTimeFormatter dtfOutput = DateTimeFormatter.ofPattern(outputFormatter);
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime[0], dtfInput);
        dateTime[0] = localDateTime.format(dtfOutput);
        localDateTime = LocalDateTime.parse(dateTime[1], dtfInput);
        dateTime[1] = localDateTime.format(dtfOutput);
        return dateTime;
    }

    public static String[] splitIntervalDateTimeByRegex(String intervalTime, String regex, String inputFormatter){
        return splitIntervalDateTimeByRegex(intervalTime, regex, inputFormatter, inputFormatter);
    }

    public static String[] splitIntervalDateTimeByRegex(String intervalTime, String regex){

        return splitIntervalDateTimeByRegex(intervalTime, regex, DATETIME_FORMAT, DATETIME_FORMAT);
    }

    /**
     * 清零时间部分
     * @param localDateTime
     * @return
     */
    private static LocalDateTime emptyTime(LocalDateTime localDateTime){
        return localDateTime
                .minusHours(localDateTime.getHour())
                .minusMinutes(localDateTime.getMinute())
                .minusSeconds(localDateTime.getSecond());
    }

    public static void main(String[] args) {
        String interval = "2019-03-12 - 2019-03-28";
        System.out.println(interval);
        String regex = " - ";
        String[] DateTime = splitIntervalDateByRegex(interval, regex);
        for (String dateTime: DateTime){
            System.out.println(dateTime);
        }
        DateTime = getBeginEndDateByIntervalDate(interval, regex, BASE_UNIT_DAY);
        for (String dateTime: DateTime){
            System.out.println(dateTime);
        }
        DateTime = splitIntervalDateByRegex(interval, regex);
        for (String dateTime: DateTime){
            System.out.println(dateTime);
        }
        DateTime = getBeginEndDateByIntervalDate(interval, regex, BASE_UNIT_WEEK);
        for (String dateTime: DateTime){
            System.out.println(dateTime);
        }
        DateTime = splitIntervalDateByRegex(interval, regex);
        for (String dateTime: DateTime){
            System.out.println(dateTime);
        }
        DateTime = getBeginEndDateByIntervalDate(interval, regex, BASE_UNIT_MONTH);
        for (String dateTime: DateTime){
            System.out.println(dateTime);
        }
        interval = "2019-03-12 12:16:18 - 2019-03-28 12:16:18";
        System.out.println(interval);
        DateTime = splitIntervalDateTimeByRegex(interval, regex);
        for (String dateTime: DateTime){
            System.out.println(dateTime);
        }
        DateTime = getBeginEndDateTimeByIntervalDateTime(interval, regex, BASE_UNIT_DAY);
        for (String dateTime: DateTime){
            System.out.println(dateTime);
        }
        DateTime = splitIntervalDateTimeByRegex(interval, regex);
        for (String dateTime: DateTime){
            System.out.println(dateTime);
        }
        DateTime = getBeginEndDateTimeByIntervalDateTime(interval, regex, BASE_UNIT_WEEK);
        for (String dateTime: DateTime){
            System.out.println(dateTime);
        }
        DateTime = splitIntervalDateTimeByRegex(interval, regex);
        for (String dateTime: DateTime){
            System.out.println(dateTime);
        }
        DateTime = getBeginEndDateTimeByIntervalDateTime(interval, regex, BASE_UNIT_MONTH);
        for (String dateTime: DateTime){
            System.out.println(dateTime);
        }
    }

    /**
     * 获得当前毫秒数
     * @return
     */
    public static long getNowMilliSecond(){
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }
    
    /**
     * @Author chen_bq
     * @Description 时间戳转localdatetime
     * @Date 2019/10/15 15:52
     * @Param [millis]
     * @return java.time.LocalDateTime
     */
    public static LocalDateTime millis2LocaldateTime(long millis){
        Instant instant = Instant.ofEpochMilli(millis);
        return LocalDateTime.ofInstant(instant, ZoneOffset.of("+8"));
    }

    public static LocalDateTime second2LocaldateTime(long millis){
        return LocalDateTime.ofEpochSecond(millis, 0, ZoneOffset.ofHours(8));
    }

}

package com.example.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chen_bq
 * @description 时间相关的工具来
 * @create: 2019-03-06 14:09
 **/
public class TimeUtil {

    public static String[] getSplitIntervalTimeByRegex(String intervalTime, String regex, String checkSDFStr, String standardSDFStr){
        String startSDFStr = standardSDFStr;
        String endSDFStr = standardSDFStr;
        if(checkSDFStr.indexOf("M") < 0 ){
            startSDFStr = startSDFStr.replaceAll("MM","1");
            endSDFStr = endSDFStr.replaceAll("MM","12");
        }
        if(checkSDFStr.indexOf("d") < 0 ){
            startSDFStr = startSDFStr.replaceAll("dd","1");
            endSDFStr =  endSDFStr.replaceAll("dd","31");
        }
        if(checkSDFStr.indexOf("H") < 0 ){
            startSDFStr = startSDFStr.replaceAll("HH","00");
            endSDFStr =  endSDFStr.replaceAll("HH","23");
        }
        if(checkSDFStr.indexOf("m") < 0 ){
            startSDFStr = startSDFStr.replaceAll("mm","00");
            endSDFStr = endSDFStr.replaceAll("mm","59");
        }
        if(checkSDFStr.indexOf("s") < 0 ){
            startSDFStr = startSDFStr.replaceAll("ss","00");
            endSDFStr = endSDFStr.replaceAll("ss","59");
        }
        SimpleDateFormat startSDF = new SimpleDateFormat(startSDFStr);
        SimpleDateFormat endSDF = new SimpleDateFormat(endSDFStr);
        String[] time = intervalTime.split(regex);
        String[] BETIme = new String[2];
        if(time != null){
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(checkSDFStr);
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

    public static String[] getSplitIntervalTimeByRegex(String intervalTime, String regex){
        return getSplitIntervalTimeByRegex(intervalTime, regex, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
    }

    public static String[] getSplitIntervalTimeByRegex(String intervalTime, String regex, String checkSDFStr){
        return getSplitIntervalTimeByRegex(intervalTime, regex, checkSDFStr, "yyyy-MM-dd HH:mm:ss");
    }



}

package com.example.demo.util;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chen_bq
 * @description
 * @create: 2019/11/13 10:55
 **/
public class TimeZoneTest {

    @Test
    public void test(){
        List<String> list = new ArrayList<>();
        list.add("20191110");
        list.add("20191111");
        list.add("20191112");
        list.add("20191113");
        list.add("20191112");
        System.out.println(list.stream().distinct().collect(Collectors.toList()));
    }

    @Test
    public void zeroSDFTest() {
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String time = sdf.format(new Date(timestamp));
        System.out.println(time);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        time = sdf.format(new Date(timestamp));
        System.out.println(time);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        time = sdf.format(new Date(timestamp));
        System.out.println(time);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+12"));
        time = sdf.format(new Date(timestamp));
        System.out.println(time);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-11"));
        time = sdf.format(new Date(timestamp));
        System.out.println(time);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-12"));
        time = sdf.format(new Date(timestamp));
        System.out.println(time);
    }

    @Test
    public void overDayTimeZoneTest() {
        Map<Integer, List<TimeZoneEntity>> map = getOverDayTimeZone();
        // 获取服务时间10点时，所有时区是否隔日
        List<TimeZoneEntity> list = map.get(10);
        List<Integer> yesterdayDayUtcCodeList = new ArrayList<>();
        List<Integer> dayUtcCodeList = new ArrayList<>();
        Iterator<TimeZoneEntity> it = list.iterator();
        while (it.hasNext()) {
            TimeZoneEntity timeZone = it.next();
            if (timeZone.getOverDay().equals(0)){
                dayUtcCodeList.add(timeZone.getUtcCode());
            }else{
                yesterdayDayUtcCodeList.add(timeZone.getUtcCode());
            }
        }
        System.out.println(yesterdayDayUtcCodeList);
        System.out.println(dayUtcCodeList);

        Map test = generateTimezoneMap();
        System.out.println(test);
    }

    public Map<Integer, TimeZoneEntity> generateTimezoneMap() {
        Map<Integer, TimeZoneEntity> map = new HashMap<>();
        int timezone = Calendar.getInstance().getTimeZone().getRawOffset() / (1000 * 3600);
        for(int i = 0; i < 24; i ++) {
            //当前时间与时区之和为系统时区
            int UTCCode = timezone - i;
            int overDay = 0;
            //时区小于－12时超出时区，加24
            if(UTCCode <= -12) {
                UTCCode += 24;
            }
            TimeZoneEntity entity = new TimeZoneEntity();
            entity.setUtcCode(UTCCode);
            //时区大于系统时区 聚合时天数＋1；
            if(UTCCode > timezone)
                ++ overDay;
            entity.setOverDay(overDay);
            //返回每小时应该聚合的时区数据
            map.put(i, entity);
        }
        return map;
    }

    private Map<Integer, List<TimeZoneEntity>> getOverDayTimeZone() {
        int thisTimeZone = Calendar.getInstance().getTimeZone().getRawOffset() / (1000 * 3600);
        Map<Integer, List<TimeZoneEntity>> map = new HashMap<>();
        for (int i = 0; i < 24; i++) {
            List<TimeZoneEntity> list = new ArrayList<>();
            for (int UTCCode = -11; UTCCode <= 12; UTCCode++) {
                // 零时区时间
                int zeroTime = i - thisTimeZone;
                // 零时区加时区为当地时间
                int utcTime = zeroTime + UTCCode;
                TimeZoneEntity timeZone = new TimeZoneEntity();
                timeZone.setUtcCode(UTCCode);
                // 当地时间为负，则为前一天
                if (utcTime < 0) {
                    timeZone.setOverDay(-1);
                } else {
                    timeZone.setOverDay(0);
                }
                list.add(timeZone);
            }
            // 返回每小时各个时区是否隔日
            map.put(i, list);
        }
        System.out.println(map);
        return map;
    }

    public class TimeZoneEntity {
        public Integer utcCode;
        public Integer overDay;

        public Integer getUtcCode() {
            return utcCode;
        }

        public void setUtcCode(Integer utcCode) {
            this.utcCode = utcCode;
        }

        public Integer getOverDay() {
            return overDay;
        }

        public void setOverDay(Integer overDay) {
            this.overDay = overDay;
        }
    }
}

package com.example.demo.simple;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.entity.jooq.tables.pojos.User;
import com.example.demo.model.entity.simple.MatchInfo;
import com.example.demo.service.thread.EasyLatchThread;
import com.example.demo.util.DateUtils;
import com.example.demo.util.common.HttpClientUtils;
import com.example.demo.util.common.ReflectUtils;
import com.example.demo.util.io.EncoderUtils;
import com.example.demo.util.io.IoStreamUtils;
import com.example.demo.util.io.PoiUtils;
import com.google.common.base.Strings;
import org.apache.calcite.util.ReflectUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.bson.Document;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author chen_bq
 * @description
 * @create: 2019/11/21 9:34
 **/
public class MainTest {

    @Test
    public void Matchertest() {
        String patther = "(\"[0-9a-zA-Z_]+\":)([0-9]{13})([,}])";
        Pattern pattern = Pattern.compile(patther);
        String str = "\"2a\":1111111111111,";
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        if (matcher.find()) {
            System.out.println(matcher.group());
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
            System.out.println(matcher.group(3));
            try {
                System.out.println(matcher.group(4));
            } catch (Exception e) {
                System.out.println("cannot group 4");
            }
            matcher.appendReplacement(sb, "\"2a\":1111111111111,\"2a\":1111111111111,");
        }
        matcher.appendTail(sb);
        System.out.println(sb.toString());
    }

    @Test
    public void jsonTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date", new Date());
        String json = jsonObject.toJSONString();
        System.out.println(json);
    }

    @Test
    public void latchThreadTest() throws InterruptedException {
        EasyLatchThread latchRunnables = new EasyLatchThread() {
            @Override
            public CountDownLatch getLatch() {
                return latch;
            }

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    System.out.println(1);
                    Thread.sleep(1000);
                    System.out.println(2);
                    Thread.sleep(1000);
                    System.out.println(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
                Thread.interrupted();
            }
        };
        Thread thread = new Thread(latchRunnables);
        thread.run();
        latchRunnables.getLatch().await();
    }


    @Test
    public void encoderTest() throws IOException {
        File file = new File("E:\\backgroundImage\\e334bddca18bdcda37a63395615a1408.jpg");
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(file);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileBase64 = Base64.getEncoder().encodeToString(data);
        System.out.println("data:image/jpeg;base64," + fileBase64);
        String str = "喵喵喵？？？";
//        String stringBase64 = Base64.getEncoder().encodeToString(str.getBytes());
//        System.out.println(stringBase64);
//        byte[] decodeFile = Base64.getDecoder().decode(fileBase64.getBytes());
//        OutputStream outputStream = new FileOutputStream("E:\\backgroundImage\\1.jpg");
//        outputStream.write(decodeFile);
//        outputStream.flush();
//        outputStream.close();

        EncoderUtils.writeImageByBase64("E:\\backgroundImage\\1.jpg", fileBase64);
        EncoderUtils.compressImage(new File("E:\\backgroundImage\\1.jpg")
            , new File("E:\\backgroundImage\\2.jpg"), 300, 150);
    }

    @Test
    public void differentCodeLengthTest() {
        String str1 = "asd";
        String str2 = "阿萨德";
        System.out.println(str1.length() == str2.length());
        // 本地环境默认是utf-8
        System.out.println(str1.getBytes().length == str2.getBytes().length);
        System.out.println(str1.getBytes(Consts.UTF_8).length == str2.getBytes(Consts.UTF_8).length);
        System.out.println(str1.getBytes(Consts.ASCII).length == str2.getBytes(Consts.ASCII).length);
    }

    @Test
    public void randomTest() {
        Random r = new Random();
        System.out.println(r.nextInt(123));
    }

    @Test
    public void structTextTest() {
        String collectionName = "apExperienceLevel";
        for (int i = 20191209; i <= 20191217; i++) {
            String jsonName = collectionName + i;
//            String str = "mongoimport -u macc -p ma0c#2017#rj -d macc2 -c " + jsonName + " -h 172.18.24.127:27017 --file \"e:\\test\\cloudtest\\" + jsonName + ".json\" --type=json --upsert -j 100 --writeConcern=0";
//            String str = "db." + jsonName + ".drop()";
//            String str = "mongoimport -u macc -p ma0c#2017#rj -d macc2 -c " + jsonName + " -h 127.0.0.1:27017 --file \"/home/Jack/test/cloudtest/" + jsonName + ".json\" --type=json --upsert -j 100 --writeConcern=0";
            String str = "db." + jsonName + ".ensureIndex({\"storageTimestamp\":1},{background:true})\n" +
                "db." + jsonName + ".ensureIndex({buildingId:1, storageTimestamp:1}, {background:true})\n" +
                "db." + jsonName + ".createIndex({\"uploadTime\":1},{expireAfterSeconds:691200, background:true})";
            System.out.println(str);
        }
    }

    /**
     * @return void
     * @Author chen_bq
     * @Description 使用list的stream进行自定义去重
     * @Date 2019/12/17 14:22
     */
    @Test
    public void listStreamCustomerDistinctTest() {
        User user1 = new User();
        user1.setUserId(1);
        user1.setUserName("test1");
        User user2 = new User();
        user2.setUserId(2);
        user2.setUserName("test1");
        User user3 = new User();
        user3.setUserId(1);
        user3.setUserName("test2");
        User user4 = new User();
        user4.setUserId(1);
        user4.setUserName("test2");
        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        list.add(user3);
        List distinctList = list.stream().collect(
            Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<User>(Comparator.comparing(n -> n.getUserId()))),
//                ArrayList::new)
                p -> (new ArrayList<>(p)))
        );
        System.out.println(distinctList);
        // 多字段去重
        List distinctClass = list.stream().collect(
            Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(n ->
                    n.getUserId() + ";" + n.getUserName()))), ArrayList::new));
        System.out.println(distinctClass);
    }

    /**
     * @Author chen_bq
     * @Description 使用List stream转成Map
     * @Date 2020/4/30 13:46
     * @return void
     */
    @Test
    public void list2Map() {
        List<User> list = new LinkedList<>();
        Map<String, User> dbFitApMap = list.stream().collect(Collectors.toMap(User::getUserKey, info -> info));
        System.out.println(dbFitApMap);
        System.out.println(dbFitApMap.containsKey(-1));
    }

    @Test
    public void listRemoveTest() {
        List<Integer> list = new ArrayList();
        list.add(1);
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(3);
        System.out.println(list.stream().limit(10));
        List<Integer> output = new ArrayList();
        while (true) {
            if (list == null || list.size() <= 0) {
                break;
            }
            Integer a = list.get(0);
            list.remove(0);
            if (list.size() <= 0) {
                output.add(a);
                break;
            }
            Iterator<Integer> it = list.iterator();
            while (it.hasNext()) {
                if (a.compareTo(it.next()) == 0) {
                    output.add(a + 100);
                    it.remove();
                    break;
                }
            }
        }
        System.out.println(output);
    }

    @Test
    public void antPathMatcherTest() {
        System.out.println(new AntPathMatcher().match("http://*", "http://123"));
        System.out.println(new AntPathMatcher().match("^(https|imaps|http)://.*", "http://172.18.24"));
    }

    private char[] getLongToChar(long times) {
        if (times <= 0) {
            return new char[0];
        }
        int AsciiLength = 256;
        long length = times / AsciiLength;
        if (times % AsciiLength == 0 || times < AsciiLength) {
            length++;
        }
        char[] chars = new char[(int)length];
        for (int i = (int)length - 1; i >= 0; i--) {
            if (times == 255) {
                chars[i] = (char)255;
            } else {
                chars[i] = (times - AsciiLength) >= 0 ? (char)255 : (char)times;
            }
            if ((times - AsciiLength) < 0) {
                break;
            }
            times -= AsciiLength;
        }
        return chars;
    }

    @Test
    public void singleRegexTest() {
        Pattern pattern = Pattern.compile("(.+)[_-]{1}([0-9-].+)");
        Matcher matcher = pattern.matcher("ap_log-20201212123123123-Gj123123123123.zip");
        if (!matcher.find() || matcher.groupCount() < 1) {
            System.out.println("匹配不到");
        } else {
            System.out.println(matcher.group(1));
        }
        int i = 10;
        System.out.println(i / 23);
    }

    @Test
    public void test14() {
        String fileName = "C:\\Users\\bangqiang chen\\Desktop\\临时文件\\客户mongo数据.json";
        String output = "C:\\Users\\bangqiang chen\\Desktop\\临时文件\\test";
        List<String> originList = IoStreamUtils.getListByFileReadLine(new File(fileName));
        if (originList.size() > 2000) {
            for (int i = 0; i < originList.size() / 2000 + 1; i++) {
                int endIndex = 2000 * (i + 1);
                if (endIndex > originList.size()) {
                    endIndex = originList.size();
                }
                List<String> tmp = originList.subList(2000 * i, endIndex);
                StringBuilder sb = new StringBuilder();
                for (String str : originList) {
                    Document document = Document.parse(str);
                    sb.append(document.get("mac").toString() + " ");
                }
                IoStreamUtils.writeTextIntoFile(sb.toString(), output + i);
            }
        }
    }

    @Test
    public void test13() throws InterruptedException {
        String fullFileName = "E:\\test\\output";
//        String fileName = "C:\\Users\\bangqiang chen\\Desktop\\临时文件\\test.txt";
        String fileName = "C:\\Users\\bangqiang chen\\Desktop\\临时文件\\客户mongo数据.json";
        List<String> originList = IoStreamUtils.getListByFileReadLine(new File(fileName));
        if (originList == null || originList.size() <= 0) {
            return;
        }
        List<List<String>> excelList = new ArrayList<>();
        List title = init();
        excelList.add(title);
        for (String str : originList) {
            Document document = Document.parse(str);
            List list = getListForDocument(document);
            excelList.add(list);
        }
        List<EasyLatchThread> threads = new ArrayList<>();
        if (excelList.size() / 30000 > 0) {
            for (int i = 0; i < excelList.size() / 30000 + 1; i++) {
                // 临时取消最后一个
                if (i == excelList.size() / 30000) {
                    break;
                }
                int endIndex = 30000 * (i + 1);
                if (endIndex > originList.size()) {
                    endIndex = originList.size();
                }

                int finalStartIndex = i;
                int finalEndIndex = endIndex;
                EasyLatchThread easyLatchThread = new EasyLatchThread() {
                    @Override
                    public CountDownLatch getLatch() {
                        return this.latch;
                    }

                    @Override
                    public void run() {
                        List<List<String>> tmp = excelList.subList(30000 * finalStartIndex, finalEndIndex);
                        PoiUtils.exportExcel(tmp, fullFileName + finalStartIndex);
                        System.out.println(30000 * finalStartIndex + " ~ " + finalEndIndex + " completed");
                        latch.countDown();
                    }
                };
                threads.add(easyLatchThread);
                Thread thread = new Thread(easyLatchThread);
                thread.start();
            }
        } else {
            PoiUtils.exportExcel(excelList, fullFileName);
        }
        while (true) {
            if (threads == null || threads.size() <= 0) {
                break;
            }
            for (EasyLatchThread easyLatchThread : threads) {
                easyLatchThread.getLatch().await();
                if (easyLatchThread.getLatch().getCount() == 0) {
                    threads.remove(easyLatchThread);
                    break;
                }
            }
        }
    }

    private List getListForDocument(Document document) {
        if (document == null) {
            return new ArrayList();
        }
        List list = new ArrayList();
        list.add(document.get("mac"));
        list.add(document.get("uploadDate"));
        list.add(document.get("onlineTime"));
        list.add(document.get("sn"));
        list.add(document.get("updateTime"));
        list.add(document.get("offlineTime"));
        list.add(document.get("userIp"));
        list.add(document.get("ssid"));
        list.add(document.get("wifiUp"));
        list.add(document.get("wifiDown"));
        list.add(document.get("wifiUpDown"));
        list.add(document.get("buildingName"));
        list.add(document.get("deviceName"));
        list.add(document.get("deviceAliasName"));
        list.add(document.get("activeTime"));
        list.add(document.get("band"));
        list.add(document.get("productType"));
        return list;
    }

    private List init() {
        List list = new ArrayList();
        list.add("mac");
        list.add("uploadDate");
        list.add("onlineTime");
        list.add("sn");
        list.add("updateTime");
        list.add("offlineTime");
        list.add("userIp");
        list.add("ssid");
        list.add("wifiUp");
        list.add("wifiDown");
        list.add("wifiUpDown");
        list.add("buildingName");
        list.add("groupName");
        list.add("deviceName");
        list.add("deviceAliasName");
        list.add("activeTime");
        list.add("band");
        list.add("productType");
        return list;
    }

    @Test
    public void downloadTest() {
        File file = new File("E:\\test\\image.jpg");
        String downloadUrl = "http://image.zzd.sm.cn/1560454273033615458.jpg";
        HttpClientUtils.download(downloadUrl, file);
    }

    @Test
    public void testMaxExcelRow() {
        List<List<String>> list = new ArrayList();
        for (int i = 0; i < 1000000; i++) {
            List<String> tmp = new ArrayList<>();
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            tmp.add("test 123 ");
            list.add(tmp);
        }
        long beginTime = System.currentTimeMillis();
        PoiUtils.exportExcelForLarge(list, "E:\\test\\test");
        System.out.println("completed " + Duration.ofMillis(System.currentTimeMillis() - beginTime));
    }

    @Test
    public void lockTest() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        CountDownLatch latch = new CountDownLatch(1);
        new Thread() {
            @Override
            public void run() {
                lock.lock();//请求锁
                try {
                    System.out.println(Thread.currentThread().getName() + "==》进入等待");
                    condition.await();//设置当前线程进入等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();//释放锁
                }
                System.out.println(Thread.currentThread().getName() + "==》继续执行");
                latch.countDown();
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                lock.lock();//请求锁
                try {
                    System.out.println(Thread.currentThread().getName() + "=》进入");
                    Thread.sleep(2000);//休息2秒
                    condition.signal();//随机唤醒等待队列中的一个线程
                    System.out.println(Thread.currentThread().getName() + "休息结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();//释放锁
                }
            }
        }.start();
        latch.await();
    }

    @Test
    public void test10() {
        String str = null;
        if (!StringUtils.isEmpty(str) && str.matches("1")) {
            System.out.println("1");
        }
        String a = "China";
        System.out.println(StringUtils.upperCase(a));
    }

    @Test
    public void testList() {
        MatchInfo a = new MatchInfo();
        a.setMatchMsg("123");
        List<MatchInfo> list = new ArrayList();
        list.add(a);
        List<MatchInfo> list2 = new ArrayList();
        if (true) {
            MatchInfo tmp = list.get(0);
            tmp.setMatchTitle("223");
            list2.add(tmp);
            list.remove(0);
        }
        System.out.println(list2);
    }

    @Test
    public void test16() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        System.out.println(calendar.getTime());
    }

    @Test
    public void test17() {
        String str = "source /macc/install/tomcat/tmp/";
        File file = new File("E:\\test\\sql-proplan\\");
        if (file.isDirectory()) {
            List<File> files = Arrays.asList(file.listFiles());
            if (files != null && files.size() > 0) {
                for (File findFile : files) {
                    System.out.println(str + findFile.getName());
                }
            }
        }
    }

    @Test
    public void test18() {
        int result = TimeZone.getTimeZone("Pacific/Chatham").getRawOffset() / 3600_000;
        System.out.println(TimeZone.getTimeZone("Pacific/Chatham").getRawOffset() % 3600_000 > 0 ? ++result : result);
        for (String str : TimeZone.getAvailableIDs()) {
            if (getUtcCode(str) > 12 || getUtcCode(str) < -11) {
                System.out.println(str + " - - - UtcCode  " + getUtcCode(str));
            }
        }
    }

    @Test
    public void test19() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MatchInfo matchInfo = new MatchInfo();
        matchInfo.setMatchTitle("1");
        matchInfo.setMatchMsg("2");
        matchInfo.setMatchResource("3");
        matchInfo.setMatchCode(4);
        Field[] fields = MatchInfo.class.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
                String name = field.getName();
                String firstLetter = name.substring(0, 1).toUpperCase();
                String getter = "get" + firstLetter + name.substring(1);
                Method method = matchInfo.getClass().getMethod(getter, new Class[]{});
                Object value = method.invoke(matchInfo, new Object[]{});
                if (value != null) {
                    System.out.println(name + "    -    " + value.toString());
                } else {
                    System.out.println(name + "    -    " + null);
                }
            }
        }

    }

    private int getUtcCode(String timezoneId) {
        int result = TimeZone.getTimeZone(timezoneId).getRawOffset() / 3600_000;
        return TimeZone.getTimeZone(timezoneId).getRawOffset() % 3600_000 > 0 ? ++result : result;
    }

    @Test
    public void test111() {
        List<String> list = new ArrayList<>();
        list.add("1");
        String a = list.stream().filter(n -> n.equalsIgnoreCase("2")).collect(Collectors.joining(","));
        System.out.println(Strings.isNullOrEmpty(a));
    }

    @Test
    public void test112() {
        final String MAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        final Pattern MAIL_PATTERN = Pattern.compile(MAIL_REGEX);
        System.out.println("test123".equalsIgnoreCase("test"));
        List<User> list = new ArrayList<>();
        List<User> list2 = list.stream().filter(n -> n.getUserId() == 0).collect(Collectors.toList());
        System.out.println(" - - - - - - - - - - ");
    }

    @Test
    public void test113() {
        List<String> list = new ArrayList<>();
        for (String str : list) {
            System.out.println(str);
        }
        System.out.println(" - - - - ");
        String str = "asd {} asd {}";
        final String tag = "\\{\\}";
        Pattern pattern = Pattern.compile(tag);
        Matcher matcher = pattern.matcher(str);
        int i = 1;
        while (matcher.find()) {
            str = str.replaceFirst(tag, i++ + "");
            matcher = pattern.matcher(str);
        }
        System.out.println(str);
    }

    @Test
    public void test114() throws ParseException {
        String str = " int";
        System.out.println("int".equalsIgnoreCase(str.trim()));
        LocalDateTime end = LocalDate.now().atTime(0, 0, 0);
        System.out.println(end.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        Date endTime = Date.from(end.atZone(ZoneId.of("Antarctica/Vostok")).toInstant());
        System.out.println(endTime.getTime());

        Instant instant = Instant.ofEpochMilli(1584596346990L);
        LocalDate indexDay = LocalDate.ofInstant(instant, ZoneOffset.of("+8"));
        Instant instant2 = Instant.ofEpochMilli(1585201146990L);
        LocalDate endDay = LocalDate.ofInstant(instant2, ZoneOffset.of("+8"));


        System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
        while (indexDay.isBefore(endDay)) {
            System.out.println(indexDay);
            indexDay = indexDay.plusDays(1);
        }
        System.out.println(getUtcCode("Europe/Sofia"));
        String east6 = "2020-03-26 10:00:0012312312312313123";
        String sdf = "yyyy-MM-dd HH:mm:ss";
        east6 = east6.substring(0, sdf.length());
        LocalDateTime localDateTime = DateUtils.getDateTimeFormatterByFormatter(east6, sdf);
        System.out.println("东6区的时间 " + east6 + "  时间戳为 " + localDateTime.toInstant(ZoneOffset.of("+" + getUtcCode("Europe/Sofia"))).toEpochMilli());
        System.out.println("转化成localdatetime " + localDateTime);
        String east8 = "2020-03-26 16:00:00";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
        Date date = simpleDateFormat.parse(east8);
        System.out.println("东8区的时间 " + east8 + "  时间戳为 " + date.getTime());
        System.out.println("东8区的Date " + date);
        String east2 = "20200402084341";
        Long time = getString2Time(east2, "yyyyMMddHHmmss");
        System.out.println(new Timestamp(time));
    }

    public static Long getString2Time(String timeString, String formatter) {
        Long timestamp = System.currentTimeMillis();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
        try {
            LocalDateTime antiFormatter = LocalDateTime.parse(timeString, dateTimeFormatter);
            int utcCode = 2;
            // 转化为时区String
            String offsetId = "";
            if (utcCode >= 0) {
                offsetId = "+" + utcCode;
            } else {
                offsetId = "" + utcCode;
            }
            timestamp = antiFormatter.toInstant(ZoneOffset.of(offsetId)).toEpochMilli();
        } catch (Exception e) {
        }

        return timestamp;
    }

    /**
     * @Author chen_bq
     * @Description 可变参数测试
     * @Date 2020/4/30 13:50
     * @return void
     */
    @Test
    public void variableArityMethodsTest() {
        System.out.println(variableArityMethod(1, null));
        System.out.println(variableArityMethod(null, 1));
        System.out.println(variableArityMethod(null, null));
        System.out.println(variableArityMethod(1, 1));
        System.out.println(variableArityMethod(null));
    }

    private Integer variableArityMethod(Integer... integers){
        Integer out = 0;
        if (integers == null){
            return 0;
        }
        for (Integer i : integers){
            if (i == null){
                continue;
            }
            out += i;
        }
        return out;
    }

    @Test
    public void newEntityTest(){
        List<User> list = new LinkedList<>();
        for (int i = 0 ; i < 2 ; i ++){
            User user = new User();
            user.setUserName("test" + i);
            list.add(user);
        }
        Map<String, User> map = new HashMap();
        for (User u : list){
            map.put(u.getUserName(), u);
        }
        for (User u : list){
            map.get(u.getUserName()).setUserName("TTT");
        }
        System.out.println(map);
        System.out.println(list);
    }

    /**
     * @Author chen_bq
     * @Description 这是一个关于unmodifiableList方法的一个测试类
     * unmodifiableList方法返回的是一个不可变的List，但是实际上不可变的仅仅是增删操作，对象的引用是可以改变的
     * @Date 2020/5/8 17:40
     */
    @Test
    public void unmodifiableListTest(){
        List<User> list = new ArrayList<>();
        for (int i = 0 ; i < 2 ; i ++){
            User user = new User();
            user.setUserName("test" + i);
            list.add(user);
        }
        System.out.println(list);
        List<User> tmp = list;
        List<User> unmodifiableList = Collections.unmodifiableList(list);
        System.out.println(tmp.get(0));
        tmp.get(0).setUserName("boom!");
        System.out.println(list.get(0));
        System.out.println(tmp.get(0));
        System.out.println(unmodifiableList.get(0));
        unmodifiableList.get(0).setUserName("boom???");
        // 实际上对象引用还是可以被修改的
        System.out.println(unmodifiableList.get(0));
        System.out.println(list.get(0));
        // 但是依然不允许增删操作
        unmodifiableList.add(new User());
        List<String> strList = new ArrayList<>();
        strList.add("test1");
        List<String> strListUnmod = Collections.unmodifiableList(strList);
        System.out.println(strListUnmod.get(0));
        strListUnmod.add("test2");
    }

    @Test
    public void test(){
        String str = "2020-05-07 08:00:00";
        System.out.println("TimeZone的默认时区 " + TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis()));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        System.out.println("DateTimeFormat的时间 " + dtf.parse(str));
        System.out.println("---------------------------");
        List<User> list = new ArrayList<>();
        User user1 = new User();
        user1.setUserName("test1");
        User user2 = new User();
        user2.setUserName("test2");
        User user3 = new User();
        user3.setUserName("test2");
        list.add(user1);
        list.add(user2);
        String a = list.stream().map(n -> n.getUserName()).distinct().collect(Collectors.joining(";"));
        System.out.println(a);
    }

    @Test
    public void reduceTest(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        int reduce = list.stream().reduce((p1, p2) -> p1 + p2).get();
        System.out.println(reduce);
        // List stream的map可以某种意义上可以理解为fareach
        List<Integer> tmp = list.stream().map(n -> {
            if (n < 3){
                return 0;
            }else {
                return 1;
            }
        }).collect(Collectors.toList());
        System.out.println(tmp);
    }

    private void printMatch(Matcher matcher) {
        System.out.println(matcher.find() ? matcher.group() : null);
    }

    @Test
    public void useTMethodTest(){
        User user = new User();
        user.setUserName("mie~");
        useTMethod(user, User.class);
    }

    @Test
    public void test2(){
        Map map = new HashMap();
        map.put("userName", "test");
        Object o = map;
        User user = JSON.parseObject(JSON.toJSONString(o), User.class);
        System.out.println(user.getUserName());
        LocalDateTime localDateTime = LocalDateTime.now();
//        localDateTime.toInstant(ZoneOffset.of("+1"));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = localDateTime.toInstant(ZoneOffset.of("+1")).toString();
        System.out.println(localDateTime);
        System.out.println(time);
        System.out.println(dateTimeFormatter.getZone());
        System.out.println(localDateTime.format(dateTimeFormatter));
        localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()),
                ZoneOffset.of("+1"));
        time = localDateTime.format(dateTimeFormatter);
        System.out.println(time);
        System.out.println(localDateTime.getHour());
        String str = "[{\"port\":\"0\", \"desc\":\"LAN 0\",\"type\":\"Gi\"},{\"port\":\"1\", \"desc\":\"LAN1/WAN3\",\"type\":\"Gi\"},{\"port\":\"2\", \"desc\":\"LAN2/WAN2\",\"type\":\"Gi\"},{\"port\":\"3\", \"desc\":\"LAN3/WAN1\",\"type\":\"Gi\"},{\"port\":\"4\", \"desc\":\"WAN0\",\"type\":\"Gi\"}]";
    }

    @Test
    public void test123(){
        String str = "createTime";
        System.out.println(str.replaceAll("([A-Z])", "_$1").toLowerCase());

    }

    @Test
    public void test3(){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()),
                ZoneOffset.of("+1"));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = localDateTime.format(dateTimeFormatter);
    }

    private <T> void useTMethod(T entity, Class<T> clazz){
        if (User.class.equals(clazz)){
            User user = (User) entity;
            System.out.println(user.getUserName());
        }
    }

    @Test
    public void escapeJavaTest(){
        String str = "\"asd.asd.ada\"";
        System.out.println(str);
        str = StringEscapeUtils.escapeJava(str);
        System.out.println(str);
        str = StringEscapeUtils.escapeJava(str);
        System.out.println(str);
        str = StringEscapeUtils.escapeJava(str);
        System.out.println(str);
    }

    @Test
    public void testUtils() throws Exception {
        List list = new ArrayList();
        System.out.println(CollectionUtils.isEmpty(list));
        list = null;
        System.out.println(CollectionUtils.isEmpty(list));
        User user = new User();
        ReflectUtils.setValudByFiled("userName", user, "test");
        System.out.println(ReflectUtils.getValueByFiled("userName", user));
    }

    @Test
    public void test1(){
        String path = "D:\\SQL\\Result_6.json";
        String file = IoStreamUtils.getStringByFile(new File(path));
        JSONArray array = JSONArray.parseArray(file);
        List<String> list = new ArrayList<>();
        for (Object o : array){
            if (o == null){
                continue;
            }
            JSONObject json = (JSONObject) o;
            Integer report_id = json.getInteger("report_id");
            String property = json.getString("property");
            String property_alias = json.getString("property_alias");
            Integer support_query = json.getInteger("support_query");
            String type = json.getString("type");
            String operator = json.getString("operator");
            String value_range = json.getString("value_range");
            value_range = StringEscapeUtils.escapeJava(value_range);
            Integer column_order = json.getInteger("column_order");
            String res_value = json.getString("res_value");
            String enumName = property_alias.toUpperCase().replaceAll("\\.", "_");
            String str = enumName + "(" + report_id + ", \"" + property + "\", \"" + property_alias + "\"," + support_query
                    + ", \"" + type + "\", \"" + operator + "\", \"" + value_range + "\"," + column_order
                    + ", \"" + res_value + "\"),";
            list.add(str);
        }
        for (String str : list){
            System.out.println(str);
        }
    }

        private String id = "id";
        private String cse_user_id = "cse_user_id";
        private String vad_user_id = "vad_user_id";
        private String vad_company = "vad_company";
        private String vad_country = "vad_country";
        private String cse_name = "cse_name";
        private String remark = "remark";
        private String vad_company_code = "vad_company_code";


}

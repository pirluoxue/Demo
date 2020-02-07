package com.example.demo.simple;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.entity.jooq.tables.pojos.User;
import com.example.demo.service.thread.EasyLatchThread;
import com.example.demo.util.io.EncoderUtils;
import com.example.demo.util.io.IoStreamUtils;
import com.example.demo.util.io.PoiUtils;
import com.google.common.base.Strings;
import org.apache.http.Consts;
import org.bson.Document;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
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
    public void test() {
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
        User user2 = new User();
        user2.setUserId(1);
        User user3 = new User();
        user3.setUserId(1);
        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        list.add(user3);
        list = list.stream().collect(
            Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<User>(Comparator.comparing(n -> n.getUserId()))),
//                ArrayList::new)
                p -> (new ArrayList<>(p)))
        );
        System.out.println(list);
    }

    @Test
    public void test3() {
        List<User> list = new LinkedList<>();
        Map<String, User> dbFitApMap = list.stream().collect(Collectors.toMap(User::getUserKey, info -> info));
        System.out.println(dbFitApMap);
    }

    @Test
    public void test4() {
        List<Integer> list = new ArrayList();
        list.add(1);
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(3);
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
    public void test5() {
        Long a = null;
        Long b = 1L;
        Long test = a == null ? 0 : a;
        test += b == null ? 0 : b;
        System.out.println(test);
    }

    @Test
    public void test7() {
        System.out.println(new AntPathMatcher().match("http://*", "http://123"));
        System.out.println(new AntPathMatcher().match("^(https|imaps|http)://.*", "http://172.18.24"));
    }

    @Test
    public void test8() {
        Map map = new HashMap();
        map.put("test", 1);
        map.replace("test", 2);
        System.out.println(map);
    }

    @Test
    public void test9() {
        Pattern FILE_PATTERN = Pattern.compile("(.+)[_-]{1}([0-9-].+)");
        String str = "current-20200103001537.log";
        Matcher matcher = FILE_PATTERN.matcher(str);
        if (!matcher.find() || matcher.groupCount() < 1) {
            System.out.println("没有");
        } else {
            System.out.println(matcher.group(1));
        }
    }

    @Test
    public void test11() {
        for (int i = 0; i < 100; i++) {
            System.out.println(getLongToChar(i));
        }
//        // TODO Auto-generated method stub
//        for (int i = 0; i < 255; i++) {
//            char a = (char)i;
//            System.out.println(a + " " + i);
//        }
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
    public void test12() {
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
                if (i == excelList.size() / 30000){
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
        while (true){
            if (threads == null || threads.size() <= 0){
                break;
            }
            for (EasyLatchThread easyLatchThread : threads) {
                easyLatchThread.getLatch().await();
                if (easyLatchThread.getLatch().getCount() == 0){
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
    public void test15(){
        List<String> list = new ArrayList();
        list.add("1");
        list.add(null);
        list.add("");
        list.add("1234");
        for (String s : list){
            if (!Strings.isNullOrEmpty(s) && s.length() > 3){
                System.out.println("boom");
            }
        }
        int a = 444;
        int b = 600;
        System.out.println(a * 100 / b);
    }

    @Test
    public void test6(){
        File file = new File("E:\\test\\image.jpg");
        String downloadUrl = "http://image.zzd.sm.cn/1560454273033615458.jpg";
        downloadZip(downloadUrl, file);
    }

    private static void downloadZip(String downloadUrl, File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            URL url = new URL(downloadUrl);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            int length = 0;
            byte[] bytes = new byte[1024];
            while ((length = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, length);
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
        }
        System.out.println("end");
    }

    @Test
    public void testMaxExcelRow(){
        List<List<String>> list = new ArrayList();
        for (int i = 0 ;i < 1000000; i++){
            List<String> tmp = new ArrayList<>();
            tmp.add("1");
            list.add(tmp);
        }
        long beginTime = System.currentTimeMillis();
        PoiUtils.exportExcelForLarge(list, "E:\\test\\test");
        System.out.println("completed " + Duration.ofMillis(System.currentTimeMillis() - beginTime));
    }

}

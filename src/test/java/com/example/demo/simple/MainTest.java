package com.example.demo.simple;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.collection.DataResourceEnum;
import com.example.demo.service.thread.EasyLatchThread;
import com.example.demo.util.ConnectionUtil;
import com.example.demo.util.JooqUtil;
import com.example.demo.util.io.EncoderUtils;
import com.example.demo.util.io.IoStreamUtils;
import com.google.common.base.Strings;
import org.apache.http.Consts;
import org.jooq.DSLContext;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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



}

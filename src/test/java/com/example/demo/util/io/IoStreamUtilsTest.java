package com.example.demo.util.io;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.entity.jooq.tables.pojos.User;
import com.mongodb.client.model.IndexOptions;
import lombok.Data;
import org.bson.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IoStreamUtilsTest {

    @Test
    public void writeLogTest() throws IOException {
        String path = "E:\\work\\工作文档\\test.txt";
        String tmpName = path.replaceAll("(\\..*)", 1 + "$1");
        System.out.println(tmpName);
        File file = new File(path);
        file.renameTo(new File(tmpName));
        file = new File(path);
        file.delete();
        System.out.println(file.getName());
        System.out.println(file.length());
        System.out.println(file.length() / 1024L / 1024L);
        System.out.println(file.getName());
        System.out.println(file.getAbsolutePath());
        file = new File(file.getAbsolutePath());
    }


    private static final Logger logger = LoggerFactory.getLogger(IoStreamUtilsTest.class);

    @Test
    public void fastWriteTextTest() throws InterruptedException {
        String path = "E:\\work\\工作文档\\test.txt";
        long beginTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(10);
//        IoStreamUtils.test(path);
        for (int i = 0; i < 10; i++) {
            Executor executor = new Executor() {
                @Override
                public void execute(Runnable command) {
                    command.run();
                }
            };
            CountDownLatch customerLatch = latch;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        String str = "Line " + (i + 1);
                        IoStreamUtils.fastWriteText(str, path);
                    }
                    customerLatch.countDown();
                }
            });
        }
        IoStreamUtils.closeWrite();
        latch.await();
        System.out.println("customer thread completed! " + Duration.ofMillis(System.currentTimeMillis() - beginTime));

//        Duration mills1 = Duration.ofMillis(System.currentTimeMillis() - beginTime);
//
//        beginTime = System.currentTimeMillis();
//        for (int i = 0; i < 10000; i++) {
//            IoStreamUtils.fastWriteText("Line " + (i + 1), path);
//        }
//        IoStreamUtils.closeWrite();
//        System.out.println("customer aloneStand completed! " + Duration.ofMillis(System.currentTimeMillis() - beginTime));
//
//        Duration mills2 = Duration.ofMillis(System.currentTimeMillis() - beginTime);
//
//        beginTime = System.currentTimeMillis();
//        for (int i = 0; i < 100000; i++) {
//            logger.debug("Line " + (i + 1));
//        }
//        System.out.println("logger.slf4j completed! " + Duration.ofMillis(System.currentTimeMillis() - beginTime));
//
//        Duration mills3 = Duration.ofMillis(System.currentTimeMillis() - beginTime);
//
//        System.out.println(mills1);
//        System.out.println(mills2);
//        System.out.println(mills3);
    }

    @Test
    public void projectIOTest() throws IOException {
        List<Map<Document, IndexOptions>> maps = buildCreateIndexs();
        System.out.println(maps);
    }



    private List<String> getIndexCommandList() {
        File file = new File("src/main/resources/init/colletionInit");
        String url = this.getClass().getClassLoader().getResource("init/colletionInit").getPath() + "";
        file = new File(url);
        InputStream inputStream = null;
        List<String> list = new ArrayList<>();
        try {
            inputStream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void buildIndexOptionsByJsonString(IndexOptions indexOptions, String optionJson) {
        JSONObject jsonObject = JSON.parseObject(optionJson);
        for (Map.Entry<String, Object> option : jsonObject.entrySet()) {
            String key = option.getKey();
            // 仅有expireAfterSeconds，需要特殊处理
            if ("expireAfterSeconds".equalsIgnoreCase(key)) {
                Integer expireAfterSeconds = (Integer)option.getValue();
                indexOptions.expireAfter(expireAfterSeconds.longValue(), TimeUnit.SECONDS);
                continue;
            }
            Class clazz = String.class;
            if (option.getValue() instanceof Boolean) {
                clazz = boolean.class;
            } else if (option.getValue() instanceof Integer) {
                clazz = Integer.class;
            } else if (option.getValue() instanceof Double) {
                clazz = Double.class;
            }
            try {
                Method setOption = indexOptions.getClass().getMethod(key, clazz);
                setOption.invoke(indexOptions, option.getValue());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private List<Map<Document, IndexOptions>> buildCreateIndexs() {
        List<String> indexCommandList = getIndexCommandList();
        List<Map<Document, IndexOptions>> indexPramList = new ArrayList<>();
        for (String command : indexCommandList) {
            String indexRegex = "\\{.*?\\}";
            Pattern pattern = Pattern.compile(indexRegex);
            Matcher matcher = pattern.matcher(command);
            Map<Document, IndexOptions> map = new HashMap<>();
            Document document = new Document();
            IndexOptions indexOptions = new IndexOptions();
            // 索引条件
            if (matcher.find()) {
                String indexPram = matcher.group();
                document = Document.parse(indexPram);
            }
            // 索引配置
            if (matcher.find()) {
                buildIndexOptionsByJsonString(indexOptions, matcher.group());
            }
            map.put(document, indexOptions);
            indexPramList.add(map);
        }
        return indexPramList;
    }

    @Test
    public void reflectTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        Method m = a.getClass().getMethod("asd", boolean.class);
        Reflect r = new Reflect();
        // 反射不需要实例化
        Method m = Reflect.class.getMethod("asd", boolean.class);
        // 调用方法需要实力化
        m.invoke(r, false);
    }

    public class Reflect {
        public void asd(boolean a) {
            System.out.println("test1");
        }

        public void asd(Boolean a) {
            System.out.println("test2");
        }
    }
}
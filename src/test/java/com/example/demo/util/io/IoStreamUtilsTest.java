package com.example.demo.util.io;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IoStreamUtilsTest {

    private static final Logger logger = LoggerFactory.getLogger(IoStreamUtilsTest.class);

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

    /**
     * @Author chen_bq
     * @Description 扫描路径下所有正则匹配对象
     * @Date 2020/4/15 17:21
     * @return void
     */
    @Test
    public void scanFileDirTest(){
//        String path = "E:\\workplace\\domestic2\\server";
//        String path = "E:\\workplace\\merge2";
//        String path = "E:\\workplace\\domestic2\\server\\macc-org\\src\\main\\java\\com\\ruijie\\cloud\\macc\\org\\entity\\enums";
        String path = "E:\\workplace\\merge0430\\macc-msw";
        List<String> list = new ArrayList<>();
        List<String> removeList = new ArrayList<>();
        List<String> removeNotesList = new ArrayList<>();
        String regex = "\"( |,)*[\\u4e00-\\u9fa5]+.*?\"";
        String removeNotesRegex = "//.*\"( |,)*[\\u4e00-\\u9fa5]+.*?\"";
        String removeLogRegex = "(?<=((error|info|warn|Exception)\\())\"( |,)*[\\u4e00-\\u9fa5]+.*?\"";
        System.out.println("开始扫描");
        IoStreamUtils.findPreciseForDir(regex, new File(path), list, ".java");
        IoStreamUtils.findPreciseForDir(removeNotesRegex, new File(path), removeList, ".java");
        IoStreamUtils.findPreciseForDir(removeLogRegex, new File(path), removeNotesList, ".java");
        System.out.println("扫描结束");
        if (list == null || list.size() <= 0){
            System.out.println("没有检索到信息");
            return;
        }
        System.out.println("检索到条目总数：" + list.size());
        System.out.println("检索到无效条目总数：" + removeList.size());
        System.out.println("检索到日志条目总数：" + removeNotesList.size());
        if (removeNotesList != null && removeNotesList.size() > 0){
            removeList.addAll(removeNotesList);
        }
        list = list.stream().distinct().collect(Collectors.toList());
        removeList = removeList.stream().distinct().collect(Collectors.toList());
        String saveFile = "E:\\test\\list.out";
        for (String removeKey : removeList){
            list.removeIf(n -> removeKey.contains(n));
        }
        System.out.println("最终输出行数： " + list.size());
        StringBuilder sb = new StringBuilder();
        for (String key : list){
            sb.append(key + "\n");
        }
//        IoStreamUtils.fastWriteText(sb.toString(), saveFile);
        IoStreamUtils.writeTextIntoFile(sb.toString(), saveFile);
    }

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
        File file = new File("src/main/resources/init/collectionInit");
        String url = this.getClass().getClassLoader().getResource("init/collectionInit").getPath() + "";
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
        // 调用方法需要实例化
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
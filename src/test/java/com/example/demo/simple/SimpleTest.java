package com.example.demo.simple;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.entity.simple.BigDecimalEntity;
import com.example.demo.model.entity.simple.ConfigEntity;
import com.example.demo.model.entity.simple.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-18 11:02
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTest.class);

    /**
     * @Author chen_bq
     * @Description 测试list中的对象修改对list的影响，证明本质上都是指向对象的
     * @Date 2019/9/29 16:42
     */
    @Test
    public void listTest() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setStr("1");
        User user2 = new User();
        user2.setStr("2");
        User user3 = new User();
        user3.setStr("3");
        users.add(user1);
        users.add(user2);
        users.add(user3);
        System.out.println(users);
        change(users);
        System.out.println(users);
    }

    private void change(List<User> users) {
        users.get(0).setStr("change 1");
        users.get(1).setStr("change 2");
        users.get(2).setStr("change 3");
    }

    /**
     * @Author chen_bq
     * @Description 证明new Date().getTime() 和 System.currentTimeMillis() 结果一致
     * @Date 2019/9/29 16:43
     */
    @Test
    public void test1() {
        System.out.println(new Date().getTime());
        System.out.println(System.currentTimeMillis());
        System.out.println(new Date().getTime() == System.currentTimeMillis());
    }

    private ConfigEntity configEntity = new ConfigEntity();

    @Test
    public void test2() {
        System.out.println(configEntity);
        // 撒币想要注入纯实体
//        ConfigEntity configEntity = SpringContextUtils.getBean(ConfigEntity.class);
//        System.out.println(configEntity);
    }

    /**
     * @Author chen_bq
     * @Description 测试截取list的结果，并改变参数的情况
     * @Date 2019/9/29 16:40
     */
    @Test
    public void subListTest() {
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setStr("asd");
        list.add(user);
        List<User> sublist = list.subList(0, 1);
        System.out.println(sublist);
        sublist.get(0).setStr("qwe");
        System.out.println(sublist);
        System.out.println(list);
        List<User> itList = new ArrayList<>();
        itList.add(list.get(0));
        itList.get(0).setStr("zxc");
        System.out.println(itList);
        System.out.println(list);
    }

    /**
     * @Author chen_bq
     * @Description java8 测试Steam sorted的lambda表达式
     * @Date 2019/9/29 16:39
     */
    @Test
    public void streamSortLambdaTest() {
        List<BigDecimalEntity> list = new ArrayList<>();
        list.add(new BigDecimalEntity(new BigDecimal(1), new BigDecimal(2)));
        list.add(new BigDecimalEntity(new BigDecimal(2), new BigDecimal(2)));
        list.add(new BigDecimalEntity(new BigDecimal(3), new BigDecimal(2)));
        list.add(new BigDecimalEntity(new BigDecimal(4), new BigDecimal(2)));
        list.add(new BigDecimalEntity(new BigDecimal(-1), new BigDecimal(2)));
        list.add(new BigDecimalEntity(new BigDecimal(-2), new BigDecimal(2)));
        list.add(new BigDecimalEntity(new BigDecimal(-3), new BigDecimal(2)));
        // 从小到大排序排序，解释：p1为前者，p2为后者，前后对比大小，默认小到大
        List<BigDecimalEntity> listOrdered = list.stream().sorted((p1, p2) -> (p1.getTest1().subtract(p2.getTest1()).intValue()))
            .collect(Collectors.toList());
        for (BigDecimalEntity b : listOrdered) {
            System.out.println(b);
        }
        System.out.println("****************************");
        // 从大到小排序排序，解释：p1为前者，p2为后者，前后对比大小，默认小到大，取反值则从大到小
        listOrdered = list.stream().sorted((p1, p2) -> (p2.getTest1().subtract(p1.getTest1()).intValue()))
            .collect(Collectors.toList());
        for (BigDecimalEntity b : listOrdered) {
            System.out.println(b);
        }
        System.out.println("****************************");
        // 顺便测试一下直接使用sort
        list.sort((p1, p2) -> (p2.getTest1().subtract(p1.getTest1()).intValue()));
        for (BigDecimalEntity b : list) {
            System.out.println(b);
        }
        System.out.println("****************************");
        // 从大到小排序排序，解释：p2为前者，p1为后者，前后对比大小，默认小到大，但前后顺序相反则打印为从大到小
        // 即，判断大于0则交换位置
        listOrdered = list.stream().sorted((p2, p1) -> (p1.getTest1().subtract(p2.getTest1()).intValue()))
            .collect(Collectors.toList());
        for (BigDecimalEntity b : listOrdered) {
            System.out.println(b);
        }
    }

    /**
     * @Author chen_bq
     * @Description 测试Stream filter 拦截的lambda表达式
     * @Date 2019/9/29 16:44
     */
    @Test
    public void stramFiltterLambdaTest() {
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        integers.add(5);
        //拦截小于3的部分
        List<Integer> filters = integers.stream().filter(e -> e < 3)
            .collect(Collectors.toList());
        for (Integer i : filters) {
            System.out.println(i);
        }
    }

    /**
     * @Author chen_bq
     * @Description 测试Stream过程流处理的lambda表达式
     * @Date 2019/9/29 16:45
     */
    @Test
    public void streamFlatTest() {
        List<String> strings = Arrays.asList("Hello", "World");
        // Arrays::stream将一个数组转换为一个流
        List<String> words = strings.stream()
            .map(s -> s.split("")) // 得到字符数组
            .flatMap(Arrays::stream) // 将字符数组中的每个元素都换成另外一个流，然后把所有的流连接起来形成一个新的流
            .distinct()
            .collect(Collectors.toList());
        for (String str : words) {
            System.out.println(str);
        }
    }

    /**
     * @Author chen_bq
     * @Description 测试截取list，更新，增删的变化
     * @Date 2019/9/29 16:46
     */
    @Test
    public void sublistTest() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("this is " + 1);
        arrayList.add("this is " + 2);
        arrayList.add("this is " + 3);
        arrayList.add("this is " + 4);
        System.out.println("初始值");
        for (Object o : arrayList) {
            System.out.println(o);
        }
        // 不可转换成ArrayList
        List list = arrayList.subList(1, 3);
        System.out.println("截取值");
        for (Object o : list) {
            System.out.println(o);
        }
        String str = (String)list.get(0);
        str += " update";
        list.remove(0);
        list.add(str);
        System.out.println("截取值 更新");
        for (Object o : list) {
            System.out.println(o);
        }
        System.out.println("初始值");
        for (Object o : arrayList) {
            System.out.println(o);
        }
    }

    private Logger logger = LoggerFactory.getLogger(SimpleTest.class);

    /**
     * @Author chen_bq
     * @Description slf4的测试
     * @Date 2019/9/29 16:46
     */
    @Test
    public void slf4jTest() {
        logger.info("info 喵喵喵？？？");
        logger.debug("debug 喵喵喵？？？");
        String[] test = {"1", "2"};
        List<String> list = Arrays.asList(test);
        try {
            list.add("3");
            for (String str : list) {
                System.out.println(str);
            }
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getCause());
            System.out.println("不支持Arrays.asList() 之后的add/clear/remove 操作");
        }
    }

    /**
     * @Author chen_bq
     * @Description 测试bufferreader读取json
     * @Date 2019/9/29 16:47
     */
    @Test
    public void bufferReader() throws IOException {
        List<JSONObject> json = new ArrayList<>();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name", "test Name 1");
        jsonObject1.put("test", "test 1");
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("name", "test Name 2");
        jsonObject2.put("test", "test 2");
        json.add(jsonObject1);
        json.add(jsonObject2);
        StringBuilder buffer = new StringBuilder();
        for (Object dataString : json) {
            buffer.append(dataString + System.lineSeparator());
        }
        StringReader sr = new StringReader(buffer.toString());
        BufferedReader br = new BufferedReader(sr);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @Author chen_bq
     * @Description redisTemplate的pop方法结果测试
     * @Date 2019/9/29 16:47
     */
    @Test
    public void redisTest() throws InterruptedException {
        redisTemplate.opsForList().leftPush("test1", "redis pop test");
        // pop即取出
        System.out.println(redisTemplate.opsForList().leftPop("test1"));
        // 返回为null
        System.out.println(redisTemplate.opsForList().leftPop("test1"));
        redisTemplate.opsForValue().set("asd", "喵喵喵？？？", 2, TimeUnit.SECONDS);
        System.out.println(redisTemplate.opsForValue().get("asd"));
        Thread.sleep(2000);
        // 超时抛弃
        System.out.println(redisTemplate.opsForValue().get("asd"));

    }

    /**
     * @Author chen_bq
     * @Description atomicLong 测试类
     * @Date 2019/9/25 13:59
     */
    @Test
    public void atomicLongTest() {
        // Java原子类，用于计数器，效率低于longAdder
        AtomicLong atomicLong = new AtomicLong();
        // 相当于getNum(); num++;
        System.out.println(atomicLong.getAndIncrement());
        System.out.println(atomicLong.getAndIncrement());
        System.out.println(atomicLong.getAndIncrement());
    }

    @Test
    public void listItertorTet() {
        List<User> list1 = new ArrayList();
        for (int i = 0; i < 10; i++) {
            list1.add(getUser(i + " list1"));
        }
        List<User> list2 = new ArrayList();
        for (int i = 0; i < 10; i++) {
            list2.add(getUser(i + " list2"));
        }
        Iterator<User> it = list2.iterator();
        for (int i = 0; i < list1.size(); i++) {
            while (it.hasNext()) {
                list1.get(i).setStr(list1.get(i).getStr() + it.next().getStr());
                it.remove();
                break;
            }
        }
        System.out.println(list1);
    }

    private User getUser(String string) {
        User user = new User();
        user.setStr(string);
        return user;
    }

    @Test
    public void test3() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/init/collectionInit");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List list = new ArrayList();
        while ((line = br.readLine()) != null) {
            list.add(line);
        }
        System.out.println(list);
    }

    @Autowired
    private RestTemplate restTemplate;

    private static final String CHANNEL_KEY = "";
    private static final String CHANNEL_SECRET = "";
    private static final String GET_TOKEN_URL = "http://test.ruijienetworks.com/api/token/get";
    @Test
    public void test4() throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", CHANNEL_KEY);
        jsonObject.put("secret", CHANNEL_SECRET);
        HttpEntity httpEntity = new HttpEntity(jsonObject, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(GET_TOKEN_URL, httpEntity, String.class);
        System.out.println(response);

    }

    @Test
    public void test5(){
        String json = "{\"userId\":\"04000000@qq.com\",\"tenantId\":1,\"accountId\":3,\"company\":\"company\",\"location\":\"FuZhou\",\"position\":\"CEO\",\"punchPercent\":90,\"imageUrl\":\"/asd/asd\",\"evaluation\":3}";
        JSONObject jsonObject = JSONObject.parseObject(json);
        File file = new File("C:\\Users\\chen_bangqiang\\Pictures\\Saved Pictures\\test.jpg");
        FileSystemResource resource = new FileSystemResource(file);
        MultiValueMap<String, Object> mapFile = new LinkedMultiValueMap<>();
        mapFile.add("file", resource);
        mapFile.add("punch", json);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","multipart/form-data");
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(mapFile,headers);
        ResponseEntity<String> postForEntity = restTemplate.postForEntity("http://localhost:8080/service/api/workshop/punch",
            httpEntity, String.class);
        String body = postForEntity.getBody();
    }

    @Test
    public void test6(){
        String key = "test";
        redisTemplate.opsForValue().set(key, 1);
        System.out.println(redisTemplate.opsForValue().get(key));
        Object o = redisTemplate.opsForValue().get(key);
        if (o != null){
            Integer val = (Integer)o;
            val += 1;
            redisTemplate.opsForValue().set(key, val);
        }
//        redisTemplate.opsForValue().increment(key);
        System.out.println(redisTemplate.opsForValue().get(key));
    }

}

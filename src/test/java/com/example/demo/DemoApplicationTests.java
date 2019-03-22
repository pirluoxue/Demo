package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.demo.dao.RedisDAO;
import com.example.demo.entity.BigDecimalEntity;
import com.example.demo.entity.LongTestEntity;
import com.example.demo.entity.TestTypeEntity;
import com.example.demo.entity.User;
import com.example.demo.entity.change.TestChangeJson;
import com.example.demo.util.TimeUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private RedisDAO redisDAO;

    @Test
    public void testRedis(){
//        redisDAO.set("asd","test dao");
        System.out.println(redisDAO.get("asd"));
    }

    @Test
    public void testChangeAssert(){
        TestChangeJson testChangeJson = new TestChangeJson();
        testChangeJson.setA("1");
        testChangeJson.setB("2");
        testChangeJson.setC("3");
        redisDAO.set("test", testChangeJson);
        if(redisDAO.get("test") instanceof TestChangeJson){
            TestChangeJson changeJson = (TestChangeJson) redisDAO.get("test");
            System.out.println(changeJson);
            System.out.println(redisDAO.get("test"));
        }else {
            System.out.println("change fail");
        }
//        String str = (TestChangeJson) redisDAO.get("test");
//        TestChangeJson changeJson = JSON.parseObject((String)redisDAO.get("test"), new TypeReference<TestChangeJson>(){});
//        System.out.println(changeJson);
//        TestChangeJson changeJson = JSON.parseObject(str, new TypeReference<TestChangeJson>(){});
//        System.out.println(changeJson);
    }

    @Test
    public void testSDF() throws ParseException {
        String str = "2018-12-12 - 2018-12-13";
//        String str = "2018-12-12";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
//        System.out.println(sdf.format(sdf1.parse(str)));
        for(String time: TimeUtil.getSplitIntervalTimeByRegex(str, " - ", "yyyy-MM-dd")){
            System.out.println(time);
        }
    }

    @Test
    public void testBigDecimal(){
        BigDecimalEntity bIgDecimalEntity = new BigDecimalEntity();
        bIgDecimalEntity.setTest1(new BigDecimal(1));
        bIgDecimalEntity.setTest2(new BigDecimal(2));
        BigDecimalEntity bigDecimalEntity1 = new BigDecimalEntity();
        bigDecimalEntity1.setTest1(bIgDecimalEntity.getTest1().add(bIgDecimalEntity.getTest2()));
        System.out.println(bigDecimalEntity1.getTest1());
        BigDecimal bigDecimal = new BigDecimal(0);
        bigDecimal = bigDecimal.add(bIgDecimalEntity.getTest1().add(bIgDecimalEntity.getTest2()));
        System.out.println(bigDecimal);
    }

    @Test
    public void testlong(){
        LongTestEntity a = new LongTestEntity();
        System.out.println(a.getA());
        System.out.println(a.getB());
        a.setA(123456789109L);
        a.setB(123456789109L);
        System.out.println(a.getA());
        System.out.println(a.getB());
    }

    @Test
    public void testBigInteger(){
        TestTypeEntity testTypeEntity = new TestTypeEntity();
        System.out.println(testTypeEntity.getBigInteger());
//        testTypeEntity.setBigInteger(new big);
    }

    @Test
    public void testField() throws IllegalAccessException {
        BigDecimalEntity bigDecimalEntity = new BigDecimalEntity();
        Field[] fields = bigDecimalEntity.getClass().getDeclaredFields();
        for(Field field: fields){
            field.setAccessible(true);
            System.out.println(field);
        }
        for (int i = 0 ; i < fields.length ; i++){
            if(fields[i].getName().equals("test1")){
                fields[i].set(bigDecimalEntity,new BigDecimal(123));
            }
        }
    }

    @Test
    public void testParam(){
        List<BigDecimalEntity> bigDecimalEntities = new ArrayList<>();
        BigDecimalEntity bigDecimalEntity = new BigDecimalEntity();
        bigDecimalEntity.setTest1(new BigDecimal(1));
        bigDecimalEntities.add(bigDecimalEntity);
        System.out.println(bigDecimalEntities);
        BigDecimalEntity param = bigDecimalEntities.get(0);
        changeParam(param);
        System.out.println(bigDecimalEntities);
    }

    private void changeParam(BigDecimalEntity bigDecimalEntity){
        bigDecimalEntity.setTest1(new BigDecimal(999));
        bigDecimalEntity.setTest2(new BigDecimal(999));
    }

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testRestTemplate(){
        HttpHeaders httpHeaders = new HttpHeaders();
        String authHeader = "Basic bWlhbzptaWFv";
        httpHeaders.add("authorization", authHeader);
        HttpEntity<String> entity = new HttpEntity<String>(null, httpHeaders);
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTMwNjg5MTAsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjA4N2UwZjcyLWMyZjctNDUzMi05YjhiLTU1NTY4NTEzN2ZhOSIsImNsaWVudF9pZCI6Im1pYW8iLCJzY29wZSI6WyJhbGwiXX0.TIdhDGkrxBlLWt-yM6eYXfzBf0kWuJe1obazZt5ncaY";
        String invalidToken = token + " error";
        String checkTokenUrl = "http://localhost:8888/oauth/check_token?token=" + invalidToken;
        String rs = restTemplate.postForObject(checkTokenUrl, entity, String.class);
        JSONObject jsonObject = JSONObject.parseObject(rs);
        System.out.println(jsonObject);
    }

    @Test
    public void testIndexOfNoteUrl(){
        String html = "<a href=\"https://www.biqudu.com/\">首页</a></li>\n" +
                "\t\t<li><a rel=\"nofollow\" href=\"http://www.biqudu.com/modules/article/bookcase.php\">我的书架</a></li>\n" +
                "\t\t<li><a href=\"http://www.biqudu.com/xuanhuanxiaoshuo/\">玄幻小说</a></li>";
        String regex = "(href=\")+.*(\">)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(html);
        while (m.find()){
            System.out.println(m.group());
        }
    }



}

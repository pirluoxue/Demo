package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.RedisDAO;
import com.example.demo.model.entity.simple.BigDecimalEntity;
import com.example.demo.model.entity.simple.LongTestEntity;
import com.example.demo.model.entity.simple.TestTypeEntity;
import com.example.demo.model.entity.change.TestChangeJson;
import com.example.demo.service.NoteService;
import com.example.demo.util.TimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
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
        String testHTML = "<li>\n" +
                "                        <a href=\"https://www.biqudu.com/\">首页</a>\n" +
                "                        <a rel=\"nofollow\" href=\"http://www.biqudu.com/modules/article/bookcase.php\">我的书架</a>\n" +
                "                        <a href=\"http://www.biqudu.com/paihangbang/\">排行榜单</a>\n" +
                "                        <a href=\"http://www.biqudu.com/wanbenxiaoshuo/\" target=\"_blank\">完本小说</a>\n" +
                "                <script type=\"text/javascript\">list1();</script>\n" +
                "                    <a href=\"/\">笔趣阁</a> &gt; 暧昧高手最新章节列表\n" +
                "                            <a href=\"javascript:;\" onclick=\"showpop('/modules/article/addbookcase.php?bid=1961&ajax_request=1');\">加入书架</a>,\n" +
                "                            <a href=\"#footer\">直达底部</a>\n" +
                "                        <p>最后更新：2018-11-22 03:00:43</p>\n" +
                "                            <a href=\"/1_1961/3474231.html\">第3229章 最后的抉择3(大结局)</a>\n" +
                "                        <p>各位书友要是觉得《暧昧高手》还不错的话请不要忘记向您QQ群和微博里的朋友推荐哦！</p>\n" +
                "                <div id=\"sidebar\">\n" +
                "                        <img alt=\"暧昧高手\" src=\"/files/article/image/2/1961/1961.jpg\" width=\"120\" height=\"150\" />\n" +
                "                        <span class=\"b\"></span>\n" +
                "                <div id=\"listtj\"></div>\n" +
                "            <div class=\"box_con\">\n" +
                "                        <dt>《暧昧高手》最新章节\n" +
                "                        </b>（提示：已启用缓存技术，最新章节可能会延时显示，登录书架即可实时查看。）\n" +
                "                        <a href=\"/1_1961/3474231.html\">第3229章 最后的抉择3(大结局)</a>\n" +
                "                    </dd>\n";
        String regex = "(href=\")+\\S*?\"";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(testHTML);
        while (m.find()){
            System.out.println(m.group());
        }
    }

    @Autowired
    private NoteService noteService;
    @Test
    public void testNoteService(){
        noteService.collectionNoteByIndexUrl("https://www.biqudu.com/54_54836/", "e:\\notePack\\", "test");
    }

}

package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.demo.dao.RedisDAO;
import com.example.demo.entity.BigDecimalEntity;
import com.example.demo.entity.LongTestEntity;
import com.example.demo.entity.TestTypeEntity;
import com.example.demo.entity.User;
import com.example.demo.entity.change.TestChangeJson;
import com.example.demo.util.TimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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



}

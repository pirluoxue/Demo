package com.example.demo.bean;

import com.example.demo.model.entity.simple.SimpleEntity;
import com.example.demo.util.common.CommonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname TestRestTemplate
 * @Description TODO
 * @Date 2019-05-10
 * @Created by chen_bq
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class TestRestTemplate {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void test(){
        String url = "http://localhost:8088/test/rest";
        SimpleEntity simpleEntity = new SimpleEntity();
        Map map = new HashMap();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        map.put("key4", "value4");
        map.put("key5", "value5");
        simpleEntity.setMap(map);
//        String rs1 = restTemplate.postForObject(url, simpleEntity, String.class);
//        System.out.println(rs1);
        String rs2 = CommonUtil.httpsRequest(url, RequestMethod.POST, simpleEntity.toString());
        System.out.println(rs2);
    }


}

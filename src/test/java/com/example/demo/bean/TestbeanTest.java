package com.example.demo.bean;

import com.example.demo.model.entity.simple.SuperUser;
import com.example.demo.model.entity.simple.User;
import com.example.demo.model.entity.bean.TestBeanEntity;
import com.example.demo.model.entity.change.TestChangeJson;
import com.example.demo.util.SpringContextUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestbeanTest {

    @Qualifier("qualifierTestChangeJson")
    @Autowired

    private TestChangeJson json1;
    @Qualifier("testQualifier2")
    @Autowired
    private TestChangeJson json2;

    @Qualifier("testQualifier")
    @Autowired
    private TestBeanEntity json3;

    @Test
    public void test(){
        TestBeanEntity testbean = SpringContextUtils.getBean("testQualifier");
        System.out.println(testbean);
        System.out.println("直接获得bean属性：" + json1);
        System.out.println("直接获得bean属性：" + json2);
        System.out.println("直接获得bean属性：" + json3);
    }

    @Qualifier("userBean")
    @Autowired
    private User autoUser;

    @Test
    public void testUser(){
        User user = SpringContextUtils.getBean("userBean");
        System.out.println(user);
        System.out.println(autoUser);
    }

    @Test
    public void testSuperUser(){
        SuperUser superUser = SpringContextUtils.getBean("getSuperUser");
        System.out.println(superUser);
    }


}
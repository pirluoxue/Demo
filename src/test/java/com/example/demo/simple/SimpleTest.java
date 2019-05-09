package com.example.demo.simple;

import com.example.demo.model.entity.clone.SimpleCloneEntity;
import com.example.demo.model.entity.simple.ConfigEntity;
import com.example.demo.model.entity.simple.User;
import com.example.demo.util.SpringContextUtils;
import com.fasterxml.jackson.databind.ser.impl.UnknownSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-18 11:02
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleTest {

    @Test
    public void test(){
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
    private void change(List<User> users){
        users.get(0).setStr("change 1");
        users.get(1).setStr("change 2");
        users.get(2).setStr("change 3");
    }

    @Test
    public void test1(){
        System.out.println(new Date().getTime());
        System.out.println(System.currentTimeMillis());
        System.out.println(new Date().getTime()==System.currentTimeMillis());
    }

    private ConfigEntity configEntity = new ConfigEntity();

    @Test
    public void test2(){
        System.out.println(configEntity);
        //撒币想要注入纯实体
//        ConfigEntity configEntity = SpringContextUtils.getBean(ConfigEntity.class);
//        System.out.println(configEntity);
    }

}

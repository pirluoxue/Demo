package com.example.demo.util;

import com.example.demo.model.entity.clone.SimpleCloneEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//排除数据源自动配置
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class JooqUtilTest {

    @Test
    public void testJooq(){
        ConnectionUtil connectionUtil = DataResourceUtil.getInstance().getConnection("jdbc1");
        System.out.println(connectionUtil);
    }

}
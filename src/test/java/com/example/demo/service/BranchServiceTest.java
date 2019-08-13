package com.example.demo.service;

import com.example.demo.service.Impl.Branch1ServiceImpl;
import com.example.demo.service.Impl.Branch2ServiceImpl;
import com.example.demo.util.SpringContextUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class BranchServiceTest {

    @Test
    public void getBranch() {
        BranchService branchService = SpringContextUtils.getBean(Branch1ServiceImpl.class);
        System.out.println(branchService.getBranch());
        //不存在继承关系，自然报错
        BranchService branchService2 = SpringContextUtils.getBean(Branch2ServiceImpl.class);
        System.out.println(branchService2.getBranch());


    }
}
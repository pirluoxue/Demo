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
        BranchService branchService1 = SpringContextUtils.getBean(Branch1ServiceImpl.class);
        System.out.println("注入 branchService1 实现接口");
        System.out.println(branchService1.getBranch());
        System.out.println(" branchService1 实现接口的 default 方法");
        System.out.println(branchService1.branchParent());
        BranchService branchService2 = SpringContextUtils.getBean(Branch2ServiceImpl.class);
        System.out.println("注入 branchService2 实现接口");
        System.out.println(branchService2.getBranch());
        System.out.println(" branchService2 实现接口的 default 方法");
        System.out.println(branchService2.branchParent());


    }
}
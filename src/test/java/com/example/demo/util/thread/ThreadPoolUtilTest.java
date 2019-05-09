package com.example.demo.util.thread;

import com.example.demo.util.SpringContextUtils;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * @Classname ThreadPoolUtilTest
 * @Description TODO
 * @Date 2019-05-09
 * @Created by chen_bq
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class ThreadPoolUtilTest {

    @Test
    //使用@Async注入的方式
    public void testAsync() {
        int threadNum = Thread.activeCount();
        System.out.println("初始线程数： " + threadNum);
        for(int i = 0 ;i < 100 ; i++){
            ThreadPoolUtil threadPoolUtil = SpringContextUtils.getBean(ThreadPoolUtil.class);
            threadPoolUtil.testAsync();
        }
        while (true){
            System.out.println("当前线程数： " + Thread.activeCount());
            if(threadNum >= Thread.activeCount()){
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Autowired
    private TaskExecutor taskExecutor;

    @Test
    public void testExecutor() throws InterruptedException {
        int threadNum = Thread.activeCount();
        System.out.println("初始线程数： " + threadNum);
        //记录线程处理，其实可以理解为线程安全的int值，而且还需要手动在线程中调用countDown减线程
        CountDownLatch latch = new CountDownLatch(10);
        System.out.println("latch初始数量：" + latch.getCount());
        for(int i = 0 ;i < 10 ; i++){
            taskExecutor.execute(new ThreadExecutor(latch));
        }
//        while (true){
//            Thread.sleep(2000);
//            System.out.println("latch数量：" + latch.getCount());
//        }
        latch.await();// 等待所有人任务结束
        System.out.println("所有的统计任务执行完成");
    }


}
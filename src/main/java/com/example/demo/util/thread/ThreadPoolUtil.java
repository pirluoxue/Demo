package com.example.demo.util.thread;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Classname ThreadPoolUtil
 * @Description 直接使用线程池的工具类
 * @Date 2019-05-09
 * @Created by chen_bq
 */
@Component
public class ThreadPoolUtil {

    @Async("taskExecutor")
    public void testAsync(){
        System.out.println(Thread.currentThread().getName() + " 运行中~");
        try {
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName() + " 执行完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread.interrupted();
    }

}

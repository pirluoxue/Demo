package com.example.demo.util.thread;

import java.util.concurrent.CountDownLatch;

/**
 * @Classname ThreadExecutor
 * @Description 直接注入TaskExecutor，调用execute使用线程
 * @Date 2019-05-09
 * @Created by chen_bq
 */
public class ThreadExecutor implements Runnable{

    private CountDownLatch countDownLatch;

    public ThreadExecutor(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " 正在运行ing···");
        try {
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName() + " 执行完毕!");
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        Thread.interrupted();
        countDownLatch.countDown();
    }
}

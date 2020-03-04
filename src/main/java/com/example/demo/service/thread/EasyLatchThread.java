package com.example.demo.service.thread;

import java.util.concurrent.CountDownLatch;

/**
 * @Author chen_bq
 * @Description 简单获取实现等待回调的线程工具
 * @Date 2019/11/22 17:33
 */
public interface EasyLatchThread extends Runnable {
    CountDownLatch latch = new CountDownLatch(1);
    CountDownLatch getLatch();
}

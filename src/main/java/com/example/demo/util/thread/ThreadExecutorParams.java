package com.example.demo.util.thread;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @Classname ThreadExecutorParams
 * @Description TODO
 * @Date 2019-05-22
 * @Created by chen_bq
 */
public class ThreadExecutorParams implements Runnable {
    private CountDownLatch countDownLatch;

    public ThreadExecutorParams(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " 正在运行ing···");
        String value = "";
//        try {
            Map<String, String> map = ThreadPublicParams.getInstance().getMap();
            if ("value".equals(map.get("key"))) {
                value = map.get("key");
                System.out.println(Thread.currentThread().getName() + "获得参数：" + map.get("key"));
                map.remove("key");
//                Thread.sleep(200);
                map.put("key", Thread.currentThread().getName() + "");
            }else{
                System.out.println(Thread.currentThread().getName() + "不等了，再见~~");
            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Thread.interrupted();
        countDownLatch.countDown();
    }
}

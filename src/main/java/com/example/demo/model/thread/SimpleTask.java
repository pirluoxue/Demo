package com.example.demo.model.thread;

import java.util.concurrent.Callable;

/**
 * @author chen_bq
 * @description
 * @create: 2019-03-23 17:06
 **/
public class SimpleTask implements Callable<Boolean> {
    @Override
    public Boolean call() throws Exception {
        Thread.sleep(3000);
        System.out.println("工作" + Thread.currentThread().getName());
        Thread.sleep(3000);
        System.out.println("工作" + Thread.currentThread().getName() + "完成了");
        return true;
    }
}

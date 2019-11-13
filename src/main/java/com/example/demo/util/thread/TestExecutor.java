package com.example.demo.util.thread;

import java.util.Comparator;
import java.util.concurrent.CountDownLatch;

/**
 * @author chen_bq
 * @description 测试调用线程工具
 * @create: 2019/10/22 10:35
 **/
public class TestExecutor<T> implements Runnable{
    // todo 输入方法，run直接执行
    private CountDownLatch countDownLatch;

    private Comparator<? super T> lambda;

    @Override
    public void run() {

    }
}

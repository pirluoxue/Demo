package com.example.demo.util.thread;

import lombok.Data;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Classname TreadPublicParams
 * @Description 线程公共参数
 * @Date 2019-05-22
 * @Created by chen_bq
 */
@Data
public class ThreadPublicParams {

    //测试公共参数
    private Map<String,String> map = new ConcurrentHashMap();
    private static ThreadPublicParams threadPublicParams;

    private Map<String,Integer> calcMap = new ConcurrentHashMap();
    private Map<String,LongAdder> longAdderMap = new ConcurrentHashMap();
    private Queue<String> queue = new ConcurrentLinkedQueue();
    private LongAdder longAdder = new LongAdder();

    public static ThreadPublicParams getInstance(){
        if (threadPublicParams == null) {
            threadPublicParams = new ThreadPublicParams();
        }
        return threadPublicParams;
    }

    public void increase(String key){
        calcMap.replace(key, calcMap.get(key) + 1);
    }

    public void increaseLongAdder(String key){
        longAdderMap.get(key).increment();
    }


}

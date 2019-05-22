package com.example.demo.util.thread;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    public static ThreadPublicParams getInstance(){
        if (threadPublicParams == null) {
            threadPublicParams = new ThreadPublicParams();
        }
        return threadPublicParams;
    }


}

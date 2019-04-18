package com.example.demo.model.thread;

import com.example.demo.model.entity.simple.SimpleEntity;

import java.nio.file.FileSystemNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-18 15:18
 **/
public class ProductThread implements Runnable {
    @Override
    public void run() {
        int i = 10;
        SimpleEntity simpleEntity = SimpleEntity.getInstance();
        while (i > 0){
//            try {
                Map map = SimpleEntity.getInstance().getMap();
                synchronized (map){
                    map.put("keys" + i, "ConnectionLink");
                    System.out.println("生产资源~ keys" + i + " 当前资源数：" + map.size());
                    i--;
                    map.notifyAll();
//                    Thread.sleep(1000);
                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        System.out.println("全部生产完毕！");
        Thread.interrupted();
    }
}

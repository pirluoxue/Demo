package com.example.demo.model.thread;

import com.example.demo.model.entity.simple.SimpleEntity;
import lombok.Data;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-18 15:19
 **/
@Data
public class CustomerThread implements Runnable {
    private long timeout = 1000;

    @Override
    public void run() {
        Map map = SimpleEntity.getInstance().getMap();
        try {
            synchronized (map) {
                if (map.size() > 0) {
                    System.out.println("尝试获取数据~");
                    Set set = map.keySet();
                    Iterator iterator = set.iterator();
                    if (iterator.hasNext()) {
                        String key = (String)iterator.next();
                        System.out.println("获得并清除该资源~ " + key );
                        map.remove(key);
                        Thread.sleep(1000);
                    }
                } else {
                    System.out.println("等待获取数据~");
                    map.wait(timeout);
                    System.out.println("唤醒！尝试获取数据~ 我看见了" + map.size() + "个产品");
                    run();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread.interrupted();
    }
}

package com.example.demo.util;

import com.example.demo.model.thread.SimpleThread;
import com.example.demo.model.thread.ThreadLocalEntity;
import com.example.demo.util.thread.ThreadLocalUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ThreadLocalUtil.class)
public class ThreadLocalUtilTest {

    @Test
    public void threadLocalTest(){
        Map<String, LocalDateTime> map = new HashMap<>();
        System.out.println(LocalDateTime.now());
        for(int i = 0 ;i < 10000; i ++){
            Instant instant = Instant.ofEpochMilli(System.currentTimeMillis() + i);
            ZoneId zone = ZoneId.systemDefault();
            map.put(i + "", LocalDateTime.ofInstant(instant, zone));
        }
        Set<String> keys = map.keySet();
        System.out.println(LocalDateTime.now());
        for(String key:keys){
            if(map.get(key) == null){
                System.out.println("!!!");
            }
        }
        System.out.println(LocalDateTime.now());
    }

    @Test
    public void memoryTest(){
        Map<String, ThreadLocalEntity> map = new HashMap<>();
        for(int i = 0 ;i < 10000; i ++){
            ThreadLocalEntity threadLocalEntity = new ThreadLocalEntity();
            threadLocalEntity.setCreateTime(new Timestamp(System.currentTimeMillis() - i ));
            threadLocalEntity.setSession("this is session " + i);
            map.put(i + "", threadLocalEntity);
        }
        System.out.println("原session数量： " + map.size());
        ThreadLocalUtil.setThreadLocalMap(map);
        System.out.println(LocalDateTime.now());
        System.out.println(ThreadLocalUtil.getInstance().getSession("9999"));
        System.out.println(LocalDateTime.now());
        Map<String, ThreadLocalEntity> result = (Map<String, ThreadLocalEntity>)ThreadLocalUtil.getInstance().getThreadLocal().get();
        ThreadLocalUtil.getInstance().remove();
        System.out.println("过期移除后数量： " + result.size());
    }

    @Test
    public void test() throws InterruptedException {
        Map<String, ThreadLocalEntity> map = new HashMap<>();
        for(int i = 0 ;i < 3; i ++){
            ThreadLocalEntity threadLocalEntity = new ThreadLocalEntity();
            threadLocalEntity.setCreateTime(new Timestamp(System.currentTimeMillis() - i ));
            threadLocalEntity.setSession("this is session " + i);
            map.put(i + "", threadLocalEntity);
        }
        ThreadLocalUtil.setThreadLocalMap(map);
        System.out.println("初始化map长度 " + map.size());
        int nowThreadCount = Thread.activeCount();
        for(int i = 0; i < 3 ;i++){
            SimpleThread simpleThread = new SimpleThread(ThreadLocalUtil.getInstance().getThreadLocal(), i);
            Thread thread = new Thread(simpleThread);
            thread.start();
            Map testMap = (Map<String, ThreadLocalEntity>)simpleThread.getThreadLocal().get();
            System.out.println("获取key 1 为 " + testMap.get("1"));
        }
        SimpleThread simpleThread = new SimpleThread(ThreadLocalUtil.getInstance().getThreadLocal());
        Thread thread = new Thread(simpleThread);
        thread.start();
        while (true){
            Thread.sleep(1000);
            System.out.println("剩余线程数量 " + Thread.activeCount());
            if(Thread.activeCount() <= nowThreadCount){
                break;
            }
        }
        map = (Map<String, ThreadLocalEntity>) ThreadLocalUtil.getInstance().getThreadLocal().get();
        if(map !=null){
            System.out.println("最终map长度 " + map.size());
            System.out.println("线程 " + thread.getName());
            Map<String, ThreadLocalEntity> test = (Map<String, ThreadLocalEntity>)simpleThread.getThreadLocal().get();
            Iterator iterator = test.keySet().iterator();
            while (iterator.hasNext()) {
                System.out.println(test.get(iterator.next()));
            }
            System.out.println("ThreadLocalUtil.getInstance() 数据为 " + ThreadLocalUtil.getInstance().getThreadLocal().get());
        }else{
            System.out.println("boom!!!");
        }
        ThreadLocalUtil.getInstance().remove();

    }

    @Test
    public void simpleRemoveMap(){
        Map map = new HashMap();
        ThreadLocalEntity threadLocalEntity = new ThreadLocalEntity();
        threadLocalEntity.setSession("test1");
        map.put("1", threadLocalEntity);
        ThreadLocalUtil.getInstance().getThreadLocal().set(map);
        Map getMap = (Map) ThreadLocalUtil.getInstance().getThreadLocal().get();
        System.out.println(getMap.get("1"));
        getMap.put("2", threadLocalEntity);
        Map getAgainMap = (Map) ThreadLocalUtil.getInstance().getThreadLocal().get();
        Iterator iterator = getAgainMap.keySet().iterator();
        while (iterator.hasNext()){
            System.out.println(getAgainMap.get(iterator.next()));
        }
    }


}
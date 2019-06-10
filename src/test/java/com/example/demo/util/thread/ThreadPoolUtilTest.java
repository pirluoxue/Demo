package com.example.demo.util.thread;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.SpringContextUtils;
import com.google.common.base.Strings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Classname ThreadPoolUtilTest
 * @Description TODO
 * @Date 2019-05-09
 * @Created by chen_bq
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class ThreadPoolUtilTest {

    @Test
    //使用@Async注入的方式
    public void testAsync() {
        int threadNum = Thread.activeCount();
        System.out.println("初始线程数： " + threadNum);
        for(int i = 0 ;i < 100 ; i++){
            ThreadPoolUtil threadPoolUtil = SpringContextUtils.getBean(ThreadPoolUtil.class);
            threadPoolUtil.testAsync();
        }
        while (true){
            System.out.println("当前线程数： " + Thread.activeCount());
            if(threadNum >= Thread.activeCount()){
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Autowired
    private TaskExecutor taskExecutor;

    @Test
    public void testExecutor() throws InterruptedException {
        int threadNum = Thread.activeCount();
        System.out.println("初始线程数： " + threadNum);
        //记录线程处理，其实可以理解为线程安全的int值，而且还需要手动在线程中调用countDown减线程
        CountDownLatch latch = new CountDownLatch(10);
        System.out.println("latch初始数量：" + latch.getCount());
        for(int i = 0 ;i < 10 ; i++){
            taskExecutor.execute(new ThreadExecutor(latch));
        }
//        while (true){
//            Thread.sleep(2000);
//            System.out.println("latch数量：" + latch.getCount());
//        }
        latch.await();// 等待所有人任务结束
        System.out.println("所有的统计任务执行完成");
    }

    /*公共参数多线程测试*/
    @Test
    public void testPublicParams() throws InterruptedException {
        int threadNum = Thread.activeCount();
        System.out.println("初始线程数： " + threadNum);
        ThreadPublicParams threadPublicParams = ThreadPublicParams.getInstance();
        Map map = new ConcurrentHashMap();
        map.put("key", "value");
        threadPublicParams.setMap(map);
        CountDownLatch latch = new CountDownLatch(10);
        System.out.println("latch初始数量：" + latch.getCount());
        for(int i = 0 ;i < 10 ; i++){
            taskExecutor.execute(new ThreadExecutorParams(latch));
        }
        latch.await();// 等待所有人任务结束
        System.out.println("所有的统计任务执行完成");
    }

    /*多线程计数器测试*/
    @Test
    public void testAdderByMap() throws InterruptedException {
        int threadNum = Thread.activeCount();
        System.out.println("初始线程数： " + threadNum);
        ThreadPublicParams threadPublicParams = ThreadPublicParams.getInstance();
        Map<String, LongAdder> stringLongAdderMap = new ConcurrentHashMap<>();
        stringLongAdderMap.put("count", new LongAdder());
        threadPublicParams.setLongAdderMap(stringLongAdderMap);
        CountDownLatch latch = new CountDownLatch(100);
        System.out.println("latch初始数量：" + latch.getCount());
        LocalDateTime beginTime = LocalDateTime.now();
        for(int i = 0 ;i < 100 ; i++){
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    ThreadPublicParams count = ThreadPublicParams.getInstance();
//                    Map<String,String> countMap = count.getMap();
                    for(int i = 0 ;i < 100000 ; i ++){
                        //使用concurrentHashMap计数，但是依然无法解决问题
//                        count.increase("count");
                        //使用LongAdder计数,成功
//                        count.getLongAdder().increment();
                        //混合concurrentHashMap和LongAdder的计数器，为了解决单容器，多计数的问题
                        count.increaseLongAdder("count");
                    }
                    latch.countDown();
                }
            });
        }
        latch.await();// 等待所有人任务结束
        LocalDateTime endTime = LocalDateTime.now();
        System.out.println("所有的统计任务执行完成");
//        System.out.println("最后统计埋点数量为： " + threadPublicParams.getCalcMap().get("count"));
//        System.out.println("最后统计埋点数量为： " + threadPublicParams.getLongAdder().sum());
        System.out.println("最后统计埋点数量为： " + threadPublicParams.getLongAdderMap().get("count").sum());
        Duration duration = Duration.between(beginTime, endTime);
        System.out.println("总用时：" + duration);
        threadPublicParams.getCalcMap().replace("asd",1);
        System.out.println("顺便测试replace为null的情况：" + threadPublicParams.getCalcMap().get("asd"));
    }

    /*多线程测试队列*/
    @Test
    public void testQueueForThred() throws InterruptedException {
        int threadNum = Thread.activeCount();
        System.out.println("初始线程数： " + threadNum);
        ThreadPublicParams threadPublicParams = ThreadPublicParams.getInstance();
        Queue queue = new ConcurrentLinkedQueue();
        for(int i = 0 ; i < 1000 ; i++){
            queue.add("task" + i);
        }
        threadPublicParams.setQueue(queue);
        CountDownLatch latch = new CountDownLatch(10);
        System.out.println("latch初始数量：" + latch.getCount());
        LocalDateTime beginTime = LocalDateTime.now();
        for(int i = 0 ;i < 10 ; i++){
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0 ;i < 1000 ; i ++){
                        String task = ThreadPublicParams.getInstance().getQueue().poll();
                        if(!Strings.isNullOrEmpty(task)){
                            System.out.println(Thread.currentThread().getName() + " 获得 " + task);
                        }else{
                            System.out.println(Thread.currentThread().getName() + " 拿不到task，task取完");
                            break;
                        }
                    }
                    latch.countDown();
                }
            });
        }
        latch.await();// 等待所有人任务结束
        LocalDateTime endTime = LocalDateTime.now();
        System.out.println("所有的统计任务执行完成");
        Duration duration = Duration.between(beginTime, endTime);
        System.out.println("总用时：" + duration);
    }

    @Test
    public void testLoopPost() throws InterruptedException {
        int threadNum = Thread.activeCount();
        CountDownLatch latch = new CountDownLatch(10);
        for(int i = 0 ;i < 10 ; i++){
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    RestTemplate restTemplate = SpringContextUtils.getBean(RestTemplate.class);
//                    String url = "http://localhost:8085/order/api/prePayOrder";
//                    String url = "http://localhost:8085/order/api/prePayOrder?openId=oVb6x1EOpoJBIqPmCZDXlmyJT2RA&deviceCode=30000028&scanId=1&orderSource=1&menuId=42&cardId=270";
                    String url = "http://localhost:8085/order/api/prePayOrder?openId=oVb6x1EOpoJBIqPmCZDXlmyJT2RA&deviceCode=22000038&scanId=1&orderSource=1&menuId=11&cardId=81";
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("openId", "oVb6x1EOpoJBIqPmCZDXlmyJT2RA");
//                    jsonObject.put("deviceCode", "20002639");
//                    jsonObject.put("scanId", "1");
//                    jsonObject.put("orderSource", "1");
//                    jsonObject.put("menuId", "11");
//                    jsonObject.put("cardId", "59");
//                    String rs = restTemplate.postForObject(url, jsonObject.toString(), String.class);
                    String rs = restTemplate.postForObject(url,null, String.class);
                    System.out.println(rs);
                    latch.countDown();
                }
            });
        }
        latch.await();// 等待所有人任务结束
        System.out.println("所有的统计任务执行完成");
    }

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testRestPost(){
        String url = "http://localhost:8088/test/restpost";
//        User user = new User();
//        user.setUserId(123123);
//        user.setUserAddress("阿里布达");
//        String rs = restTemplate.postForObject(url, user, String.class);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId","123123");
        String rs = restTemplate.postForObject(url, jsonObject, String.class);
        System.out.println(rs);
    }

}
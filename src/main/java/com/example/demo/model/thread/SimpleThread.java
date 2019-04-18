package com.example.demo.model.thread;

import com.example.demo.util.ThreadLocalUtil;
import com.example.demo.util.test.simple.rabbitmq.helloworld.P;
import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import lombok.Data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-11 10:44
 **/
@Data
public class SimpleThread implements Runnable {

    private ThreadLocal threadLocal;
    private ThreadLocalEntity threadLocalEntity;

    public SimpleThread(ThreadLocal threadLocal){
        this.threadLocal = threadLocal;
        Map<String, ThreadLocalEntity> result = (Map<String, ThreadLocalEntity>)this.threadLocal.get();
        System.out.println("线程 " + Thread.currentThread().getName() + "  " + result.get("1"));
    }

    @Override
    public void run() {
        //即便使用了instance模式，获得的依然还是该线程的独有的threadlocal，因此数据为空
        Map<String, ThreadLocalEntity> result = (Map<String, ThreadLocalEntity>)ThreadLocalUtil.getInstance().getThreadLocal().get();
        long id = Thread.currentThread().getId();
        System.out.println("线程 " + Thread.currentThread().getName()
                + "\n线程ID " + id );
        if(result != null){
            if(result.get((int) id) != null){
                System.out.println("线程 " + Thread.currentThread().getName() + "  获得map值为：" + result.get((int) id));
            }
        }else{
            System.out.println("线程 " + Thread.currentThread().getName() + " 无法获得其他线程的ThreadLocal数据");
            System.out.println("尝试获得传入该线程数据");
            //即便通过赋值获得了threadlocal对象，也仅仅只能通过对象获得这个属性，但是在线程内部this.threadLocal数据依旧为空
            if(this.threadLocal.get() != null) {
                Map<String, ThreadLocalEntity> map = (Map<String, ThreadLocalEntity>)this.threadLocal.get();
                ThreadLocalEntity threadLocalEntity = new ThreadLocalEntity();
                threadLocalEntity.setSession("this is " + Thread.currentThread().getName());
                threadLocalEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
                map.put(Thread.currentThread().getName(), threadLocalEntity);
                System.out.println("线程 " + Thread.currentThread().getName() + " 数据长度为 " + map.size());
            }else{
                System.out.println("传入线程的ThreadLocal数据无法访问！");
                System.out.println("生成当前ThreadLocal数据");
                Map<String, ThreadLocalEntity> map = new HashMap<>();
                ThreadLocalEntity threadLocalEntity = new ThreadLocalEntity();
                threadLocalEntity.setSession("this is " + Thread.currentThread().getName());
                threadLocalEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
                map.put(Thread.currentThread().getName(), threadLocalEntity);
                ThreadLocalUtil.getInstance().getThreadLocal().set(map);
            }
        }
        Map<String, ThreadLocalEntity> map = (Map<String, ThreadLocalEntity>)ThreadLocalUtil.getInstance().getThreadLocal().get();
        System.out.println("线程 " + Thread.currentThread().getName() + "  结束时数据：" + map.get(Thread.currentThread().getName()));
        this.threadLocalEntity = map.get(Thread.currentThread().getName());
        ThreadLocalUtil.getInstance().remove();
        Thread.interrupted();
    }
}

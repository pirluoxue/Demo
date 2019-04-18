package com.example.demo.util;

import com.example.demo.model.thread.ThreadLocalEntity;
import lombok.Data;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author chen_bq
 * @description ThreadLocal测试工具类
 * @create: 2019-04-09 14:08
 **/
@Data
public class ThreadLocalUtil {

    private static ThreadLocal threadLocal;
    private static ThreadLocalUtil threadLocalUtil;

    private static long threadLocalTimeout = 1000L;

    public static ThreadLocalUtil getInstance(){
        if(threadLocal == null){
            threadLocal = new ThreadLocal();
        }
        if(threadLocalUtil == null){
            threadLocalUtil = new ThreadLocalUtil();
        }
        return threadLocalUtil;
    }

    public void remove(){
        threadLocal.remove();
        threadLocal = null;
        threadLocalUtil = null;
    }

    public ThreadLocal getThreadLocal(){
        if(threadLocalUtil == null){
            threadLocalUtil = getInstance();
        }
        return threadLocal;
    }

    public static void setThreadLocalMap(Map map){
        getInstance().getThreadLocal().set(map);
    }

    public ThreadLocalEntity getSession(String key){
        Map<String, ThreadLocalEntity> map= (Map<String, ThreadLocalEntity>) threadLocal.get();
        ThreadLocalEntity threadLocalEntity = map.get(key);
        if(threadLocalEntity != null){
            long now = System.currentTimeMillis();
            long duration = now - threadLocalEntity.getCreateTime().getTime();
            if(duration < threadLocalTimeout){
                return threadLocalEntity;
            }
            removeExpire(map);
        }
        return null;
    }



    /**
     * 清除线程内的其他过期信息
     * 效率待验证
     * @param sessionMap
     */
    private void removeExpire(Map<String, ThreadLocalEntity> sessionMap){

        //todo 清除线程内的其他过期信息
        Set<String> keys = sessionMap.keySet();
        Iterator it = keys.iterator();
        long now = System.currentTimeMillis();
        while(it.hasNext()){
            String key = (String)it.next();
            ThreadLocalEntity session = sessionMap.get(key);
            if (session != null) {
                long duration = now - session.getCreateTime().getTime();
                if (duration < threadLocalTimeout) {
                    continue;
                }
            }
            it.remove();
            sessionMap.remove(key);
        }
    }


}

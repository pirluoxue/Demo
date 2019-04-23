package com.example.demo.model.entity.proxy;

import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

/**
 * 动态代理模式demo
 */
public class GirlEntityTest {

    public static void main(String[] args) {
        MasterEntity masterEntity = new MasterEntity();

        PeopleEntity peopleEntity = (PeopleEntity) Proxy.newProxyInstance(masterEntity.getClass().getClassLoader()
                //代理了MasterEntity对象
                , masterEntity.getClass().getInterfaces()
                , (proxy, method, arg) -> {

                    // 如果是调用becoming方法，那么执行这段代码
                    if (method.getName().equals("becoming")) {
                        method.invoke(masterEntity, arg);
                        System.out.println("我是水军，我来点赞了！");
                    } else {
                        // 如果不是调用coding方法，那么调用原对象的方法
                        return method.invoke(masterEntity, arg);
                    }

                    return null;
                });

        peopleEntity.becoming();

    }

}
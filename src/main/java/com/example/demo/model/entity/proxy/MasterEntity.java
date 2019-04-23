package com.example.demo.model.entity.proxy;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-20 17:56
 **/
public class MasterEntity implements PeopleEntity{

    @Override
    public void becoming() {
        System.out.println("我来了~");
    }


}

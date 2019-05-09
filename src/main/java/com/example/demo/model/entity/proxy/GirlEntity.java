package com.example.demo.model.entity.proxy;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-20 17:57
 **/
public class GirlEntity implements PeopleEntity{

    @Override
    public void becoming() {
        System.out.println("女生来了~");
    }

}

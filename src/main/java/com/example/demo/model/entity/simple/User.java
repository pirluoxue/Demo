package com.example.demo.model.entity.simple;

import lombok.Data;

/**
 * @author chen_bq
 * @description
 * @create: 2019-02-23 15:22
 **/
@Data
public class User extends SuperUser {

    private String str;

    @Override
    public void test(){
        System.out.println("User");
        str = super.getStr();
        System.out.println(str);
        System.out.println(super.getStr());
        super.test();
        str = super.getStr();
        System.out.println(str);
        System.out.println(super.getStr());
    }

    public String getStr1(){
        return this.str;
    }
    public String getStr2(){
        return super.getStr();
    }

    public static void main(String[] args) {
        User user = new User();
        user.setStr("asd");
        System.out.println("实例化子类~");
        System.out.println("子类值" + user.getStr1());
        System.out.println("父类值" + user.getStr2());
        SuperUser superUser = new SuperUser();
        System.out.println("实例化父类~");
        superUser.setStr("test");
        System.out.println("父类值" + superUser.getStr());
        System.out.println("子类转父类~");
        superUser = user;
        System.out.println("父类值" + superUser.getStr());
        System.out.println("父类强转子类 子类值" + ((User) superUser).getStr1());
        System.out.println("父类强转子类 父类值" + ((User) superUser).getStr2());
    }

}



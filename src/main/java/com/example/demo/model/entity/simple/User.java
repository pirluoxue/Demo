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
        System.out.println(user.getStr1());
        System.out.println(user.getStr2());
        SuperUser superUser = new SuperUser();
        superUser.setStr("test");
        System.out.println(superUser.getStr());
        superUser = user;
        System.out.println(superUser.getStr());
        System.out.println(((User) superUser).getStr1());
        System.out.println(((User) superUser).getStr2());
    }

}



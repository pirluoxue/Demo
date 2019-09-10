package com.example.demo.model.entity.simple;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

//    public static void main(String[] args) {
//        User user = new User();
//        user.setStr("asd");
//        System.out.println("实例化子类~");
//        System.out.println("子类值" + user.getStr1());
//        System.out.println("父类值" + user.getStr2());
//        SuperUser superUser = new SuperUser();
//        System.out.println("实例化父类~");
//        superUser.setStr("test");
//        System.out.println("父类值" + superUser.getStr());
//        System.out.println("子类转父类~");
//        superUser = user;
//        System.out.println("父类值" + superUser.getStr());
//        System.out.println("父类强转子类 子类值" + ((User) superUser).getStr1());
//        System.out.println("父类强转子类 父类值" + ((User) superUser).getStr2());
//
//        System.out.println("父类直接强转子类");
//        try {
//            SuperUser superUser1 = new SuperUser();
//            User test = (User) superUser1;
//            System.out.println("测试结果 " + test);
//        }catch (Exception e){
//            System.out.println("非通过子类初始化的父类，无法直接转换为子类");
//        }
//    }

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setStr("1");
        users.add(user1);
        User user2 = new User();
        user2.setStr("2");
        users.add(user2);
        User user3 = new User();
        user3.setStr("3");
        users.add(user3);
        User user4 = new User();
        user4.setStr("4");
        users.add(user4);
        User user5 = new User();
        user5.setStr("5");
        users.add(user5);
        User user6 = new User();
        user6.setStr("6");
        users.add(user6);
        System.out.println(users);
        user1.setStr("test");
        System.out.println(users);
    }


}



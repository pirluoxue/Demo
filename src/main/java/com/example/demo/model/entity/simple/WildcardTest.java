package com.example.demo.model.entity.simple;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @Classname WildcardTest
 * @Description 通配符测试
 * @Date 2019-07-08
 * @Created by chen_bq
 */
@Data
public class WildcardTest<E> {

    private Collection<E> stack;

    WildcardTest(){
        this.stack = new ArrayList<>();
    }

    public void push(E e){
        stack.add(e);
    }

//    //参数值若不显性申明，则不会报错
//    public void pushAll(Iterable<E> it){
//        for (E e: it){
//            push(e);
//        }
//    }

    public void pushAll(Iterable<? extends E> it){
        for (E e: it){
            push(e);
        }
    }

    public E pop(){
        if (!isEmpty() || stack.iterator().hasNext()){
            E e = stack.iterator().next();
            stack.remove(e);
            return e;
        }
        return null;
    }

    public boolean isEmpty(){
        if (stack == null || stack.size() <= 0 || stack.isEmpty()){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        WildcardTest<String> wildcardTest = new WildcardTest();
        wildcardTest.push("test1");
        wildcardTest.push("test2");
        wildcardTest.push("test3");
        wildcardTest.push("test4");
        wildcardTest.push("test5");
//        System.out.println(wildcardTest.pop());
//        System.out.println(wildcardTest.pop());
//        System.out.println(wildcardTest.pop());
//        System.out.println(wildcardTest.pop());
//        System.out.println(wildcardTest.pop());
        //不同类型，没有父子关系的，无法使用
        Collection<Integer> collection = new ArrayList();
        collection.add(1);
        collection.add(2);
        collection.add(3);
        collection.add(4);
        collection.add(5);
//        wildcardTest.pushAll(collection);
        System.out.println(wildcardTest.pop());
        System.out.println(wildcardTest.pop());
        System.out.println(wildcardTest.pop());
        System.out.println(wildcardTest.pop());
        System.out.println(wildcardTest.pop());
    }
}

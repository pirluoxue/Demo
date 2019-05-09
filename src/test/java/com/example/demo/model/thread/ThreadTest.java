package com.example.demo.model.thread;

import com.example.demo.model.entity.simple.SimpleEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ThreadTest {

    public static void main(String[] args) throws InterruptedException {
        int activeCount = Thread.activeCount();
        Thread productThread = new Thread(new ProductThread());
        productThread.start();
        for(int i = 0 ;i < 10 ;i++){
            Thread thread = new Thread(new CustomerThread());
            thread.start();
        }
        while (activeCount < Thread.activeCount()){
            Thread.sleep(1000);
        }
    }



}
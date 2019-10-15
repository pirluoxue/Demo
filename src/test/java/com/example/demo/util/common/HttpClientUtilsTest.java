package com.example.demo.util.common;

import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class HttpClientUtilsTest {

    @Test
    public void AsyncRequestGet() throws InterruptedException {
        String baseUrl = "http://localhost:8088/test/get_param?";
        String param = "name={name}&number={number}";
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            String tmpParam = param.replace("{name}", "喵喵喵" + i);
            tmpParam = tmpParam.replace("{number}", 100 + i + "");
            String tmpUrl = baseUrl + tmpParam;
            HttpClientUtils.requestForAsyncGet(tmpUrl, Duration.ofMinutes(1), latch);
        }
        latch.await();
    }


}
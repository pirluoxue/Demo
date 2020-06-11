package com.example.demo.util.common;

import org.junit.Test;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

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

    @Test
    public void asyncRequestPost() throws InterruptedException {
//        String baseUrl = "http://localhost:8088/test/longPolling";
//        String baseUrl = "http://localhost:8088/test/rest";
//        String baseUrl = "http://localhost:8080/service/api/internationalRrm/rrm2/device/G1J91CK000634?access_token=BD0C56F2CC49442B8AA6910C5D6E3F32";
        String baseUrl = "http://localhost:8080/service/api/internationalRrm/rrm2/device/G1J91CK000634?access_token=BD0C56F2CC49442B8AA6910C5D6E3F32&immediate=false&start_time=1586484968000";
//        String baseUrl = "https://cloudtest-as.ruijienetworks.com/admin3/monitor/getMonitorDeviceList?groupId=4514";
        long beginTime = System.currentTimeMillis();
        List<CompletableFuture<HttpResponse>> futures = new ArrayList<>();
        ConcurrentHashMap<String, String> headers = new ConcurrentHashMap<>();
//        headers.put("Cookie", "JSESSIONID=DDFCC3E2FC3894195C1AFC9608660CF4; _gscu_2117649708=65749431mvene016; _ga=GA1.2.304034209.1566973221; _gid=GA1.2.751007043.1584498609; _gat_gtag_UA_120668435_3=1");
        CountDownLatch latch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            System.out.println("send async psot " + (i + 1));
            // 异步等待回调方案
//            futures.add(HttpClientUtils.requestForAsyncPost(baseUrl, Duration.ofMillis(1000)));
//            futures.add(HttpClientUtils.requestForAsyncPost(baseUrl, Duration.ofMillis(1000), latch));

            futures.add(HttpClientUtils.requestForAsyncPost(baseUrl, Duration.ofMillis(60000), null, headers, latch));
        }
//        // 异步等待回调方案
//        for (CompletableFuture<HttpResponse> future : futures){
//            try {
//                future.get();
//                System.out.println("返回正常");
//            } catch (ExecutionException e) {
//                System.out.println(e.getCause());
//            }
//        }
        latch.await();
        System.out.println(Duration.ofMillis(System.currentTimeMillis() - beginTime));
    }


}
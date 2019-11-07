package com.example.demo.util.common;

import com.alibaba.fastjson.JSON;
import com.example.demo.model.entity.simple.TestHttpPostEntity;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

/**
 * @author chen_bq
 * @description java11 httpClient工具类
 * @create: 2019/9/25 15:22
 **/
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
    public static final String POST = "post";
    public static final String GET = "get";

    public void postFile() throws FileNotFoundException {
        HttpRequest request = HttpRequest.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .uri(URI.create("http://httpbin.org/post"))
            .timeout(Duration.ofMinutes(2))
            .header("Content-Type", "octet-stream")
            .POST(HttpRequest.BodyPublishers.ofFile(Paths.get("HelloJava11.java")))
            .build();
        HttpClient client = HttpClient.newBuilder().build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(System.out::println)
            .join();
    }

    /**
     * @return void
     * @Author chen_bq
     * @Description post请求测试
     * @Date 2019/9/25 18:20
     */
    public void postTest() {
        String url = "http://localhost:8088/test/post_entity";
        TestHttpPostEntity postEntity = new TestHttpPostEntity();
        postEntity.setName("miao???");
        postEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        postEntity.setIgnoreStr("can't watch me");
        HttpRequest request = HttpRequest.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .uri(URI.create(url))
            // 默认超时时间
            .timeout(Duration.ofMinutes(2))
            .header("Content-Type", "application/json;charset=UTF-8")
            // 请求类型，参数体需要转换成string
            .POST(HttpRequest.BodyPublishers.ofString(JSON.toJSONString(postEntity)))
            .build();
        HttpClient client = HttpClient.newBuilder().build();
        // 异步请求
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            // 返回体
            .thenApply(rsp -> rsp)
            // 返回时处理
            .thenAccept(rsp -> {
                if (!Strings.isNullOrEmpty(rsp.body())) {
                    System.out.println(rsp.body());
                }
            })
            // 等待返回结果，相当于同步请求
            .join();

        // 同步请求
//        HttpResponse rsp = null;
//        try {
//            rsp = client.send(request, HttpResponse.BodyHandlers.ofString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(rsp.body());
    }

    /**
     * @return java.net.http.HttpResponse
     * @Author chen_bq
     * @Description java12 新增的httpclient get请求
     * @Date 2019/10/15 10:21
     * @Param [url, timeout]
     */
    public static HttpResponse requestForGet(String url, Duration timeout) {
        HttpRequest request = getHttpRequestForGet(url, timeout);
        HttpClient client = HttpClient.newBuilder().build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void requestForAsyncGet(String url, Duration timeout, CountDownLatch latch) {
        HttpRequest request = getHttpRequestForGet(url, timeout);
        HttpClient client = HttpClient.newBuilder().build();
        // 异步请求
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            // 返回体
            .thenApply(rsp -> rsp)
            // 返回时处理
            .thenAccept(rsp -> {
                if (!Strings.isNullOrEmpty(rsp.body())) {
                    logger.warn("request {} response body: {}", url, rsp.body());
                }
                latch.countDown();
            });
    }



    public static HttpResponse requestForPost(String url, Duration timeout){
        return requestForPost(url, timeout, null, null);
    }

    public static HttpResponse requestForPost(String url, Duration timeout, Class<?> postBody){
        return requestForPost(url,timeout, postBody, null);
    }

    public static HttpResponse requestForPost(String url, Duration timeout, Class<?> postBody, ConcurrentHashMap<String, String> headers){
        HttpRequest request = getHttpRequestForPost(url, timeout, postBody, headers);
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse httpResponse = null;
        try {
            httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return httpResponse;
    }

    public static HttpRequest getHttpRequestForGet(String url, Duration timeout) {
        return HttpRequest.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            // get请求的传统，还是要追加后缀。按照restful规范，载入页面的时候需要用get，
            // 为了避免过多的暴露参数，一般会对参数进行加密，服务端时再解密。但是这样效率较低，而且安全性依旧不高
            .uri(URI.create(url))
            // 默认超时时间
            .timeout(timeout)
            // 请求类型
            .GET()
            .build();
    }

    private static HttpRequest getHttpRequestForPost(String url, Duration timeout) {
        return getHttpRequestForPost(url, timeout, null, null);
    }

    private static HttpRequest getHttpRequestForPost(String url, Duration timeout, Class<?> entity) {
        return getHttpRequestForPost(url, timeout, entity, null);
    }

    public static HttpRequest getHttpRequestForPost(String url, Duration timeout, Class<?> postBody, ConcurrentHashMap<String, String> headers) {
        return HttpRequest.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .uri(URI.create(url))
            // 默认超时时间
            .timeout(Duration.ofMinutes(2))
            .headers(formatHeardersMap(headers))
            // 请求类型，参数体需要转换成string
            .POST(getBodyPublisherByEntity(postBody))
            .build();
    }

    private static HttpRequest.BodyPublisher getBodyPublisherByEntity(Class<?> postBody){
        HttpRequest.BodyPublisher bodyPublisher;
        if (postBody == null) {
            bodyPublisher = HttpRequest.BodyPublishers.noBody();
        }else {
            bodyPublisher = HttpRequest.BodyPublishers.ofString(JSON.toJSONString(postBody));
        }
        return bodyPublisher;
    }

    private static String[] formatHeardersMap(ConcurrentHashMap<String, String> headers){
        List headersList = new ArrayList();
        if (headers != null){
            for (Map.Entry entry: headers.entrySet()){
                headersList.add(entry.getKey());
                headersList.add(entry.getValue());
            }
        }
        if (headersList == null || headersList.size() <= 0){
            headersList.add("Content-Type");
            headersList.add("application/json;charset=UTF-8");
        }
        return TypeChangeUtils.list2StringArrays(headersList);
    }

    /**
     * @return void
     * @Author chen_bq
     * @Description get请求测试
     * @Date 2019/9/25 18:20
     */
    public void getTest() {
        String url = "http://localhost:8088/test/get_param?name=miao&number=1";
        Duration timeout = Duration.ofMinutes(2);
        HttpResponse rsp = HttpClientUtils.requestForGet(url, timeout);
        System.out.println(rsp.body());
    }

    public static void main(String[] args) {
        new HttpClientUtils().postTest();
        new HttpClientUtils().getTest();
    }

}

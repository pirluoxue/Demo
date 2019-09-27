package com.example.demo.util.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.entity.simple.TestHttpPostEntity;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import org.apache.http.Consts;
import org.apache.http.protocol.HTTP;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Base64;

/**
 * @author chen_bq
 * @description java11 httpClient工具类
 * @create: 2019/9/25 15:22
 **/
public class HttpClientUtils {

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
     * @Author chen_bq
     * @Description post请求测试
     * @Date 2019/9/25 18:20
     * @return void
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
                if (!Strings.isNullOrEmpty(rsp.body())){
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
     * @Author chen_bq
     * @Description get请求测试
     * @Date 2019/9/25 18:20
     * @return void
     */
    public void getTest(){
        String url = "http://localhost:8088/test/get_param?name=miao&number=1";
        HttpRequest request = HttpRequest.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            // get请求的传统，还是要追加后缀。按照restful规范，载入页面的时候需要用get，
            // 为了避免过多的暴露参数，一般会对参数进行加密，服务端时再解密。但是这样效率较低，而且安全性依旧不高
            .uri(URI.create(url))
            // 默认超时时间
            .timeout(Duration.ofMinutes(2))
            // 请求类型
            .GET()
            .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse rsp = null;
        try {
            rsp = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(rsp.body());
    }

    public static void main(String[] args) {
        new HttpClientUtils().postTest();
        new HttpClientUtils().getTest();
    }

}

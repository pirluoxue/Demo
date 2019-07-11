package com.example.demo.util.http;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname HttpClientUtil
 * @Description HttpClient工具类
 * @Date 2019-06-12
 * @Created by chen_bq
 */
public class HttpClientUtil {

    //获取需登录网站信息
    public static void main(String[] args) throws IOException {

        HttpClient client = HttpClientBuilder.create().build();
//        String url="http://localhost:8084/admin/login";
        String url="https://passport.baidu.com/v2/api/?login";
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
//        nvp.add(new BasicNameValuePair("username","admin"));
//        nvp.add(new BasicNameValuePair("password", "123456"));
        httpost.setEntity(new UrlEncodedFormEntity(nvp, HTTP.UTF_8));
        HttpResponse response1 = client.execute(httpost);
        httpost.abort();//关闭httppost，不关闭的话下面使用httpget会报错
        if (response1.getStatusLine().getStatusCode() == 302) {//使用httppost执行，会导致302重定向，response中会包含重定向的地址yyy，需使用get方式访问
//            HttpGet httpget = new HttpGet("http://localhost:8084/admin/system/role/list");
            HttpGet httpget = new HttpGet("https://pan.baidu.com/disk/home?errno=0&errmsg=Auth%20Login%20Sucess&&bduss=&ssnerror=0&traceid=#/all?path=%2F&vmode=list");
            HttpResponse response = client.execute(httpget);
            String entity = EntityUtils.toString (response.getEntity(),"utf-8");
            System.out.println(entity);//输出的就是html的内容
        }else {
            System.out.println("失败");
        }


    }

}

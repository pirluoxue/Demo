package com.example.demo.configuration;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author chen_bq
 * @Description RestTemplate配置
 * @Date 2019/3/20 14:59
 **/
@Configuration
public class RestTemplateConfig {

//    @Bean
//    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        factory.setReadTimeout(20000);//ms
//        factory.setConnectTimeout(20000);//ms
//        return factory;
//    }
//
//    @Bean
//    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
//        RestTemplate restTemplate = new RestTemplate(factory);
//        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
////        restTemplate.getMessageConverters().set(2, new ByteArrayHttpMessageConverter());
//        ResponseErrorHandler responseErrorHandler = new ResponseErrorHandler() {
//            @Override
//            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
//                //不检查状态码及抛异常
//                return false;
//            }
//            @Override
//            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
//            }
//        };
//        restTemplate.setErrorHandler(responseErrorHandler);
//        return restTemplate;
//    }

    /**
     * HttpComponentsClientHttpRequestFactory可以控制能够建立的连接数还能细粒度的控制到某个 server 的连接数
     * @param socketFactory
     * @return
     */
    @Bean
    public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory(SSLConnectionSocketFactory socketFactory){
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory ();
        factory.setReadTimeout(20000);//ms
        factory.setConnectTimeout(20000);//ms

        /*https过滤*/
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", socketFactory).build();
        //设置最大连接数
        PoolingHttpClientConnectionManager phccm = new PoolingHttpClientConnectionManager(registry);
        phccm.setMaxTotal(200);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).setConnectionManager(phccm).setConnectionManagerShared(true).build();
        factory.setHttpClient(httpClient);
        return factory;
    }

    @Bean
    public SSLConnectionSocketFactory sslConnectionSocketFactory() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, (X509Certificate[] x509Certificates, String s) -> true);
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
        return socketFactory;
    }

    @Bean
    public RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory factory){
        RestTemplate restTemplate = new RestTemplate(factory);
        //默认消息编码
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
//        restTemplate.getMessageConverters().set(2, new ByteArrayHttpMessageConverter());
        ResponseErrorHandler responseErrorHandler = new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                //不检查状态码及抛异常
                return false;
            }
            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {

            }
        };
        restTemplate.setErrorHandler(responseErrorHandler);
        return restTemplate;
    }

}

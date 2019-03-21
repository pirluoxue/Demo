package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author chen_bq
 * @Description RestTemplate配置
 * @Date 2019/3/20 14:59
 **/
@Configuration
public class RestTemplateConfig {
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(20000);//ms
        factory.setConnectTimeout(20000);//ms
        return factory;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        RestTemplate restTemplate = new RestTemplate(factory);
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

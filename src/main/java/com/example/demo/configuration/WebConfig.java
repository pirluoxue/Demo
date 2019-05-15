package com.example.demo.configuration;

import com.example.demo.components.annotation.CurrentUserMethodArgumentResolver;
import com.example.demo.components.annotation.MyRequestParamMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author chen_bq
 * @description web相关配置
 * @create: 2019-03-13 14:34
 **/
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CurrentUserMethodArgumentResolver());
        argumentResolvers.add(new MyRequestParamMethodArgumentResolver());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // static, 单独部署静态文件不需要static这个path,
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}

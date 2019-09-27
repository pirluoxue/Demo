package com.example.demo.bean;

import com.example.demo.components.filter.RestComplexFilter;
import com.example.demo.components.filter.RestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author chen_bq
 * @description 过滤器配置
 * @create: 2019/9/27 9:41
 **/
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean restFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new RestFilter());
        // 匹配路径
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        // 过滤顺序，数字小的先执行
        filterRegistrationBean.setOrder(2);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean restComplexFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new RestComplexFilter());
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/test/get_param"));
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

}

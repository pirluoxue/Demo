package com.example.demo.components.interceptor;


import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author chen_bq
 * @description
 * @create: 2019/9/27 9:32
 **/
public class RestTimeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long restBeginTime = System.currentTimeMillis();
        request.setAttribute("restBeginTime", restBeginTime);
        System.out.println("remark rest beginTime: " + new Timestamp(restBeginTime));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        Long now = System.currentTimeMillis();
        System.out.println("remark endTime: " + LocalDateTime.now());
        Long restBeginTime = 0L;
        if (request.getAttribute("restBeginTime") instanceof Long){
            restBeginTime = (Long)request.getAttribute("restBeginTime");
            System.out.println("completed by " + Duration.ofMillis(now - restBeginTime));
        }
    }

}

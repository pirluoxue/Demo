package com.example.demo.components.filter;


import com.example.demo.util.common.CommonUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author chen_bq
 * @description rest接口的过滤器
 * @create: 2019/9/27 9:28
 **/
public class RestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("this is RestFilter init >>> ");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("RestFilter filter nothing");
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        switch (httpServletRequest.getMethod()){
            case "POST" -> {
                HttpServletRequest tmpRequest = new RequestInputStreamReadWrapper(httpServletRequest);
                String body = CommonUtil.getPostBodyToStringByRequest(tmpRequest);
                System.out.println(body);
                filterChain.doFilter(tmpRequest, response);
            }
            case "GET" ->{
                System.out.println("get parameter: " + CommonUtil.getGetParamToStringByRequest(httpServletRequest));
                filterChain.doFilter(request, response);
            }
            default -> {
                filterChain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {}

}

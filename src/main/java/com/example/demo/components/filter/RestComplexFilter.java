package com.example.demo.components.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author chen_bq
 * @description rest接口的过滤器
 * @create: 2019/9/27 9:28
 **/
public class RestComplexFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("this is RestComplexFilter init >>> ");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("RestComplexFilter filter change name to wang!");
        RequestParameterWrapper requestParameterWrapper = new RequestParameterWrapper((HttpServletRequest)request);
        requestParameterWrapper.editParameter("name", "wang!");
        filterChain.doFilter(requestParameterWrapper, response);
    }

    @Override
    public void destroy() {}

}

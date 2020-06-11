package com.example.demo.components.filter;


import com.example.demo.util.common.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @author chen_bq
 * @description rest接口的过滤器
 * @create: 2019/9/27 9:28
 **/
public class RestFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RestFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("this is RestFilter init >>> ");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("RestFilter filter nothing");
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        switch (httpServletRequest.getMethod()) {
            case "POST": {
                HttpServletRequest tmpRequest = new RequestInputStreamReadWrapper(httpServletRequest);
                String body = CommonUtil.getPostBodyToStringByRequest(tmpRequest);
                System.out.println(body);
                filterChain.doFilter(tmpRequest, response);
                break;
            }
            case "GET": {
                System.out.println("get parameter: " + CommonUtil.getGetParamToStringByRequest(httpServletRequest));
                logger.info("URI {}", ((HttpServletRequest)request).getRequestURI());
                printIp((HttpServletRequest)request);
                filterChain.doFilter(request, response);
                break;
            }
            default: {
                filterChain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {
    }

    private void printIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        logger.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip {}", ip);
        // 这有一个很憨的问题，就是如果赋值这个对象，而直接使用hasMoreElements的话，就是永久死循环。相当于每次执行的方法都是重头执行的
        Enumeration<String> names= request.getHeaderNames();
        while (names.hasMoreElements()){
            String key = names.nextElement();
            logger.info("{} header value {}", key, request.getHeader(key));
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            logger.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String {}", ip);
            ip = request.getHeader("WL-Proxy-Client-IP");
            logger.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String {}", ip);
            ip = request.getHeader("HTTP_CLIENT_IP");
            logger.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String {}", ip);
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            logger.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String {}", ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            logger.info("getRemoteAddr - String {}", ip);
        }
    }

}

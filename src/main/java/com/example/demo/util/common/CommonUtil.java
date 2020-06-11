package com.example.demo.util.common;


import org.springframework.web.bind.annotation.RequestMethod;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;


/**
 * 通用工具类
 *
 * @author
 * @date
 */
public class CommonUtil {

    /**
     * 统一下单接口
     * 发送https请求
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return 返回微信服务器响应的信息
     */
    public static String httpsRequest(String requestUrl, RequestMethod requestMethod, String outputStr) {
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
			/*TrustManager[] tm = {new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();*/
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//            conn.setSSLSocketFactory(ssf);
            conn.setConnectTimeout(30000); // 设置连接主机超时（单位：毫秒)
            conn.setReadTimeout(30000); // 设置从主机读取数据超时（单位：毫秒)
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);// Post 请求不能使用缓存
            conn.setRequestMethod(requestMethod.name());// 设置请求方式（GET/POST）
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            System.out.println("outputStr------" + outputStr);
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            System.out.println("--------getResponseCode--------" + conn.getResponseCode() + "------- HttpURLConnection.HTTP_OK-------" + HttpsURLConnection.HTTP_OK);
            if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                return null;
            }
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString().trim();
        } catch (ConnectException ce) {
            System.out.println("连接超时：" + ce.getStackTrace());
        } catch (Exception e) {
            System.out.println("https请求异常：" + e.getStackTrace());
        }
        return null;
    }

    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getPostBodyToStringByRequest(HttpServletRequest request) {
        //获得输入流
        ServletInputStream servletInputStream = null;
        //创建StringBuilder暂存信息
        StringBuilder content = new StringBuilder();
        try {
            servletInputStream = request.getInputStream();
            byte[] b = new byte[1024];
            int lens = -1;
            //读入流
            while ((lens = servletInputStream.read(b)) > 0) {
                //写入StringBuilder
                content.append(new String(b, 0, lens));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public static String getGetParamToStringByRequest(HttpServletRequest request) {
		Map<String, String[]> paramMap = request.getParameterMap();
		StringBuilder paramString = new StringBuilder();
		if (paramMap == null || paramMap.size() <= 0){
		    return null;
        }
		String suffix = ", ";
		for (Map.Entry<String, String[]> entry: paramMap.entrySet()){
			paramString.append(entry.getKey() + ":" + Arrays.toString(entry.getValue()) + suffix);
		}
		paramString.replace(paramString.length() - suffix.length(), paramString.length(),"");
		return paramString.toString();
    }

}
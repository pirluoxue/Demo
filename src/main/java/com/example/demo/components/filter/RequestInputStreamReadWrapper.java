package com.example.demo.components.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * @author chen_bq
 * @description 多次读取post request参数的工具
 * @create: 2019/9/27 10:07
 **/
public class RequestInputStreamReadWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public RequestInputStreamReadWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = InputStreamToByte(request.getInputStream());
    }

    /**
     * 流转 字节数组
     * @param inputStream
     * @return byte[]
     * @throws IOException
     */
    private byte[] InputStreamToByte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int ch;
        while ((ch = inputStream.read(buffer)) != -1) {
            bytestream.write(buffer,0,ch);
        }
        byte data[] = bytestream.toByteArray();
        bytestream.close();
        return data;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

}

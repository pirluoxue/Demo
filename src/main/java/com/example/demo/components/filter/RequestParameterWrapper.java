package com.example.demo.components.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chen_bq
 * @description
 * @create: 2019/9/27 11:18
 **/
public class RequestParameterWrapper extends HttpServletRequestWrapper {

    private Map<String, String[]> params = new HashMap<String, String[]>();

    public RequestParameterWrapper(HttpServletRequest request) {
        super(request);
        this.params.putAll(request.getParameterMap());
    }

    public void addParameters(Map<String, Object> extraParams) {
        for (Map.Entry<String, Object> entry : extraParams.entrySet()) {
            addParameter(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 添加参数
     *
     * @param name
     * @param value
     */
    public void addParameter(String name, Object value) {
        if (value != null) {
            System.out.println(value);
            if (value instanceof String[]) {
                params.put(name, (String[])value);
            } else if (value instanceof String) {
                params.put(name, new String[]{(String)value});
            } else {
                params.put(name, new String[]{String.valueOf(value)});
            }
        }
    }

    public void editParameter(String key, Object value) {
        if (value instanceof String[] && params.keySet().contains(key)) {
            params.replace(key, (String[])value);
        }
    }

    public void editParameter(String key, String value) {
        String[] values = {value};
        editParameter(key, values);
    }

    /**
     * @Author chen_bq
     * @Description 重写get方法
     * @Date 2019/9/27 11:34
     * @Param [name]
     * @return java.lang.String
     */
    @Override
    public String getParameter(String name) {
        String[]values = params.get(name);
        if(values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    /**
     * @Author chen_bq
     * @Description 重写get方法
     * @Date 2019/9/27 11:34
     * @Param [name]
     * @return java.lang.String[]
     */
    @Override
    public String[] getParameterValues(String name) {
        return params.get(name);
    }

    /**
     * @Author chen_bq
     * @Description 重写get方法
     * @Date 2019/9/27 11:33
     * @return java.util.Map<java.lang.String,java.lang.String[]>
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        return params;
    }
}

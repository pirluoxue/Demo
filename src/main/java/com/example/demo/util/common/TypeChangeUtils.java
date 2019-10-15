package com.example.demo.util.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chen_bq
 * @description 类型转化工具类
 * @create: 2019/10/15 10:56
 **/
public class TypeChangeUtils {

    /**
     * @return java.util.List
     * @Author chen_bq
     * @Description 数组转List
     * @Date 2019/10/15 9:42
     * @Param [objects]
     */
    public static List objectArray2List(Object[] objects) {
        List list = new ArrayList();
        for (Object o : objects) {
            list.add(o);
        }
        return list;
    }

    public static String[] list2StringArrays(List list) {
        if (list == null || list.size() <= 0) {
            return null;
        }
        String[] arrays = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arrays[i] = (String)list.get(i);
        }
        return arrays;
    }

}

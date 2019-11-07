package com.example.demo.util.common;

import com.example.demo.util.io.IoStreamUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chen_bq
 * @description 类型转化工具类
 * @create: 2019/10/15 10:56
 **/
public class TypeChangeUtils<T> {

    private static final String LINE_FEED = "\r\n";

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

    /**
     * @Author chen_bq
     * @Description 本质上就是List.addAll
     * @Date 2019/10/17 14:53
     * @Param [lists]
     * @return java.util.List<T>
     */
    public static <T> List<T> mergeList(List<T>... lists) {
        List buildList = new ArrayList();
        for (List list : lists) {
            for (Object o : list) {
                buildList.add(o);
            }
        }
        return buildList;
    }

    public static String listToLineFeedString(List list) {
        StringBuilder builder = new StringBuilder();
        for (Object o : list) {
            builder.append(o.toString()).append(LINE_FEED);
        }
        return builder.toString();
    }

    /**
     * @Author chen_bq
     * @Description 对象List转化成List<List<String>
     * @Date 2019/10/18 16:35
     * @Param [list]
     * @return java.util.List<java.util.List<java.lang.String>>
     */
    public static <T> List<List<String>> changeListEntity2ListOfListString(List<T> list){
        if (list == null ||list.size()<= 0){
            return new ArrayList<>();
        }
        // 本来就是List<List<String>>的话，直接返回
        if (list.get(0) instanceof List){
            List tmp = (List)list.get(0);
            if (tmp != null && tmp.size() > 0 && tmp.get(0) instanceof String){
                return (List<List<String>>)list;
            }
        }
        Class clazz = list.get(0).getClass();
        // 获得所有对象
        Method[] method = clazz.getMethods();
        // todo
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        list.add("test3");
        String str = listToLineFeedString(list);
        IoStreamUtils.writeTextIntoFile(str, "E:\\work\\工作文档\\test1.txt");
    }

}

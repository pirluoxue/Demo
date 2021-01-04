package com.example.demo.util.common;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author bangqiang_chen
 * @Description 简单的列表工具类
 * @Date 11:12 2020/10/26
 **/
public class ArraysUtils {

    public static Object getValueByListMap(List<Map> listMap, String key){
        return getValueByListMap(listMap, key, null);
    }

    public static Object getValueByListMap(List<Map> listMap, String key, String defualtValue){
        if (listMap == null || listMap.size() <= 0){
            return defualtValue;
        }
        for (Map map : listMap){
            if (map == null || map.size() <= 0) {
                continue;
            }
            Object o = map.get(key);
            if (o == null){
                continue;
            }
            return o;
        }
        return defualtValue;
    }

    public static List<Map> listMapByCellKeyValue(List<Map> listMap, String key, String value){
        if (listMap == null || listMap.size() <= 0 || StringUtils.isAnyEmpty(key, value)){
            return null;
        }
        List<Map> targetListMap = new ArrayList<>();
        for (Map map : listMap){
            if (map == null || map.size() <= 0) {
                continue;
            }
            Object o = map.get(key);
            if (o == null){
                continue;
            }
            if (o instanceof String){
                String mapVal = (String) o;
                if (value.equalsIgnoreCase(mapVal)){
                    targetListMap.add(map);
                }
            }
        }
        return targetListMap;
    }


}

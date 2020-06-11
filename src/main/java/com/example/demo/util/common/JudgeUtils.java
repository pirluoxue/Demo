package com.example.demo.util.common;

import com.example.demo.model.entity.simple.User;
import com.example.demo.util.mongo.analysis.entity.MongoLogEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chen_bq
 * @description
 * @create: 2019/10/17 14:56
 **/
public class JudgeUtils {

    public static boolean listEquals(List list, List judgedList) {
        return listEquals(list, judgedList, false);
    }

    /**
     * @return boolean
     * @Author chen_bq
     * @Description 判断list是否完全一致
     * @Date 2019/10/17 14:57
     * @Param [list, judgedList]
     */
    public static <T> boolean listEquals(List<T> list, List<T> judgedList, boolean sort) {
        if (list == null || judgedList == null || list.size() != judgedList.size()) {
            return false;
        }
        if (sort){
            list = list.stream().sorted().collect(Collectors.toList());
            judgedList = judgedList.stream().sorted().collect(Collectors.toList());
        }
        for (int i = 0; i < list.size(); i++){
            if (!list.get(i).equals(judgedList.get(i))){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        List<String> list1 = new ArrayList();
        List<String> list2 = new ArrayList();
        list1.add("1");
        list1.add("2");
        System.out.println(listEquals(list1, list2));
        list2.add("2");
        list2.add("1");
        System.out.println(listEquals(list1, list2));
        list1.add("5");
        list1.add("3");
        System.out.println(list1);
        list1 = list1.stream().sorted().collect(Collectors.toList());
        System.out.println(list1);
        list1 = list1.stream().sorted((e1, e2) -> (e1.compareTo(e2))).collect(Collectors.toList());
        System.out.println(list1);
        User user = new User();
        System.out.println(user.getBool() == Boolean.TRUE);

    }


}

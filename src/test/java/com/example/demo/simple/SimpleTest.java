package com.example.demo.simple;

import com.example.demo.model.entity.simple.BigDecimalEntity;
import com.example.demo.model.entity.simple.ConfigEntity;
import com.example.demo.model.entity.simple.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-18 11:02
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleTest {

    @Test
    public void test(){
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setStr("1");
        User user2 = new User();
        user2.setStr("2");
        User user3 = new User();
        user3.setStr("3");
        users.add(user1);
        users.add(user2);
        users.add(user3);
        System.out.println(users);
        change(users);
        System.out.println(users);
    }
    private void change(List<User> users){
        users.get(0).setStr("change 1");
        users.get(1).setStr("change 2");
        users.get(2).setStr("change 3");
    }

    @Test
    public void test1(){
        System.out.println(new Date().getTime());
        System.out.println(System.currentTimeMillis());
        System.out.println(new Date().getTime()==System.currentTimeMillis());
    }

    private ConfigEntity configEntity = new ConfigEntity();

    @Test
    public void test2(){
        System.out.println(configEntity);
        //撒币想要注入纯实体
//        ConfigEntity configEntity = SpringContextUtils.getBean(ConfigEntity.class);
//        System.out.println(configEntity);
    }

    @Test
    public void testSubList(){
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setStr("asd");
        list.add(user);
        List<User> sublist = list.subList(0,1);
        System.out.println(sublist);
        sublist.get(0).setStr("qwe");
        System.out.println(sublist);
        System.out.println(list);
        List<User> itList = new ArrayList<>();
        itList.add(list.get(0));
        itList.get(0).setStr("zxc");
        System.out.println(itList);
        System.out.println(list);
    }

    @Test
    public void streamSortLambdaTest(){
        List<BigDecimalEntity> list = new ArrayList<>();
        list.add(new BigDecimalEntity(new BigDecimal(1), new BigDecimal(2)));
        list.add(new BigDecimalEntity(new BigDecimal(2), new BigDecimal(2)));
        list.add(new BigDecimalEntity(new BigDecimal(3), new BigDecimal(2)));
        list.add(new BigDecimalEntity(new BigDecimal(4), new BigDecimal(2)));
        list.add(new BigDecimalEntity(new BigDecimal(-1), new BigDecimal(2)));
        list.add(new BigDecimalEntity(new BigDecimal(-2), new BigDecimal(2)));
        list.add(new BigDecimalEntity(new BigDecimal(-3), new BigDecimal(2)));
        //从小到大排序排序，解释：p1为前者，p2为后者，前后对比大小，默认小到大
        List<BigDecimalEntity> listOrdered = list.stream().sorted((p1, p2) -> (p1.getTest1().subtract(p2.getTest1()).intValue()))
                .collect(Collectors.toList());
        for (BigDecimalEntity b : listOrdered){
            System.out.println(b);
        }
        System.out.println("****************************");
        //从大到小排序排序，解释：p1为前者，p2为后者，前后对比大小，默认小到大，取反值则从大到小
        listOrdered = list.stream().sorted((p1, p2) -> (p2.getTest1().subtract(p1.getTest1()).intValue()))
                .collect(Collectors.toList());
        for (BigDecimalEntity b : listOrdered){
            System.out.println(b);
        }
        System.out.println("****************************");
        //从大到小排序排序，解释：p2为前者，p1为后者，前后对比大小，默认小到大，但前后顺序相反则打印为从大到小
        listOrdered = list.stream().sorted((p2, p1) -> (p1.getTest1().subtract(p2.getTest1()).intValue()))
                .collect(Collectors.toList());
        for (BigDecimalEntity b : listOrdered){
            System.out.println(b);
        }
    }

    @Test
    public void stramFiltterLambdaTest(){
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        integers.add(5);
        //拦截小于3的部分
        List<Integer> filters = integers.stream().filter(e -> e < 3)
                .collect(Collectors.toList());
        for (Integer i : filters){
            System.out.println(i);
        }
    }


}

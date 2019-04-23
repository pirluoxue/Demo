package com.example.demo.model.entity.decorate;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-23 10:06
 **/
public class TestGirls {


    public static void main(String[] args) {

        //创建一个初始女性
        Girls girls = new SuccessGirls();
        //装饰一个“美貌”
        girls = new BeautifulGirls(girls);
        //装饰一个”智慧“
        girls = new SmartGirls(girls);
        //装饰一个"特点"
        girls = new FantasticGirls(girls);

        girls.success();
    }



}

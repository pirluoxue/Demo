package com.example.demo.model.entity.decorate;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-23 10:03
 **/
public class BeautifulGirls extends GirlsDeorate {
    public BeautifulGirls(Girls girls) {
        super(girls);
    }

    public void getBeautiful(){
        System.out.println("获得了美貌");
    }

    @Override
    public void success(){
        getBeautiful();
//        super.success();
        getBeautiful();
//        super.success();
    }
}

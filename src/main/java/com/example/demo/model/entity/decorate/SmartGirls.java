package com.example.demo.model.entity.decorate;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-23 09:59
 **/
public class SmartGirls extends GirlsDeorate {

    public SmartGirls(Girls girls) {
        super(girls);
    }

    public void getIntelligent(){
        System.out.println("获得了智慧");
    }

    @Override
    public void success(){
//        super.success();
        getIntelligent();
        getIntelligent();
//        super.success();
    }

}

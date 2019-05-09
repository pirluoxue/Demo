package com.example.demo.model.entity.decorate;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-23 10:23
 **/
public class FantasticGirls extends GirlsDeorate {
    public FantasticGirls(Girls girls) {
        super(girls);
    }

    public void getCharacteristic(){
        System.out.println("获得了特点");
    }

    @Override
    public void success(){
        super.success();
        getCharacteristic();
        getCharacteristic();
//        super.success();
    }
}

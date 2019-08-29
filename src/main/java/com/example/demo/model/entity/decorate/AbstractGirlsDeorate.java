package com.example.demo.model.entity.decorate;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-23 09:57
 **/
public abstract class AbstractGirlsDeorate implements Girls {

    private Girls girls;
    public AbstractGirlsDeorate(Girls girls){
        this.girls = girls;
    }

    @Override
    public void success(){
        girls.success();
    }

}

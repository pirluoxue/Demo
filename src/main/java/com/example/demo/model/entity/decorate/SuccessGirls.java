package com.example.demo.model.entity.decorate;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-23 10:02
 **/
public class SuccessGirls implements Girls {
    @Override
    public void success() {
        System.out.println("成功的女性");
    }
}

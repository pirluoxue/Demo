package com.example.demo.model.entity.simple;

import lombok.Data;

/**
 * @author chen_bq
 * @description
 * @create: 2019-02-23 15:22
 **/
@Data
public class SuperUser {

    private String str;

    private String nothing;

    public void test(){
        System.out.println("SuperUser");
        str = "SuperUser";
    }

}

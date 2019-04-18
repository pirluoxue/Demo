package com.example.demo.model.entity.simple;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-18 15:21
 **/
@Data
public class SimpleEntity {

    private static SimpleEntity simpleEntity;

    private Map map;

    public static synchronized SimpleEntity getInstance(){
        if(simpleEntity == null){
            simpleEntity = new SimpleEntity();
            simpleEntity.setMap(new HashMap());
        }
        return simpleEntity;
    }
}

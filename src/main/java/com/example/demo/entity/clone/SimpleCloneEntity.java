package com.example.demo.entity.clone;

import lombok.Data;
import org.springframework.context.annotation.Bean;

/**
 * @author chen_bq
 * @description 一个用于测试clone来new实体的效率
 * @create: 2019-04-01 09:55
 **/
@Data
public class SimpleCloneEntity implements Cloneable{

    private String str;

    private static SimpleCloneEntity baseEntity = new SimpleCloneEntity();


    public static SimpleCloneEntity getSimpleCloneEntity() {
        try {
            return baseEntity.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new SimpleCloneEntity();
    }

    @Override
    protected SimpleCloneEntity clone() throws CloneNotSupportedException {
        return (SimpleCloneEntity) super.clone();
    }


    public static SimpleCloneEntity getInstance(){
        return baseEntity;
    }

}

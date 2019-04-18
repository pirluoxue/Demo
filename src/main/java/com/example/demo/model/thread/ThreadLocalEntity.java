package com.example.demo.model.thread;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-09 14:31
 **/
@Data
public class ThreadLocalEntity {

    public Timestamp createTime;
    public String session;

    public ThreadLocalEntity(){
        this.createTime = new Timestamp(System.currentTimeMillis());
    }

}

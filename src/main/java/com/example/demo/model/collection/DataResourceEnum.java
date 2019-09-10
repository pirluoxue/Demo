package com.example.demo.model.collection;

import com.example.demo.util.DataResourceUtil;

import java.sql.Connection;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-27 14:02
 **/

public enum DataResourceEnum {

//    DATA_RESOURCE(DataResourceUtil.getInstance().getConnection("jdbc1").getConn()),
    DATA_TARGET(DataResourceUtil.getInstance().getConnection("jdbc2").getConn());

    private Connection conn;

    DataResourceEnum(Connection conn){
        this.conn = conn;
    }

    public Connection getConn(){
        if (this == null){
            return null;
        }
        return this.conn;
    }


}

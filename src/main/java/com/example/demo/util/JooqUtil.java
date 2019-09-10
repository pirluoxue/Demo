package com.example.demo.util;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.sql.Connection;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-23 16:38
 **/
public class JooqUtil {

    public static DSLContext getCreate(Connection connection){
        if (connection == null){
            return null;
        }
        DSLContext resource = DSL.using(connection);
        return resource;
    }


}

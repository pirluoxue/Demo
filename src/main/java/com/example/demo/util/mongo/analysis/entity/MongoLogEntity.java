package com.example.demo.util.mongo.analysis.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author chen_bq
 * @description
 * @create: 2019/10/16 10:55
 **/
@Data
public class MongoLogEntity {

    private String logDateTime;
    private String logMessage;
    private List<String> characteristic;
    private Map locks;
    private String collectionName;
    private String commandTypeDescrition;

    private String result;
    private int operationTime;
    private int numYields;
    private int reslen;

}

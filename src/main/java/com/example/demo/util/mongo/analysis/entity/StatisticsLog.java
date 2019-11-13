package com.example.demo.util.mongo.analysis.entity;

import lombok.Data;

/**
 * @author chen_bq
 * @description 统计日志实体
 * @create: 2019/10/17 16:39
 **/
@Data
public class StatisticsLog extends MongoLogEntity {

    private int useTimes;
    private int maxOperationTime;
    private int avgOperationTime;
    private int maxNumYields;
    private int avgNumYields;
    private int avgReslen;
    private int maxReslen;


}

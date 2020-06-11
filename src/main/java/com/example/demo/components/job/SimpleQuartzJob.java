package com.example.demo.components.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author chen_bq
 * @description 简单通过quartz的方式
 * @create: 2020/3/4 9:45
 **/
public class SimpleQuartzJob extends QuartzJobBean {

    @Autowired
    private Scheduler scheduler;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("SimpleQuartzJob " + new Timestamp(System.currentTimeMillis()));
    }
}

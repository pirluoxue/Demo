package com.example.demo.components.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author chen_bq
 * @description spring自带的触发器
 * @create: 2020/3/3 17:41
 **/
@Component
public class SimpleJob {

    @Scheduled(cron="0/5 * * * * ?")
    public void testJob(){
        System.out.println("testJob " + new Date());
    }


}

package com.example.demo.configuration;

import com.example.demo.components.job.SimpleQuartzJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chen_bq
 * @description quartz配置
 * 或者说是quartz的注册
 * @create: 2020/3/3 17:48
 **/
@Configuration
public class QuartzConfig {


    // 使用jobDetail包装job
    @Bean
    public JobDetail myJobDetail() {
        return JobBuilder.newJob(SimpleQuartzJob.class).withIdentity("simpleQuartzJob").storeDurably().build();
    }

    // 把jobDetail注册到trigger上去
    @Bean
    public Trigger myJobTrigger() {
        // 这是对象的方式跑配置执行周期
//        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//            .withIntervalInSeconds(15).repeatForever();
        // cron配置方式
        CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule("0/1 * * * * ?");
        return TriggerBuilder.newTrigger()
            .forJob(myJobDetail())
            .withIdentity("simpleQuartzJob_Trigger")
            .withSchedule(cron)
            .build();
    }




}

package com.example.demo.components.annotation;

import com.example.demo.model.entity.common.Description;
import com.example.demo.model.entity.common.LogType;

import java.lang.annotation.*;

/**
 * @author chen_bq
 * @description 面向切面的日志注解
 * @create: 2019/9/27 9:30
 **/
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {

    LogType logtype() default LogType.CommonType;

    Description description();

}

package com.example.demo.components.annotation;

/**
 * @author chen_bq
 * @description
 * @create: 2019-03-13 14:04
 **/

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestParam {

    String value() default "";

//    boolean required() default true;

//    String defaultValue() default "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";
}

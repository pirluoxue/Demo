package com.example.demo.service.Impl;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;

@Component
public class SparkServiceImpl {

    private static final String MIE = "mie";

    @Autowired
    private JavaSparkContext javaSparkContext;

    public String test(){
        JavaRDD<String> lines = javaSparkContext.textFile("D:\\test\\sparkTest.txt").cache();
        JavaRDD<String> mies = lines.filter(n -> n.contains(MIE));
        System.out.println("=================");
        System.out.println(mies.collect());
        System.out.println(mies.count());
        System.out.println("=================");
        return mies.count() + "";
    }


}

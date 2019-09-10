package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author chen_bq
 * @description
 * @create: 2019-03-21 15:50
 **/
@Controller
public class TestFreemarker {

    @RequestMapping("testFreemarker")
    public String testFreemarker(){
        return "test";
    }

    @RequestMapping("test/freemarker/https")
    public String testFreemarkerHttps(){
        return "test";
    }


}

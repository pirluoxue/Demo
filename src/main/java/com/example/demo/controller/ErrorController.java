package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Classname ErrorController
 * @Description 错误页跳转
 * @Date 2019-05-31
 * @Created by chen_bq
 */
@Controller
public class ErrorController {

    @RequestMapping(value = "/error400Page")
    public String error400Page() {
        return "404";
    }
    @RequestMapping(value = "/error401Page")
    public String error401Page() {
        return "401";
    }
    @RequestMapping(value = "/error404Page")
    public String error404Page(Model model) {
        model.addAttribute("code","6666666");
        model.addAttribute("msg","服务器降级中......");
        return "404";
    }
    @RequestMapping(value = "/error500Page")
    public String error500Page(Model model) {
        return "500";
    }


}

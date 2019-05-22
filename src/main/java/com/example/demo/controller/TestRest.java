package com.example.demo.controller;

import com.example.demo.model.entity.jooq.tables.pojos.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Classname TestRest
 * @Description 一个专用于测试连接相关的控制类
 * @Date 2019-05-10
 * @Created by chen_bq
 */
@RestController
public class TestRest {

//    @RequestMapping("test/rest")
//    public String testRest(@RequestParam Map<String, Object> params) throws IOException {
//        System.out.println(params);
//        return "hello world";
//    }
    @RequestMapping("test/rest")
    public String testRest(HttpServletRequest request) throws IOException {
//        Map<String, String[]> map = request.getParameterMap();
//        String[] simpleEntity = map.get("SimpleEntity(map");
//        System.out.println(simpleEntity[0]);
        /*仅能选择一种方法获取HttpServletRequest内的参数，哪怕使用getParameterMap也会清空请求参数*/
        printlnParam(request);
        return "hello world";
    }

    private void printlnParam(HttpServletRequest request) throws IOException {
        //获得输入流
        ServletInputStream servletInputStream = request.getInputStream();
        //创建StringBuilder暂存信息
        StringBuilder content = new StringBuilder();
        byte[] b = new byte[1024];
        int lens = -1;
        //读入流
        while ((lens = servletInputStream.read(b)) > 0) {
            //写入StringBuilder
            content.append(new String(b, 0, lens));
        }
        String strcont = content.toString();// 内容
        System.out.println(strcont);
    }

    @RequestMapping(value = "test/restpost", method = RequestMethod.POST)
    public String restpost(@RequestBody User user){
        return user.toString();
    }


}

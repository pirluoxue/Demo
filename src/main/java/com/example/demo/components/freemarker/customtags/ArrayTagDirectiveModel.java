package com.example.demo.components.freemarker.customtags;

import freemarker.core.Environment;
import freemarker.template.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/**
 * @author chen_bq
 * @description
 * @create: 2019-03-21 15:48
 * TemplateDirectiveModel 定义<></>的标签
 **/
@Slf4j
public class ArrayTagDirectiveModel implements TemplateDirectiveModel {

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels, TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        log.info("Environment: " + environment);
        log.info("map: " + map);
        //DefaultObjectWrapperBuilder版本设置，为了转化成TemplateModel类型
        Version version = new Version(2,3,22);
        if(map.get("name") != null){
            String str = map.get("name").toString();
            if(!"喵喵喵".equalsIgnoreCase(str)){
                map.remove("act");
                map.put("act", "除了喵，都不能动");
            }
            environment.setVariable("paramList", new DefaultObjectWrapperBuilder(version).build().wrap(map));
        }
        //直接放出
        if(templateDirectiveBody != null){
            templateDirectiveBody.render(environment.getOut());
        }
    }
}

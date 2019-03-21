package com.example.demo.components.freemarker.customtags;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;

/**
 * @author chen_bq
 * @description
 * @create: 2019-03-21 16:09
 **/
public class SimpleSub implements TemplateMethodModelEx{
    @Override
    public Object exec(List args) throws TemplateModelException {
        if(args.size() <= 2){
            throw new RuntimeException("自定义标签 SimpleSub 参数异常");
        }
        String str = args.get(0).toString();
        return str.substring(Integer.parseInt(args.get(1).toString()), Integer.parseInt(args.get(2).toString()));
    }
}

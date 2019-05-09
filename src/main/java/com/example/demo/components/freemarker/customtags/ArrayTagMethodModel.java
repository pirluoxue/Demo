package com.example.demo.components.freemarker.customtags;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;

/**
 * @author chen_bq
 * @description
 * TemplateMethodModelEx 定义${}的标签
 * TemplateDirectiveModel 定义<></>的标签
 * @create: 2019-03-21 16:44
 **/
public class ArrayTagMethodModel implements TemplateMethodModelEx {
    @Override
    public Object exec(List list) throws TemplateModelException {
        return "this is First ArrayTagMethod";
    }
}

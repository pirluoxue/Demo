package com.example.demo.components.freemarker.customtags;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.util.List;

/**
 * @author chen_bq
 * @description
 * @create: 2019-03-21 16:06
 **/
public class SimpleTag implements TemplateMethodModelEx {
    @Override
    public Object exec(List list) throws TemplateModelException {
        return "this is test SimpleTag";
    }
}

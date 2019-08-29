package com.example.demo.components.freemarker;

import com.example.demo.components.freemarker.customtags.ArrayTag;
import com.example.demo.components.freemarker.customtags.ArrayTagDirectiveModel;
import com.example.demo.components.freemarker.customtags.ArrayTagMethodModel;

/**
 * @author chen_bq
 * @description
 * @create: 2019-03-21 15:43
 **/
public class CustomTags extends ArrayTag {

    public CustomTags() {
        this.put("ArrayTagMethodModel", new ArrayTagMethodModel());
        this.put("ArrayTagDirectiveModel", new ArrayTagDirectiveModel());
    }

}

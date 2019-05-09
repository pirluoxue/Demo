package com.example.demo.configuration;

import com.example.demo.components.freemarker.CustomTags;
import com.example.demo.components.freemarker.customtags.SimpleSub;
import com.example.demo.components.freemarker.customtags.SimpleTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class FreeMarkerConfig {

    @Autowired
    private freemarker.template.Configuration configuration;
    
    
	@PostConstruct
    public void setConfigure() throws Exception {
		configuration.setAPIBuiltinEnabled(false);
		configuration.setDefaultEncoding("UTF-8");
        //configuration.setSharedVariable("static_server", "//www.dev.pangugle.com/static");

//		configuration.setSharedVariable("shiro", new MyShiroTags());

        configuration.setSharedVariable("SimpleTag", new SimpleTag());
        configuration.setSharedVariable("SimpleSub", new SimpleSub());
        configuration.setSharedVariable("ArrayTags", new CustomTags());



    }



}

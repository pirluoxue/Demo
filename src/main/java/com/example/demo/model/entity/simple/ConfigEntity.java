package com.example.demo.model.entity.simple;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-19 17:15
 **/
@Data
public class ConfigEntity {

    //这样赋值，new的时候还是空
    @Value("spring.redis.host")
    private String redisHost;

//    public ConfigEntity() {
//        Yaml yaml = new Yaml();
//        URL url = this.getClass().getClassLoader().getResource("application.yml");
//        if (url != null) {
//            //获取test.yaml文件中的配置数据，然后转换为obj，
//            Map map = null;
//            try {
//                map = (Map) yaml.load(new FileInputStream(url.getFile()));
//                Map spring = (Map) map.get("spring");
//                Map redis = (Map) spring.get("redis");
//                this.redisHost = redis.get("host") + "";
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }


}

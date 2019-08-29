package com.example.demo.util;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-23 17:06
 **/
public class DataResourceUtil {

    private static Map<String, ConnectionUtil> map = new HashMap<>();

    private static DataResourceUtil instance;

    public static DataResourceUtil getInstance(){
        if(instance == null){
            instance = new DataResourceUtil();
        }
        return instance;
    }

    public ConnectionUtil getConnection(String key){
        if (map == null || map.isEmpty()){
            return null;
        }
        return map.get(key);
    }

    DataResourceUtil() {
        Yaml yaml = new Yaml();
        URL url = this.getClass().getClassLoader().getResource("application.yml");
        try {
            if (url != null) {
                //获取application.yaml文件中的配置数据，然后转换为obj，
                Map map = null;
                map = (Map) yaml.load(new FileInputStream(url.getFile()));
                Set keys = map.keySet();
                Iterator iterator = keys.iterator();
                while (iterator.hasNext()){
                    String key = iterator.next() + "";
                    if(key.startsWith("jdbc")){
                        Map jdbc = (Map) map.get(key);
                        setJdbc(key, jdbc);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void setJdbc(String key, Map jdbc){
        String driverClassName = jdbc.get("driverClassName") + "";
        String url = jdbc.get("url") + "";
        String username = jdbc.get("username") + "";
        String password = jdbc.get("password") + "";
        ConnectionUtil connectionUtil = new ConnectionUtil(driverClassName, url, username, password);
        map.put(key, connectionUtil);
    }

}

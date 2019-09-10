package com.example.demo.bean;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * @author chen_bq
 * @description
 * @create: 2019/9/2 10:22
 **/
@Configuration
public class HttpsBean {

    @Value("${server.ssl.key-alias}")
    private String key_alias;
    @Value("${server.ssl.key-store}")
    private String key_store;
    @Value("${server.ssl.key-password}")
    private String key_password;
    @Value("${server.ssl.key-store-type}")
    private String key_store_type;

    @Bean
    public Connector connector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        // 申明http/https
        connector.setScheme("https");
        // 端口号
        connector.setPort(8089);
        // 是否启用secure 若为true则需要配置secure相关信息，否则无法启动
        connector.setSecure(false);
        try {
            Http11NioProtocol protocol = (Http11NioProtocol)connector.getProtocolHandler();
            // 是否启用ssl
            protocol.setSSLEnabled(true);
            // 读取ssl文件，根为resource
            File file = new ClassPathResource(key_store).getFile();
            protocol.setKeystoreFile(file.getAbsolutePath());
            protocol.setKeyAlias(key_alias);
            protocol.setKeyPass(key_password);
            protocol.setKeystorePass(key_password);
            protocol.setKeystoreType(key_store_type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connector;
    }

    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(Connector connector) {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
                // 拦截路由，仅有该配置连接可以访问
                collection.addPattern("/test/freemarker/https");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(connector);
        return tomcat;
    }


}

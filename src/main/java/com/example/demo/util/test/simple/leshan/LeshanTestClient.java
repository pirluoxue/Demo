package com.example.demo.util.test.simple.leshan;

import com.example.demo.util.test.simple.leshan.object.ConnectivityStatistics;
import org.eclipse.leshan.LwM2mId;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.LeshanClientBuilder;
import org.eclipse.leshan.client.object.Device;
import org.eclipse.leshan.client.object.Security;
import org.eclipse.leshan.client.object.Server;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.request.BindingMode;

/**
 * @author chen_bq
 * @description
 * @create: 2019/9/12 14:16
 **/
public class LeshanTestClient {

    public static void main(String[] args) {
        String endpoint = "TEST_ASD" ; // choose an endpoint name
        LeshanClientBuilder builder = new LeshanClientBuilder(endpoint);
        // 初始化配置
        ObjectsInitializer initializer = new ObjectsInitializer();
        // 连接地址
        initializer.setInstancesForObject(LwM2mId.SECURITY, Security.noSec("coap://127.0.0.1:5683", 12345));
//        initializer.setInstancesForObject(LwM2mId.SECURITY, Security.noSec("coap://leshan.eclipseprojects.io:5683", 12345));
        initializer.setInstancesForObject(LwM2mId.SERVER, new Server(12345, 5 * 60, BindingMode.U, false));
        initializer.setInstancesForObject(LwM2mId.DEVICE, new Device("Eclipse Leshan", "model12345", "12345", "U"));
        initializer.setInstancesForObject(LwM2mId.CONNECTIVITY_STATISTICS, new ConnectivityStatistics());
        builder.setObjects(initializer.createAll());

        LeshanClient client = builder.build();
        client.start();
    }

}

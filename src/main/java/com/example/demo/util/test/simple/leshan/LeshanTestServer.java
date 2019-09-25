package com.example.demo.util.test.simple.leshan;

import com.example.demo.util.test.simple.leshan.listener.Lwm2mObserveListener;
import com.example.demo.util.test.simple.leshan.listener.Lwm2mRegistrationListener;
import org.eclipse.leshan.core.request.ContentFormat;
import org.eclipse.leshan.core.request.ReadRequest;
import org.eclipse.leshan.server.californium.LeshanServerBuilder;
import org.eclipse.leshan.server.californium.impl.LeshanServer;

/**
 * @author chen_bq
 * @description
 * @create: 2019/9/12 14:15
 **/
public class LeshanTestServer {

    public static void main(String[] args) {
        LeshanServerBuilder builder = new LeshanServerBuilder();
        LeshanServer server = builder.build();
        server.getRegistrationService().addListener(new Lwm2mRegistrationListener());
        server.getObservationService().addListener(new Lwm2mObserveListener());
        server.start();
        while (true) {
            try {
                Thread.sleep(2000);
                server.send(server.getRegistrationService().getByEndpoint("TEST_ASD")
                    , new ReadRequest(ContentFormat.TLV, "coap://127.0.0.1:5683"));
            } catch (InterruptedException e) {
                continue;
            }
        }
    }

}

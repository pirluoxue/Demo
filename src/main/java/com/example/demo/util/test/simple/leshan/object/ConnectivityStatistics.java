package com.example.demo.util.test.simple.leshan.object;

import org.eclipse.leshan.client.request.ServerIdentity;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ReadResponse;

/**
 * @author chen_bq
 * @description
 * @create: 2019/9/12 16:33
 **/
public class ConnectivityStatistics extends BaseInstanceEnabler {

    @Override
    public ReadResponse read(ServerIdentity identity) {
        System.out.println(identity.getRole());
        return ReadResponse.notFound();
    }

}

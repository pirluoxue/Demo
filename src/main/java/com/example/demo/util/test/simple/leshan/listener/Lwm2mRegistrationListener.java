package com.example.demo.util.test.simple.leshan.listener;

import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.server.registration.Registration;
import org.eclipse.leshan.server.registration.RegistrationListener;
import org.eclipse.leshan.server.registration.RegistrationUpdate;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author chen_bq
 * @description 客户端注册监听
 * @create: 2019/9/12 14:23
 **/
public class Lwm2mRegistrationListener implements RegistrationListener {
    @Override
    public void registered(Registration registration, Registration registration1, Collection<Observation> collection) {
        System.out.println("喵喵喵：" + "registered to my leshan Server " + LocalDateTime.now());
    }

    @Override
    public void updated(RegistrationUpdate registrationUpdate, Registration registration, Registration registration1) {
        System.out.println("喵喵喵：" + "updated client " + LocalDateTime.now());
    }

    @Override
    public void unregistered(Registration registration, Collection<Observation> collection, boolean b, Registration registration1) {
        System.out.println("喵喵喵：" + "unregistered by my leshan Server " + LocalDateTime.now());
    }
}

package com.example.demo.util.test.simple.leshan.listener;

import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.response.ObserveResponse;
import org.eclipse.leshan.server.observation.ObservationListener;
import org.eclipse.leshan.server.registration.Registration;

/**
 * @author chen_bq
 * @description
 * @create: 2019/9/12 17:42
 **/
public class Lwm2mObserveListener implements ObservationListener {

    @Override
    public void newObservation(Observation observation, Registration registration) {
        System.out.println("newObservation observation " + observation.getPath());
    }

    @Override
    public void cancelled(Observation observation) {
        System.out.println("cancelled observation " + observation.getPath());
    }

    @Override
    public void onResponse(Observation observation, Registration registration, ObserveResponse observeResponse) {
        System.out.println("onResponse observation " + observation.getPath());
    }

    @Override
    public void onError(Observation observation, Registration registration, Exception e) {
        System.out.println("onError observation " + observation.getPath());
    }
}

package com.example.demo.service.abc;

import org.springframework.stereotype.Service;

@Service
public class ExtendAbstractService2 extends AbstractService {

    ExtendAbstractService2(){
        System.out.println("this is ExtendAbstractService 2 init");
    }

    @Override
    public String getString() {
        return "this is ExtendAbstractService 2";
    }
}

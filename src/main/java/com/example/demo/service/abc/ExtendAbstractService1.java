package com.example.demo.service.abc;

import org.springframework.stereotype.Service;

@Service
public class ExtendAbstractService1 extends AbstractService {

    ExtendAbstractService1(){
        System.out.println("this is ExtendAbstractService 1 init");
    }
    @Override
    public String getString() {
        return "this is ExtendAbstractService 1";
    }

}

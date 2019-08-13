package com.example.demo.service.abc;


import org.springframework.stereotype.Service;

@Service
public class ExtendAbstractService3 extends AbstractService {

    ExtendAbstractService3(){
        System.out.println("this is ExtendAbstractService 3 init");
    }

    @Override
    public String getString() {
        return "this is ExtendAbstractService 3";
    }
}

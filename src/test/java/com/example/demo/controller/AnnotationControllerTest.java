package com.example.demo.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

public class AnnotationControllerTest {

    @Autowired
    private RestTemplate restTemplate;
    public String URL = "http://localhost:8088/test";
    @Test
    public void testAnnotation(){
        String rs = restTemplate.getForObject(URL, String.class);
        System.out.println(rs);
    }

}
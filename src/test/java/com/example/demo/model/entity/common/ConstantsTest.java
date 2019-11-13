package com.example.demo.model.entity.common;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class ConstantsTest {

    @Test
    public void getFunProperty() {
        System.out.println(Constants.funProperty);
        System.out.println(Constants.funProperty);
        System.out.println(Constants.funProperty);
        System.out.println(Constants.funProperty);
    }

    @Test
    public void test() throws ParseException {
        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd");
        System.out.println(sdf1.format(date));
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(sdf2.format(date));
        System.out.println("- - - - - -");
    }
}
package com.example.demo.util.toy;

import com.example.demo.model.entity.enums.LogCode;
import com.example.demo.model.entity.simple.MatchInfo;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class RecursionUtilsTest {

    @Test
    public void listMatchEnunsByEnumTest() {
//        String path = "D:\\worksplace\\java\\Demo\\src\\main\\java\\com\\example\\demo\\util";
        String path = "D:\\worksplace\\java\\Demo\\src\\main\\java\\com\\example\\demo\\model";
        List<MatchInfo> list = RecursionUtils.listMatchEnunsByEnum(LogCode.class, path);
        for (MatchInfo matchInfo : list){
            System.out.println(matchInfo);
        }
    }
}
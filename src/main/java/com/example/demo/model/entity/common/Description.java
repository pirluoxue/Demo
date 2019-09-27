package com.example.demo.model.entity.common;

/**
 * @Author chen_bq
 * @Description 描述
 * @Date 2019/9/27 14:03
 */
public enum Description {

    TEST_GET_PARAM(1, "testGetParam", "test get param"),
    TEST_POST_PARAM(2, "testPostParam", "test post param");

    final int id;
    final String restName;
    final String restOperation;

    Description(int id, String restName, String restOperation) {
        this.id = id;
        this.restName = restName;
        this.restOperation = restOperation;
    }


}

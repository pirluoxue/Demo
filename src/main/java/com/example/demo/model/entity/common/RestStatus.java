package com.example.demo.model.entity.common;

import java.io.Serializable;

/**
 * @author chen_bq
 * @description
 * @create: 2019-01-14 17:37
 **/
public interface RestStatus extends Serializable {

    int code();

    String message();
}

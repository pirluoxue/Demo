package com.example.demo.model.entity.simple;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author chen_bq
 * @description 用于测试post请求的参数体
 * @create: 2019/9/25 16:40
 **/
@Data
public class TestHttpPostEntity implements Serializable {

    private static final long serialVersionUID = 1234567890L;

    @NotNull
    private String name;
    private Timestamp createTime;

    @JSONField(serialize = false)
    private String ignoreStr;

}

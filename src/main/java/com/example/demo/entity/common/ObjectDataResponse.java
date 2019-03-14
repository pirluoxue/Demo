package com.example.demo.entity.common;

import lombok.*;

/**
 * @author chen_bq
 * @description 封装包
 * @create: 2019-01-14 17:19
 **/
@Data
@ToString(callSuper = true)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ObjectDataResponse<T> {

    private static final long serialVersionUID = 1562741318988223256L;


    private T data;

//    @ApiModelProperty(value = "平台状态码", example = "20000", required = true)
    @Builder.Default()
    private int code = 20000;

//    @ApiModelProperty(value = "返回提示", example = "20000", required = true)
    @Builder.Default()
    private String msg = "请求成功";

}

package com.example.demo.model.entity.easy.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author chen_bq
 * @description 适用于easyExcel的实体对象
 * @create: 2020/2/25 17:28
 **/
@Data
public class ExcelDemo {

    @ExcelProperty("id 标题")
    private Long id;
    @ExcelProperty("userName 标题")
    private String userName;
    @ExcelProperty("userCode 标题")
    private String userCode;
    @ExcelProperty("remark 标题")
    private String remark;
    @ExcelProperty("email 标题")
    private String email;
    @ExcelProperty("company 标题")
    private String company;
    @ExcelProperty("country 标题")
    private String country;
    @ExcelProperty("tel 标题")
    private String tel;
    @ExcelProperty("createTime 标题")
    private Date createTime;
    @ExcelProperty("updateTime 标题")
    private Date updateTime;
    private Integer enable;

    @ExcelIgnore
    private String ignore;


}

package com.example.demo.model.entity.common;

/**
 * @Author chen_bq
 * @Description 日志类型
 * @Date 2019/9/27 14:00
 */
public enum LogType {

    LoginType(0,"登录日志"),// 用于记录登录登出日志类型
    ServiceType(1,"服务日志"),// 用于记录对外接口日志类型
    CommonType(2,"通用类型"),// 用于记录通用日志类型,即对数据的增删改查
    ExceptionType(3,"异常类型");// 记录系统各种异常

    final int id;
    final String name;//名称

    private LogType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}

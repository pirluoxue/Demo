package com.example.demo.model.entity.jooq.form;

import com.example.demo.model.entity.jooq.tables.pojos.User;
import lombok.Data;

/**
 * User 实体表单类
 * Created by CoderMaker on 2019/04/27.
 */
@Data
public class UserForm extends User {
    private String user_bindDate_start; //  关注日期_开始时间
    private String user_bindDate_end; //  关注日期_结束时间
    private String user_birth_start; //  出生日期_开始时间
    private String user_birth_end; //  出生日期_结束时间
    private String user_loginDate_start; //  最后登录日期_开始时间
    private String user_loginDate_end; //  最后登录日期_结束时间
    private String user_joinTime_start; //  加入时间_开始时间
    private String user_joinTime_end; //  加入时间_结束时间
    private String user_createTime_start; //  创建时间_开始时间
    private String user_createTime_end; //  创建时间_结束时间
    private String user_updateTime_start; //  更新时间_开始时间
    private String user_updateTime_end; //  更新时间_结束时间
}


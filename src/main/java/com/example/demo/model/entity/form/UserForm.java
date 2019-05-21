package com.example.demo.model.entity.form;

import com.example.demo.model.entity.jooq.tables.pojos.User;
import lombok.Data;

/**
 * User 实体表单类
 * Created by CoderMaker on 2019/05/14.
 */
@Data
public class UserForm extends User {

    public static String STSTUS_TEMP = "TEMP";//暂存，协议未生效过
    public static String STSTUS_NORMAL = "NORMAL";//正常
    public static String STSTUS_STOP = "STOP";//暂停

    public static Integer ENABLE = 1;
    public static Integer UNENABLE = 0;


}


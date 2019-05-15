package com.example.demo.model.entity.form;

import com.example.demo.model.entity.jooq.tables.pojos.Order;
import lombok.Data;

/**
 * Order 实体表单类
 * Created by CoderMaker on 2019/05/15.
 */
@Data
public class OrderForm extends Order {
    private String gmtPayment_start; //  交易支付时间_开始时间
    private String gmtPayment_end; //  交易支付时间_结束时间
}


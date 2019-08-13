package com.example.demo.model.entity.simple;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author chen_bq
 * @description
 * @create: 2019-03-07 09:48
 **/
@Data
@NoArgsConstructor
public class BigDecimalEntity {

    private BigDecimal test1;
    private BigDecimal test2;

    public BigDecimalEntity(BigDecimal test1, BigDecimal test2){
        this.test1 = test1;
        this.test2 = test2;
    }

}

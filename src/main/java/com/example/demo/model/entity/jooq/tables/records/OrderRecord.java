/*
 * This file is generated by jOOQ.
 */
package com.example.demo.model.entity.jooq.tables.records;


import com.example.demo.model.entity.jooq.tables.Order;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record13;
import org.jooq.Row13;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * 订单表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OrderRecord extends UpdatableRecordImpl<OrderRecord> implements Record13<Integer, String, BigDecimal, String, String, String, BigDecimal, Timestamp, String, BigDecimal, String, String, Integer> {

    private static final long serialVersionUID = 642395831;

    /**
     * Setter for <code>opentogetdb.order.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>opentogetdb.order.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>opentogetdb.order.outTradeNo</code>. 订单号
     */
    public void setOuttradeno(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>opentogetdb.order.outTradeNo</code>. 订单号
     */
    @NotNull
    @Size(max = 128)
    public String getOuttradeno() {
        return (String) get(1);
    }

    /**
     * Setter for <code>opentogetdb.order.totalAmount</code>. 订单金额
     */
    public void setTotalamount(BigDecimal value) {
        set(2, value);
    }

    /**
     * Getter for <code>opentogetdb.order.totalAmount</code>. 订单金额
     */
    public BigDecimal getTotalamount() {
        return (BigDecimal) get(2);
    }

    /**
     * Setter for <code>opentogetdb.order.tradeNo</code>. 支付宝订单号
     */
    public void setTradeno(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>opentogetdb.order.tradeNo</code>. 支付宝订单号
     */
    @Size(max = 64)
    public String getTradeno() {
        return (String) get(3);
    }

    /**
     * Setter for <code>opentogetdb.order.buyerLogonId</code>. 买家支付宝账号
     */
    public void setBuyerlogonid(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>opentogetdb.order.buyerLogonId</code>. 买家支付宝账号
     */
    @Size(max = 100)
    public String getBuyerlogonid() {
        return (String) get(4);
    }

    /**
     * Setter for <code>opentogetdb.order.receiptAmount</code>. 实收金额
     */
    public void setReceiptamount(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>opentogetdb.order.receiptAmount</code>. 实收金额
     */
    @Size(max = 11)
    public String getReceiptamount() {
        return (String) get(5);
    }

    /**
     * Setter for <code>opentogetdb.order.buyerPayAmount</code>. 买家付款的金额
     */
    public void setBuyerpayamount(BigDecimal value) {
        set(6, value);
    }

    /**
     * Getter for <code>opentogetdb.order.buyerPayAmount</code>. 买家付款的金额
     */
    public BigDecimal getBuyerpayamount() {
        return (BigDecimal) get(6);
    }

    /**
     * Setter for <code>opentogetdb.order.gmtPayment</code>. 交易支付时间
     */
    public void setGmtpayment(Timestamp value) {
        set(7, value);
    }

    /**
     * Getter for <code>opentogetdb.order.gmtPayment</code>. 交易支付时间
     */
    public Timestamp getGmtpayment() {
        return (Timestamp) get(7);
    }

    /**
     * Setter for <code>opentogetdb.order.fundChannel</code>. 交易使用的资金渠道
     */
    public void setFundchannel(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>opentogetdb.order.fundChannel</code>. 交易使用的资金渠道
     */
    @Size(max = 32)
    public String getFundchannel() {
        return (String) get(8);
    }

    /**
     * Setter for <code>opentogetdb.order.fundAmount</code>. 该支付工具类型所使用的金额
     */
    public void setFundamount(BigDecimal value) {
        set(9, value);
    }

    /**
     * Getter for <code>opentogetdb.order.fundAmount</code>. 该支付工具类型所使用的金额
     */
    public BigDecimal getFundamount() {
        return (BigDecimal) get(9);
    }

    /**
     * Setter for <code>opentogetdb.order.buyerUserId</code>. 买家在支付宝的用户id
     */
    public void setBuyeruserid(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>opentogetdb.order.buyerUserId</code>. 买家在支付宝的用户id
     */
    @Size(max = 32)
    public String getBuyeruserid() {
        return (String) get(10);
    }

    /**
     * Setter for <code>opentogetdb.order.buyerId</code>. 买家的支付宝用户Uid
     */
    public void setBuyerid(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>opentogetdb.order.buyerId</code>. 买家的支付宝用户Uid
     */
    @Size(max = 32)
    public String getBuyerid() {
        return (String) get(11);
    }

    /**
     * Setter for <code>opentogetdb.order.orderStatus</code>. 订单状态，0为未付款，1为以申请扣款但未完成，2为付款成功，3为用户无法支付
     */
    public void setOrderstatus(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>opentogetdb.order.orderStatus</code>. 订单状态，0为未付款，1为以申请扣款但未完成，2为付款成功，3为用户无法支付
     */
    public Integer getOrderstatus() {
        return (Integer) get(12);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record13 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row13<Integer, String, BigDecimal, String, String, String, BigDecimal, Timestamp, String, BigDecimal, String, String, Integer> fieldsRow() {
        return (Row13) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row13<Integer, String, BigDecimal, String, String, String, BigDecimal, Timestamp, String, BigDecimal, String, String, Integer> valuesRow() {
        return (Row13) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Order.ORDER.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Order.ORDER.OUTTRADENO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field3() {
        return Order.ORDER.TOTALAMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Order.ORDER.TRADENO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Order.ORDER.BUYERLOGONID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return Order.ORDER.RECEIPTAMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field7() {
        return Order.ORDER.BUYERPAYAMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field8() {
        return Order.ORDER.GMTPAYMENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return Order.ORDER.FUNDCHANNEL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field10() {
        return Order.ORDER.FUNDAMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field11() {
        return Order.ORDER.BUYERUSERID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field12() {
        return Order.ORDER.BUYERID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field13() {
        return Order.ORDER.ORDERSTATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getOuttradeno();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal component3() {
        return getTotalamount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getTradeno();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getBuyerlogonid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getReceiptamount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal component7() {
        return getBuyerpayamount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component8() {
        return getGmtpayment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component9() {
        return getFundchannel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal component10() {
        return getFundamount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component11() {
        return getBuyeruserid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component12() {
        return getBuyerid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component13() {
        return getOrderstatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getOuttradeno();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value3() {
        return getTotalamount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getTradeno();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getBuyerlogonid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getReceiptamount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value7() {
        return getBuyerpayamount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value8() {
        return getGmtpayment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getFundchannel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value10() {
        return getFundamount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value11() {
        return getBuyeruserid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value12() {
        return getBuyerid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value13() {
        return getOrderstatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value2(String value) {
        setOuttradeno(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value3(BigDecimal value) {
        setTotalamount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value4(String value) {
        setTradeno(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value5(String value) {
        setBuyerlogonid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value6(String value) {
        setReceiptamount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value7(BigDecimal value) {
        setBuyerpayamount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value8(Timestamp value) {
        setGmtpayment(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value9(String value) {
        setFundchannel(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value10(BigDecimal value) {
        setFundamount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value11(String value) {
        setBuyeruserid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value12(String value) {
        setBuyerid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord value13(Integer value) {
        setOrderstatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderRecord values(Integer value1, String value2, BigDecimal value3, String value4, String value5, String value6, BigDecimal value7, Timestamp value8, String value9, BigDecimal value10, String value11, String value12, Integer value13) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached OrderRecord
     */
    public OrderRecord() {
        super(Order.ORDER);
    }

    /**
     * Create a detached, initialised OrderRecord
     */
    public OrderRecord(Integer id, String outtradeno, BigDecimal totalamount, String tradeno, String buyerlogonid, String receiptamount, BigDecimal buyerpayamount, Timestamp gmtpayment, String fundchannel, BigDecimal fundamount, String buyeruserid, String buyerid, Integer orderstatus) {
        super(Order.ORDER);

        set(0, id);
        set(1, outtradeno);
        set(2, totalamount);
        set(3, tradeno);
        set(4, buyerlogonid);
        set(5, receiptamount);
        set(6, buyerpayamount);
        set(7, gmtpayment);
        set(8, fundchannel);
        set(9, fundamount);
        set(10, buyeruserid);
        set(11, buyerid);
        set(12, orderstatus);
    }
}
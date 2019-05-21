package com.example.demo.dao.repositry;

import com.example.demo.model.collection.DataResourceEnum;
import com.example.demo.model.entity.form.OrderForm;
import com.example.demo.util.JooqUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.OrderField;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.example.demo.model.entity.jooq.tables.Order.ORDER;

/**
* Order Repository with JOOQ
* Created by CoderMaker on 2019/05/15.
*/
@Repository
public class OrderRepository {

    private DSLContext create = JooqUtil.getCreate(DataResourceEnum.DATA_TARGET.getConn());

	//region 通用CURD方法
	/**
	 * 根据实体类条件查询数据(分页)
     * Querying data based on entity class conditions.(pagination)
	 * @Param order
     * @return Data mapping entity list
	 */
	public List<OrderForm> queryOrderListByCondition(OrderForm order){
		return create.select(getBaseColumnList()).from(ORDER)
				.where(queryCondition(order))
                .orderBy(getSordFieldList(order))
				.fetchInto(OrderForm.class);
	}

	/**
	 * 根据实体类条件查询数据(无分页)
	 * Querying data based on entity class conditions.(no pagination)
     * @Param order
     * @return Data mapping entity list
	 */
	public List<OrderForm> queryOrderListByConditionNoPage(OrderForm order){
		return create.select(getBaseColumnList()).from(ORDER)
				.where(queryCondition(order))
				.fetchInto(OrderForm.class);
	}

	/**
	 * 根据实体类条件查询数据总条数
     * Querying data count based on entity class conditions.
     * @Param order
     * @return Total Data
	 */
	public Integer queryOrderNumByCondition(OrderForm order){
		return create.fetchCount(ORDER,queryCondition(order));
	}

	/**
	 * 根据实体类属性进行数据添加
	 * Add data based on entity class attributes.
	 * @Param order
	 */
	public void addOrder(OrderForm order){
		create.insertInto(ORDER).columns(getBaseColumnList())
				.values(
						order.getId(),
						order.getOuttradeno(),
						order.getTotalamount(),
						order.getTradeno(),
						order.getBuyerlogonid(),
						order.getReceiptamount(),
						order.getBuyerpayamount(),
						order.getGmtpayment(),
						order.getFundchannel(),
						order.getFundamount(),
						order.getBuyeruserid(),
						order.getBuyerid(),
						order.getOrderstatus()
				).execute();
	}

	/**
	 * 根据主键删除数据
	 * Delete data according to the primary key.
	 * @Param id  PrimaryKey
	 */
	public void deleteOrder(Integer id){
		 create.deleteFrom(ORDER).where(ORDER.ID.eq(id)).execute();
	}

	/**
	 * 根据主键获取数据实体
	 * Querying Data entity based on primary key.
	 * @Param id  PrimaryKey
	 * @return Data mapping entity
	 */
	public OrderForm getOrder(Integer id){
		return create.select(getBaseColumnList()).from(ORDER)
                .where(ORDER.ID.eq(id)).fetchOneInto(OrderForm.class);
	}

	/**
	 * 根据实体类属性修改数据
	 * Modify data based on entity class attributes.
	 * @Param order
	 */
	public void editOrder(OrderForm order){
		 create.update(ORDER).set(setValueEmptyClause(order)).where(ORDER.ID.eq(order.getId())).execute();
	}

	/**
	 * 查询所有数据
	 * Querying all data.
	 * @return Data mapping entity list
	 */
	public List<OrderForm> queryAllOrder(){
		return create.select(getBaseColumnList()).from(ORDER).fetchInto(OrderForm.class);
	}
	//endregion

	//region 通用条件/基本字段构造
 	/**
     * 获取所有基本字段
     * @Param var1 自定义字段集合
     * @return 基本查询字段+自定义字段集合
     */
	private List<Field<?>> getBaseColumnList(Field<?>...var1){
		List<Field<?>> selectFieldList= Lists.newArrayList(
				ORDER.ID,
				ORDER.OUTTRADENO,
				ORDER.TOTALAMOUNT,
				ORDER.TRADENO,
				ORDER.BUYERLOGONID,
				ORDER.RECEIPTAMOUNT,
				ORDER.BUYERPAYAMOUNT,
				ORDER.GMTPAYMENT,
				ORDER.FUNDCHANNEL,
				ORDER.FUNDAMOUNT,
				ORDER.BUYERUSERID,
				ORDER.BUYERID,
				ORDER.ORDERSTATUS
		);
		selectFieldList.addAll(Lists.newArrayList(var1));
		return selectFieldList;
	}

	 /**
     * 查询条件构造
     * @Param order
     * @return
     */
	 private Condition queryCondition(OrderForm order){
		 Condition condition = DSL.trueCondition(); //equals where 1=1
		 if(order.getId() != null){
			 condition = condition.and(ORDER.ID.eq(order.getId()));
		 }
		 if(!Strings.isNullOrEmpty(order.getOuttradeno())){
			 condition = condition.and(ORDER.OUTTRADENO.containsIgnoreCase(order.getOuttradeno()));
		 }
		 if(order.getTotalamount() != null){
			 condition = condition.and(ORDER.TOTALAMOUNT.eq(order.getTotalamount()));
		 }
		 if(!Strings.isNullOrEmpty(order.getTradeno())){
			 condition = condition.and(ORDER.TRADENO.containsIgnoreCase(order.getTradeno()));
		 }
		 if(!Strings.isNullOrEmpty(order.getBuyerlogonid())){
			 condition = condition.and(ORDER.BUYERLOGONID.containsIgnoreCase(order.getBuyerlogonid()));
		 }
		 if(!Strings.isNullOrEmpty(order.getReceiptamount())){
			 condition = condition.and(ORDER.RECEIPTAMOUNT.containsIgnoreCase(order.getReceiptamount()));
		 }
		 if(order.getBuyerpayamount() != null){
			 condition = condition.and(ORDER.BUYERPAYAMOUNT.eq(order.getBuyerpayamount()));
		 }
		 if(!Strings.isNullOrEmpty(order.getGmtPayment_start())){
			 condition = condition.and(ORDER.GMTPAYMENT.ge(Timestamp.valueOf(order.getGmtPayment_start())));
		 }
		 if(!Strings.isNullOrEmpty(order.getGmtPayment_end())){
			 condition = condition.and(ORDER.GMTPAYMENT.le(Timestamp.valueOf(order.getGmtPayment_end())));
		 }
		 if(!Strings.isNullOrEmpty(order.getFundchannel())){
			 condition = condition.and(ORDER.FUNDCHANNEL.containsIgnoreCase(order.getFundchannel()));
		 }
		 if(order.getFundamount() != null){
			 condition = condition.and(ORDER.FUNDAMOUNT.eq(order.getFundamount()));
		 }
		 if(!Strings.isNullOrEmpty(order.getBuyeruserid())){
			 condition = condition.and(ORDER.BUYERUSERID.containsIgnoreCase(order.getBuyeruserid()));
		 }
		 if(!Strings.isNullOrEmpty(order.getBuyerid())){
			 condition = condition.and(ORDER.BUYERID.containsIgnoreCase(order.getBuyerid()));
		 }
		 if(order.getOrderstatus() != null){
			 condition = condition.and(ORDER.ORDERSTATUS.eq(order.getOrderstatus()));
		 }
		 return condition;
	 }

	 /**
     * 更新条件值构造
     * @Param order
     * @return
     */
	 private Map<Field<?>,Object> setValueEmptyClause(OrderForm order){
		 Map<Field<?>,Object> map= Maps.newHashMap();
		 if(order.getId() != null){
			 map.put(ORDER.ID,order.getId());
		 }
		 if(!Strings.isNullOrEmpty(order.getOuttradeno())){
			 map.put(ORDER.OUTTRADENO,order.getOuttradeno());
		 }
		 if(order.getTotalamount() != null){
			 map.put(ORDER.TOTALAMOUNT,order.getTotalamount());
		 }
		 if(!Strings.isNullOrEmpty(order.getTradeno())){
			 map.put(ORDER.TRADENO,order.getTradeno());
		 }
		 if(!Strings.isNullOrEmpty(order.getBuyerlogonid())){
			 map.put(ORDER.BUYERLOGONID,order.getBuyerlogonid());
		 }
		 if(!Strings.isNullOrEmpty(order.getReceiptamount())){
			 map.put(ORDER.RECEIPTAMOUNT,order.getReceiptamount());
		 }
		 if(order.getBuyerpayamount() != null){
			 map.put(ORDER.BUYERPAYAMOUNT,order.getBuyerpayamount());
		 }
		 if(order.getGmtpayment() != null){
			 map.put(ORDER.GMTPAYMENT,order.getGmtpayment());
		 }
		 if(!Strings.isNullOrEmpty(order.getFundchannel())){
			 map.put(ORDER.FUNDCHANNEL,order.getFundchannel());
		 }
		 if(order.getFundamount() != null){
			 map.put(ORDER.FUNDAMOUNT,order.getFundamount());
		 }
		 if(!Strings.isNullOrEmpty(order.getBuyeruserid())){
			 map.put(ORDER.BUYERUSERID,order.getBuyeruserid());
		 }
		 if(!Strings.isNullOrEmpty(order.getBuyerid())){
			 map.put(ORDER.BUYERID,order.getBuyerid());
		 }
		 if(order.getOrderstatus() != null){
			 map.put(ORDER.ORDERSTATUS,order.getOrderstatus());
		 }
		 return map;
	 }
	//endregion
    /**
     * 获取排序字段
     * @Param order 默认排序字段
     * @Param moreOrderField 更多排序条件(非主表请使用此属性)
     * @return
     */
    private List<OrderField<?>> getSordFieldList(OrderForm order, OrderField<?>... moreOrderField){

        List<OrderField<?>> orderFieldList=Lists.newArrayList();

        //默认排序字段
		orderFieldList.add(ORDER.ID.desc());
        //更多排序条件
        if(moreOrderField.length>0){
            Collections.addAll(orderFieldList, moreOrderField);
        }

        return orderFieldList;
    }
	//region 自定义方法写这里

    //endregion

}

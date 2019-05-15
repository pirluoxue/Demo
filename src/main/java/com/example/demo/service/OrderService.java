package com.example.demo.service;

import com.example.demo.model.entity.form.OrderForm;

import java.util.List;

/**
 * Order Service Interface
 * Created by CoderMaker on 2019/05/15.
 */
public interface OrderService {

	/**
	  * 根据实体类条件查询数据(分页)
	  * Querying data based on entity class conditions.(pagination)
	  * @Param order
	  * @return Data mapping entity list
	  */
	List<OrderForm> queryOrderListByCondition(OrderForm order);

	/**
	  * 根据实体类条件查询数据(无分页)
	  * Querying data based on entity class conditions.(no pagination)
	  * @Param order
	  * @return Data mapping entity list
	  */
	List<OrderForm> queryOrderListByConditionNoPage(OrderForm order);

	/**
	  * 根据实体类条件查询数据总条数
	  * Querying data count based on entity class conditions.
	  * @Param order
	  * @return Total Data
	  */
	Integer queryOrderNumByCondition(OrderForm order);

	/**
      * 根据实体类属性进行数据添加
	  * Add data based on entity class attributes.
	  * @Param order
	  */
	void addOrder(OrderForm order);

	/**
	  * 根据主键删除数据
	  * Delete data according to the primary key.
	  * @Param id  PrimaryKey
	  */
	void deleteOrder(Integer id);

	/**
	  * 根据主键获取数据实体
	  * Querying Data entity based on primary key.
	  * @Param id  PrimaryKey
	  * @return Data mapping entity
	  */
	OrderForm getOrder(Integer id);

	/**
	  * 根据实体类属性修改数据
	  * Modify data based on entity class attributes.
	  * @Param order
	  */
	void editOrder(OrderForm order);

	/**
	  * 查询所有数据
	  * Querying all data.
	  * @return Data mapping entity list
	  */
	List<OrderForm> queryAllOrder();
}

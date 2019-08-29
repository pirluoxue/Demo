package com.example.demo.service.Impl;

import com.example.demo.dao.repositry.OrderRepository;
import com.example.demo.model.entity.form.OrderForm;
import com.example.demo.service.OrderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
  * Order
  * Created by CoderMaker on 2019/05/15.
  */
@Service
public class OrderServiceImpl implements OrderService {

	private final static Logger log= Logger.getLogger(OrderService.class);
	
	@Autowired
	OrderRepository orderMapper;

	/**
	  * 根据实体类条件查询数据(分页)
	  * Querying data based on entity class conditions.(pagination)
	  * @Param order
	  * @return Data mapping entity list
	  */
	@Override
	public List<OrderForm> queryOrderListByCondition(OrderForm order) {
		return orderMapper.queryOrderListByCondition(order);
	}

	/**
	  * 根据实体类条件查询数据(无分页)
	  * Querying data based on entity class conditions.(no pagination)
      * @Param order
	  * @return Data mapping entity list
	  */
	@Override
	public List<OrderForm> queryOrderListByConditionNoPage(OrderForm order){
		return orderMapper.queryOrderListByConditionNoPage(order);
	}

	/**
	  * 根据实体类条件查询数据总条数
      * Querying data count based on entity class conditions.
	  * @Param order
	  * @return Total Data
	  */
	@Override
	public Integer queryOrderNumByCondition(OrderForm order) {
		return orderMapper.queryOrderNumByCondition(order);
	}

	/**
	  * 根据实体类属性进行数据添加
	  * Add data based on entity class attributes.
	  * @Param order
	  */
	@Override
	@Transactional
	public void addOrder(OrderForm order) {
		orderMapper.addOrder(order);
	}

	/**
	  * 根据主键删除数据
	  * Delete data according to the primary key.
	  * @Param id  PrimaryKey
	  */
	@Override
	@Transactional
	public void deleteOrder(Integer id) {
		orderMapper.deleteOrder(id);
	}

	/**
	  * 根据主键获取数据实体
	  * Querying Data entity based on primary key.
	  * @Param id  PrimaryKey
	  * @return Data mapping entity
	  */
	@Override
	public OrderForm getOrder(Integer id) {
		return orderMapper.getOrder(id);
	}

	/**
	  * 根据实体类属性修改数据
	  * Modify data based on entity class attributes.
	  * @Param order
	  */
	@Override
	@Transactional
	public void editOrder(OrderForm order) {
		orderMapper.editOrder(order);
	}

	/**
	  * 查询所有数据
	  * Querying all data.
	  * @return Data mapping entity list
	  */
	@Override
	public List<OrderForm> queryAllOrder(){
		return orderMapper.queryAllOrder();
	}

}

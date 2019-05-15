package com.example.demo.service.Impl;

import com.example.demo.dao.repositry.UserRepository;
import com.example.demo.model.entity.form.UserForm;
import com.example.demo.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
  * User
  * Created by CoderMaker on 2019/05/14.
  */
@Service
public class UserServiceImpl implements UserService {

	private final static Logger log= Logger.getLogger(UserService.class);
	
	@Autowired
	UserRepository userMapper;

	/**
	  * 根据实体类条件查询数据(分页)
	  * Querying data based on entity class conditions.(pagination)
	  * @Param user
	  * @return Data mapping entity list
	  */
	public List<UserForm> queryUserListByCondition(UserForm user) {
		return userMapper.queryUserListByCondition(user);
	}

	/**
	  * 根据实体类条件查询数据(无分页)
	  * Querying data based on entity class conditions.(no pagination)
      * @Param user
	  * @return Data mapping entity list
	  */
	public List<UserForm> queryUserListByConditionNoPage(UserForm user){
		return userMapper.queryUserListByConditionNoPage(user);
	}

	/**
	  * 根据实体类条件查询数据总条数
      * Querying data count based on entity class conditions.
	  * @Param user
	  * @return Total Data
	  */
	public Integer queryUserNumByCondition(UserForm user) {
		return userMapper.queryUserNumByCondition(user);
	}

	/**
	  * 根据实体类属性进行数据添加
	  * Add data based on entity class attributes.
	  * @Param user
	  */
	@Transactional
	public void addUser(UserForm user) {
		userMapper.addUser(user);
	}

	@Transactional
	public boolean saveAndUpdate(UserForm userForm){
		return userMapper.saveAndUpdate(userForm);
	}

	/**
	  * 根据主键删除数据
	  * Delete data according to the primary key.
	  * @Param id  PrimaryKey
	  */
	@Transactional
	public void deleteUser(Integer id) {
		userMapper.deleteUser(id);
	}

	/**
	  * 根据主键获取数据实体
	  * Querying Data entity based on primary key.
	  * @Param id  PrimaryKey
	  * @return Data mapping entity
	  */
	public UserForm getUser(Integer id) {
		return userMapper.getUser(id);
	}
	/**
	  * 根据主键获取数据实体
	  * Querying Data entity based on primary key.
	  * @Param id  PrimaryKey
	  * @return Data mapping entity
	  */
	public UserForm getUserByUserId(String userId) {
		return userMapper.getUserByUserId(userId);
	}

	/**
	  * 根据实体类属性修改数据
	  * Modify data based on entity class attributes.
	  * @Param user
	  */
	@Transactional
	public void editUser(UserForm user) {
		userMapper.editUser(user);
	}

	/**
	  * 查询所有数据
	  * Querying all data.
	  * @return Data mapping entity list
	  */
	public List<UserForm> queryAllUser(){
		return userMapper.queryAllUser();
	}

}

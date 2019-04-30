package com.example.demo.service;
import com.example.demo.model.entity.jooq.form.UserForm;

import java.util.List;

/**
 * User Service Interface
 * Created by CoderMaker on 2019/04/27.
 */
public interface UserService {

	/**
	  * 根据实体类条件查询数据(分页)
	  * Querying data based on entity class conditions.(pagination)
	  * @Param user
	  * @return Data mapping entity list
	  */
	List<UserForm> queryUserListByCondition(UserForm user);

	/**
	  * 根据实体类条件查询数据(无分页)
	  * Querying data based on entity class conditions.(no pagination)
	  * @Param user
	  * @return Data mapping entity list
	  */
	List<UserForm> queryUserListByConditionNoPage(UserForm user);

	/**
	  * 根据实体类条件查询数据总条数
	  * Querying data count based on entity class conditions.
	  * @Param user
	  * @return Total Data
	  */
	Integer queryUserNumByCondition(UserForm user);

	/**
      * 根据实体类属性进行数据添加
	  * Add data based on entity class attributes.
	  * @Param user
	  */
	void addUser(UserForm user);

	/**
	  * 根据主键删除数据
	  * Delete data according to the primary key.
	  * @Param id  PrimaryKey
	  */
	void deleteUser(Integer id);

	/**
	  * 根据主键获取数据实体
	  * Querying Data entity based on primary key.
	  * @Param id  PrimaryKey
	  * @return Data mapping entity
	  */
	UserForm getUser(Integer id);

	/**
	  * 根据实体类属性修改数据
	  * Modify data based on entity class attributes.
	  * @Param user
	  */
	void editUser(UserForm user);

	/**
	  * 查询所有数据
	  * Querying all data.
	  * @return Data mapping entity list
	  */
	List<UserForm> queryAllUser();
}

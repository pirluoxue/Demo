package com.example.demo.dao.repositry;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.demo.model.entity.jooq.form.UserForm;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.OrderField;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.example.demo.model.entity.jooq.Tables.USER;
import static org.jooq.impl.DSL.field;

/**
 * User Repository with JOOQ
 * Created by CoderMaker on 2019/04/27.
 */
@Repository
public class UserRepository {

	private DSLContext create;

	public void setCreate(Connection conn){
		this.create = DSL.using(conn);
	}

	//region 通用CURD方法
	/**
	 * 根据实体类条件查询数据(分页)
	 * Querying data based on entity class conditions.(pagination)
	 * @Param user
	 * @return Data mapping entity list
	 */
	public List<UserForm> queryUserListByCondition(UserForm user){
		return create.select(getBaseColumnList()).from(USER)
				.where(queryCondition(user))
				.orderBy(getSordFieldList(user))
				.fetchInto(UserForm.class);
	}

	/**
	 * 根据实体类条件查询数据(无分页)
	 * Querying data based on entity class conditions.(no pagination)
	 * @Param user
	 * @return Data mapping entity list
	 */
	public List<UserForm> queryUserListByConditionNoPage(UserForm user){
		return create.select(getBaseColumnList()).from(USER)
				.where(queryCondition(user))
				.fetchInto(UserForm.class);
	}

	/**
	 * 根据实体类条件查询数据总条数
	 * Querying data count based on entity class conditions.
	 * @Param user
	 * @return Total Data
	 */
	public Integer queryUserNumByCondition(UserForm user){
		return create.fetchCount(USER,queryCondition(user));
	}

	/**
	 * 根据实体类属性进行数据添加
	 * Add data based on entity class attributes.
	 * @Param user
	 */
	public void addUser(UserForm user){
		create.insertInto(USER).columns(getBaseColumnList())
				.values(
						user.getUserId(),
						user.getUserKey(),
						user.getUserName(),
						user.getUserOpenid(),
						user.getUserPassword(),
						user.getUserPhone(),
						user.getUserAmount(),
						user.getUserBalance(),
						user.getUserPoint(),
						user.getUserBinddate(),
						user.getUserBirth(),
						user.getUserEmail(),
						user.getUserGender(),
						user.getUserAddress(),
						user.getUserIsenabled(),
						user.getUserLogindate(),
						user.getUserRemarks(),
						user.getUserUsetotal(),
						user.getUserUname(),
						user.getUserQdkey(),
						user.getUserCdkey(),
						user.getUserDevcode(),
						user.getUserSecondcdkey(),
						user.getUserFromtype(),
						user.getUserJointime(),
						user.getUserCreatetime(),
						user.getUserUpdatetime()
				).execute();
	}

	/**
	 * 根据主键删除数据
	 * Delete data according to the primary key.
	 * @Param id  PrimaryKey
	 */
	public void deleteUser(Integer id){
		create.deleteFrom(USER).where(USER.USER_ID.eq(id)).execute();
	}

	/**
	 * 根据主键获取数据实体
	 * Querying Data entity based on primary key.
	 * @Param id  PrimaryKey
	 * @return Data mapping entity
	 */
	public UserForm getUser(Integer id){
		return create.select(getBaseColumnList()).from(USER)
				.where(USER.USER_ID.eq(id)).fetchOneInto(UserForm.class);
	}

	/**
	 * 根据实体类属性修改数据
	 * Modify data based on entity class attributes.
	 * @Param user
	 */
	public void editUser(UserForm user){
		create.update(USER).set(setValueEmptyClause(user)).where(USER.USER_ID.eq(user.getUserId())).execute();
	}

	/**
	 * 查询所有数据
	 * Querying all data.
	 * @return Data mapping entity list
	 */
	public List<UserForm> queryAllUser(){
		return create.select(getBaseColumnList()).from(USER).fetchInto(UserForm.class);
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
				USER.USER_ID,
				USER.USER_KEY,
				USER.USER_NAME,
				USER.USER_OPENID,
				USER.USER_PASSWORD,
				USER.USER_PHONE,
				USER.USER_AMOUNT,
				USER.USER_BALANCE,
				USER.USER_POINT,
				USER.USER_BINDDATE,
				USER.USER_BIRTH,
				USER.USER_EMAIL,
				USER.USER_GENDER,
				USER.USER_ADDRESS,
				USER.USER_ISENABLED,
				USER.USER_LOGINDATE,
				USER.USER_REMARKS,
				USER.USER_USETOTAL,
				USER.USER_UNAME,
				USER.USER_QDKEY,
				USER.USER_CDKEY,
				USER.USER_DEVCODE,
				USER.USER_SECONDCDKEY,
				USER.USER_FROMTYPE,
				USER.USER_JOINTIME,
				USER.USER_CREATETIME,
				USER.USER_UPDATETIME
		);
		selectFieldList.addAll(Lists.newArrayList(var1));
		return selectFieldList;
	}

	/**
	 * 查询条件构造
	 * @Param user
	 * @return
	 */
	private Condition queryCondition(UserForm user){
		Condition condition = DSL.trueCondition(); //equals where 1=1
		if(user.getUserId() != null){
			condition = condition.and(USER.USER_ID.eq(user.getUserId()));
		}
		if(!Strings.isNullOrEmpty(user.getUserKey())){
			condition = condition.and(USER.USER_KEY.containsIgnoreCase(user.getUserKey()));
		}
		if(!Strings.isNullOrEmpty(user.getUserName())){
			condition = condition.and(USER.USER_NAME.containsIgnoreCase(user.getUserName()));
		}
		if(!Strings.isNullOrEmpty(user.getUserOpenid())){
			condition = condition.and(USER.USER_OPENID.containsIgnoreCase(user.getUserOpenid()));
		}
		if(!Strings.isNullOrEmpty(user.getUserPassword())){
			condition = condition.and(USER.USER_PASSWORD.containsIgnoreCase(user.getUserPassword()));
		}
		if(!Strings.isNullOrEmpty(user.getUserPhone())){
			condition = condition.and(USER.USER_PHONE.containsIgnoreCase(user.getUserPhone()));
		}
		if(user.getUserAmount() != null){
			condition = condition.and(USER.USER_AMOUNT.eq(user.getUserAmount()));
		}
		if(user.getUserBalance() != null){
			condition = condition.and(USER.USER_BALANCE.eq(user.getUserBalance()));
		}
		if(user.getUserPoint() != null){
			condition = condition.and(USER.USER_POINT.eq(user.getUserPoint()));
		}
		if(!Strings.isNullOrEmpty(user.getUser_bindDate_start())){
			condition = condition.and(USER.USER_BINDDATE.ge(Timestamp.valueOf(user.getUser_bindDate_start())));
		}
		if(!Strings.isNullOrEmpty(user.getUser_bindDate_end())){
			condition = condition.and(USER.USER_BINDDATE.le(Timestamp.valueOf(user.getUser_bindDate_end())));
		}
		if(!Strings.isNullOrEmpty(user.getUser_birth_start())){
			condition = condition.and(USER.USER_BIRTH.ge(Timestamp.valueOf(user.getUser_birth_start())));
		}
		if(!Strings.isNullOrEmpty(user.getUser_birth_end())){
			condition = condition.and(USER.USER_BIRTH.le(Timestamp.valueOf(user.getUser_birth_end())));
		}
		if(!Strings.isNullOrEmpty(user.getUserEmail())){
			condition = condition.and(USER.USER_EMAIL.containsIgnoreCase(user.getUserEmail()));
		}
		if(!Strings.isNullOrEmpty(user.getUserGender())){
			condition = condition.and(USER.USER_GENDER.containsIgnoreCase(user.getUserGender()));
		}
		if(!Strings.isNullOrEmpty(user.getUserAddress())){
			condition = condition.and(USER.USER_ADDRESS.containsIgnoreCase(user.getUserAddress()));
		}
		if(user.getUserIsenabled() != null){
			condition = condition.and(USER.USER_ISENABLED.eq(user.getUserIsenabled()));
		}
		if(!Strings.isNullOrEmpty(user.getUser_loginDate_start())){
			condition = condition.and(USER.USER_LOGINDATE.ge(Timestamp.valueOf(user.getUser_loginDate_start())));
		}
		if(!Strings.isNullOrEmpty(user.getUser_loginDate_end())){
			condition = condition.and(USER.USER_LOGINDATE.le(Timestamp.valueOf(user.getUser_loginDate_end())));
		}
		if(!Strings.isNullOrEmpty(user.getUserRemarks())){
			condition = condition.and(USER.USER_REMARKS.containsIgnoreCase(user.getUserRemarks()));
		}
		if(user.getUserUsetotal() != null){
			condition = condition.and(USER.USER_USETOTAL.eq(user.getUserUsetotal()));
		}
		if(!Strings.isNullOrEmpty(user.getUserUname())){
			condition = condition.and(USER.USER_UNAME.containsIgnoreCase(user.getUserUname()));
		}
		if(!Strings.isNullOrEmpty(user.getUserQdkey())){
			condition = condition.and(USER.USER_QDKEY.containsIgnoreCase(user.getUserQdkey()));
		}
		if(!Strings.isNullOrEmpty(user.getUserCdkey())){
			condition = condition.and(USER.USER_CDKEY.containsIgnoreCase(user.getUserCdkey()));
		}
		if(!Strings.isNullOrEmpty(user.getUserDevcode())){
			condition = condition.and(USER.USER_DEVCODE.containsIgnoreCase(user.getUserDevcode()));
		}
		if(!Strings.isNullOrEmpty(user.getUserSecondcdkey())){
			condition = condition.and(USER.USER_SECONDCDKEY.containsIgnoreCase(user.getUserSecondcdkey()));
		}
		if(!Strings.isNullOrEmpty(user.getUserFromtype())){
			condition = condition.and(USER.USER_FROMTYPE.containsIgnoreCase(user.getUserFromtype()));
		}
		if(!Strings.isNullOrEmpty(user.getUser_joinTime_start())){
			condition = condition.and(USER.USER_JOINTIME.ge(Timestamp.valueOf(user.getUser_joinTime_start())));
		}
		if(!Strings.isNullOrEmpty(user.getUser_joinTime_end())){
			condition = condition.and(USER.USER_JOINTIME.le(Timestamp.valueOf(user.getUser_joinTime_end())));
		}
		if(!Strings.isNullOrEmpty(user.getUser_createTime_start())){
			condition = condition.and(USER.USER_CREATETIME.ge(Timestamp.valueOf(user.getUser_createTime_start())));
		}
		if(!Strings.isNullOrEmpty(user.getUser_createTime_end())){
			condition = condition.and(USER.USER_CREATETIME.le(Timestamp.valueOf(user.getUser_createTime_end())));
		}
		if(!Strings.isNullOrEmpty(user.getUser_updateTime_start())){
			condition = condition.and(USER.USER_UPDATETIME.ge(Timestamp.valueOf(user.getUser_updateTime_start())));
		}
		if(!Strings.isNullOrEmpty(user.getUser_updateTime_end())){
			condition = condition.and(USER.USER_UPDATETIME.le(Timestamp.valueOf(user.getUser_updateTime_end())));
		}
		return condition;
	}

	/**
	 * 更新条件值构造
	 * @Param user
	 * @return
	 */
	private Map<Field<?>,Object> setValueEmptyClause(UserForm user){
		Map<Field<?>,Object> map= Maps.newHashMap();
		if(user.getUserId() != null){
			map.put(USER.USER_ID,user.getUserId());
		}
		if(!Strings.isNullOrEmpty(user.getUserKey())){
			map.put(USER.USER_KEY,user.getUserKey());
		}
		if(!Strings.isNullOrEmpty(user.getUserName())){
			map.put(USER.USER_NAME,user.getUserName());
		}
		if(!Strings.isNullOrEmpty(user.getUserOpenid())){
			map.put(USER.USER_OPENID,user.getUserOpenid());
		}
		if(!Strings.isNullOrEmpty(user.getUserPassword())){
			map.put(USER.USER_PASSWORD,user.getUserPassword());
		}
		if(!Strings.isNullOrEmpty(user.getUserPhone())){
			map.put(USER.USER_PHONE,user.getUserPhone());
		}
		if(user.getUserAmount() != null){
			map.put(USER.USER_AMOUNT,user.getUserAmount());
		}
		if(user.getUserBalance() != null){
			map.put(USER.USER_BALANCE,user.getUserBalance());
		}
		if(user.getUserPoint() != null){
			map.put(USER.USER_POINT,user.getUserPoint());
		}
		if(user.getUserBinddate() != null){
			map.put(USER.USER_BINDDATE,user.getUserBinddate());
		}
		if(user.getUserBirth() != null){
			map.put(USER.USER_BIRTH,user.getUserBirth());
		}
		if(!Strings.isNullOrEmpty(user.getUserEmail())){
			map.put(USER.USER_EMAIL,user.getUserEmail());
		}
		if(!Strings.isNullOrEmpty(user.getUserGender())){
			map.put(USER.USER_GENDER,user.getUserGender());
		}
		if(!Strings.isNullOrEmpty(user.getUserAddress())){
			map.put(USER.USER_ADDRESS,user.getUserAddress());
		}
		if(user.getUserIsenabled() != null){
			map.put(USER.USER_ISENABLED,user.getUserIsenabled());
		}
		if(user.getUserLogindate() != null){
			map.put(USER.USER_LOGINDATE,user.getUserLogindate());
		}
		if(!Strings.isNullOrEmpty(user.getUserRemarks())){
			map.put(USER.USER_REMARKS,user.getUserRemarks());
		}
		if(user.getUserUsetotal() != null){
			map.put(USER.USER_USETOTAL,user.getUserUsetotal());
		}
		if(!Strings.isNullOrEmpty(user.getUserUname())){
			map.put(USER.USER_UNAME,user.getUserUname());
		}
		if(!Strings.isNullOrEmpty(user.getUserQdkey())){
			map.put(USER.USER_QDKEY,user.getUserQdkey());
		}
		if(!Strings.isNullOrEmpty(user.getUserCdkey())){
			map.put(USER.USER_CDKEY,user.getUserCdkey());
		}
		if(!Strings.isNullOrEmpty(user.getUserDevcode())){
			map.put(USER.USER_DEVCODE,user.getUserDevcode());
		}
		if(!Strings.isNullOrEmpty(user.getUserSecondcdkey())){
			map.put(USER.USER_SECONDCDKEY,user.getUserSecondcdkey());
		}
		if(!Strings.isNullOrEmpty(user.getUserFromtype())){
			map.put(USER.USER_FROMTYPE,user.getUserFromtype());
		}
		if(user.getUserJointime() != null){
			map.put(USER.USER_JOINTIME,user.getUserJointime());
		}
		if(user.getUserCreatetime() != null){
			map.put(USER.USER_CREATETIME,user.getUserCreatetime());
		}
		if(user.getUserUpdatetime() != null){
			map.put(USER.USER_UPDATETIME,user.getUserUpdatetime());
		}
		return map;
	}
	//endregion
	/**
	 * 获取排序字段
	 * @Param user 默认排序字段
	 * @Param moreOrderField 更多排序条件(非主表请使用此属性)
	 * @return
	 */
	private List<OrderField<?>> getSordFieldList(UserForm user, OrderField<?>... moreOrderField){

		List<OrderField<?>> orderFieldList=Lists.newArrayList();

		//默认排序字段
		orderFieldList.add(USER.USER_ID.desc());
		//更多排序条件
		if(moreOrderField.length>0){
			Collections.addAll(orderFieldList, moreOrderField);
		}

		return orderFieldList;
	}
	//region 自定义方法写这里

	//endregion

}

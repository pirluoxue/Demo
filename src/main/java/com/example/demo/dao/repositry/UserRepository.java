package com.example.demo.dao.repositry;

import com.example.demo.model.collection.DataResourceEnum;
import com.example.demo.model.entity.form.UserForm;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.example.demo.model.entity.jooq.Tables.USER;

/**
* User Repository with JOOQ
* Created by CoderMaker on 2019/05/14.
*/
@Repository
public class UserRepository {

    private DSLContext create = JooqUtil.getCreate(DataResourceEnum.DATA_TARGET.getConn());

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
						user.getUserUserid(),
						user.getUserPhone(),
						user.getUserAmount(),
						user.getUserBalance(),
						user.getUserBirth(),
						user.getUserEmail(),
						user.getUserGender(),
						user.getUserAddress(),
						user.getUserRemarks(),
						user.getUserFromtype(),
						user.getUserJointime(),
						user.getUserCreatetime(),
						user.getUserUpdatetime(),
						user.getUserEnable(),
						user.getUserExternalagreementno(),
						user.getUserZmopenid(),
						user.getUserStatus(),
						user.getUserLogonid(),
						user.getUserAgreementno()
				).execute();
	}

	public boolean saveAndUpdate(UserForm userForm){
		UserForm originForm = new UserForm();
		if(userForm.getUserId() != null){
			originForm.setUserId(userForm.getUserId());
		}
		if(!Strings.isNullOrEmpty(userForm.getUserUserid())){
			originForm.setUserUserid(userForm.getUserUserid());
		}else{
			//user_userId 不能为空
			return false;
		}
		List<UserForm> userForms = queryUserListByCondition(originForm);
		if(userForms != null && userForms.size() > 0){
			editUser(userForm);
		}else{
			addUser(userForm);
		}
		return true;
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
	 * 根据主键获取数据实体
	 * Querying Data entity based on primary key.
	 * @Param id  PrimaryKey
	 * @return Data mapping entity
	 */
	public UserForm getUserByUserId(String userId){
		return create.select(getBaseColumnList()).from(USER)
                .where(USER.USER_USERID.eq(userId)).fetchOneInto(UserForm.class);
	}

	/**
	 * 根据实体类属性修改数据
	 * Modify data based on entity class attributes.
	 * @Param user
	 */
	public void editUser(UserForm user){
		Condition condition = DSL.trueCondition(); //equals where 1=1
		if(user.getUserId() != null){
			condition = condition.and(USER.USER_ID.eq(user.getUserId()));
		}
		if (!Strings.isNullOrEmpty(user.getUserUserid())){
			condition = condition.and(USER.USER_USERID.eq(user.getUserUserid()));
		}
		 create.update(USER).set(setValueEmptyClause(user)).where(condition).execute();
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
				USER.USER_USERID,
				USER.USER_PHONE,
				USER.USER_AMOUNT,
				USER.USER_BALANCE,
				USER.USER_BIRTH,
				USER.USER_EMAIL,
				USER.USER_GENDER,
				USER.USER_ADDRESS,
				USER.USER_REMARKS,
				USER.USER_FROMTYPE,
				USER.USER_JOINTIME,
				USER.USER_CREATETIME,
				USER.USER_UPDATETIME,
				USER.USER_ENABLE,
				USER.USER_EXTERNALAGREEMENTNO,
				USER.USER_ZMOPENID,
				USER.USER_STATUS,
				USER.USER_LOGONID,
				USER.USER_AGREEMENTNO
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
		 if(!Strings.isNullOrEmpty(user.getUserUserid())){
			 condition = condition.and(USER.USER_USERID.eq(user.getUserUserid()));
		 }
		 if(!Strings.isNullOrEmpty(user.getUserPhone())){
			 condition = condition.and(USER.USER_PHONE.eq(user.getUserPhone()));
		 }
		 if(user.getUserAmount() != null){
			 condition = condition.and(USER.USER_AMOUNT.eq(user.getUserAmount()));
		 }
		 if(user.getUserBalance() != null){
			 condition = condition.and(USER.USER_BALANCE.eq(user.getUserBalance()));
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
		 if(!Strings.isNullOrEmpty(user.getUserRemarks())){
			 condition = condition.and(USER.USER_REMARKS.containsIgnoreCase(user.getUserRemarks()));
		 }
		 if(!Strings.isNullOrEmpty(user.getUserFromtype())){
			 condition = condition.and(USER.USER_FROMTYPE.containsIgnoreCase(user.getUserFromtype()));
		 }
		 if(user.getUserEnable() != null){
			 condition = condition.and(USER.USER_ENABLE.eq(user.getUserEnable()));
		 }
		 if(!Strings.isNullOrEmpty(user.getUserExternalagreementno())){
			 condition = condition.and(USER.USER_EXTERNALAGREEMENTNO.containsIgnoreCase(user.getUserExternalagreementno()));
		 }
		 if(!Strings.isNullOrEmpty(user.getUserZmopenid())){
			 condition = condition.and(USER.USER_ZMOPENID.containsIgnoreCase(user.getUserZmopenid()));
		 }
		 if(!Strings.isNullOrEmpty(user.getUserStatus())){
			 condition = condition.and(USER.USER_STATUS.containsIgnoreCase(user.getUserStatus()));
		 }
		 if(!Strings.isNullOrEmpty(user.getUserLogonid())){
			 condition = condition.and(USER.USER_LOGONID.containsIgnoreCase(user.getUserLogonid()));
		 }
		 if(!Strings.isNullOrEmpty(user.getUserAgreementno())){
			 condition = condition.and(USER.USER_AGREEMENTNO.containsIgnoreCase(user.getUserAgreementno()));
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
		 if(!Strings.isNullOrEmpty(user.getUserUserid())){
			 map.put(USER.USER_USERID,user.getUserUserid());
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
		 if(!Strings.isNullOrEmpty(user.getUserRemarks())){
			 map.put(USER.USER_REMARKS,user.getUserRemarks());
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
		 if(user.getUserEnable() != null){
			 map.put(USER.USER_ENABLE,user.getUserEnable());
		 }
		 if(!Strings.isNullOrEmpty(user.getUserExternalagreementno())){
			 map.put(USER.USER_EXTERNALAGREEMENTNO,user.getUserExternalagreementno());
		 }
		 if(!Strings.isNullOrEmpty(user.getUserZmopenid())){
			 map.put(USER.USER_ZMOPENID,user.getUserZmopenid());
		 }
		 if(!Strings.isNullOrEmpty(user.getUserStatus())){
			 map.put(USER.USER_STATUS,user.getUserStatus());
		 }
		 if(!Strings.isNullOrEmpty(user.getUserLogonid())){
			 map.put(USER.USER_LOGONID,user.getUserLogonid());
		 }
		 if(!Strings.isNullOrEmpty(user.getUserAgreementno())){
			 map.put(USER.USER_AGREEMENTNO,user.getUserAgreementno());
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

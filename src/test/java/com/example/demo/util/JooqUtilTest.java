package com.example.demo.util;

import ch.qos.logback.core.db.dialect.DBUtil;
import com.example.demo.dao.repositry.UserRepository;
import com.example.demo.model.collection.DataResourceEnum;
import com.example.demo.model.entity.clone.SimpleCloneEntity;
import com.example.demo.model.entity.jooq.form.UserForm;
import com.example.demo.model.entity.jooq.tables.User;
import com.example.demo.model.entity.jooq.tables.records.UserRecord;
import com.example.demo.service.UserService;
import com.google.common.collect.Lists;
import lombok.Data;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.model.entity.jooq.Tables.USER;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//排除数据源自动配置
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JooqUtilTest {

    @Ignore
    public void testJooq() {
        ConnectionUtil connectionUtil = DataResourceUtil.getInstance().getConnection("jdbc1");
        System.out.println(connectionUtil);
    }

    @Test
    public void JooqUsing() {
//        DSLContext create = JooqUtil.getCreate(DataResourceEnum.DATA_TARGET.getConn());
//        List<User> list = create.select(getBaseColumnList()).from(USER).fetchInto(User.class);
//        System.out.println(list);
    }

    @Test
    public void ImportDataBase() throws SQLException {
        Connection connection = DataResourceEnum.DATA_RESOURCE.getConn();
        String sql = "select wx_openid, name, password, phone, amount, balance, point, birth, email, gender, address\n" +
                "     , is_enabled, login_date, remarks, use_total, uname, qd_id, cd_id, second_cd_id\n" +
                "     , from_type, join_time\n" +
                "from xx_member limit 0, ";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            List<UserRecord> userRecords = new ArrayList<>();
            while (rs.next()) {
                UserRecord userRecord = new UserRecord();
                userRecord.setUserOpenid(rs.getString("wx_openid"));
                userRecord.setUserName(rs.getString("name"));
                userRecord.setUserPassword(rs.getString("password"));
                userRecord.setUserPhone(rs.getString("phone"));
                userRecord.setUserAmount(rs.getDouble("amount"));
                userRecord.setUserBalance(rs.getDouble("balance"));
                userRecord.setUserPoint(rs.getInt("point"));
                userRecord.setUserBirth(rs.getTimestamp("birth"));
                userRecord.setUserEmail(rs.getString("email"));
                userRecord.setUserGender(rs.getString("gender"));
                userRecord.setUserAddress(rs.getString("address"));
                userRecord.setUserIsenabled(rs.getInt("is_enabled"));
                userRecord.setUserLogindate(rs.getTimestamp("login_date"));
                userRecord.setUserRemarks(rs.getString("remarks"));
                userRecord.setUserUsetotal(rs.getInt("use_total"));
                userRecord.setUserUname(rs.getString("uname"));
                userRecord.setUserQdkey(rs.getString("qd_id"));
                userRecord.setUserCdkey(rs.getString("cd_id"));
                userRecord.setUserSecondcdkey(rs.getString("second_cd_id"));
                userRecord.setUserFromtype(rs.getString("from_type"));
                userRecord.setUserJointime(rs.getTimestamp("join_time"));
                userRecords.add(userRecord);
            }

            int threadNum = 30;
            int dataNum = userRecords.size();
            int onceNum = dataNum % threadNum == 0 ? dataNum / threadNum : dataNum / threadNum + 1;
            int originThreadNum = Thread.activeCount();
            for (int i = 0; i < threadNum; i++) {
//                ImportDataForJooq importData = new ImportDataForJooq();
                ImportDataForJDBC importData = new ImportDataForJDBC();
                importData.setOnceNum(100);
                int beginIndex = i * onceNum;
                int endIndex = (i + 1) * onceNum;
                if (endIndex > userRecords.size()) {
                    endIndex = userRecords.size();
                }
                importData.setUserRecords(userRecords.subList(beginIndex, endIndex));
                Thread thread = new Thread(importData);
                thread.start();
            }

            while (true) {
                int nowThreadNum = Thread.activeCount();
                if (originThreadNum < nowThreadNum) {
                    System.out.println("剩余线程数： " + (nowThreadNum - originThreadNum));
                    Thread.sleep(10000);
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Data
    class ImportDataForJooq implements Runnable {

        private List<UserRecord> userRecords;

        private Integer onceNum = 10;

        @Override
        public void run() {
            LocalDateTime now = LocalDateTime.now();
            System.out.println("启动 " + Thread.currentThread().getName());
            if (userRecords == null || userRecords.size() <= 0) {
                Thread.interrupted();
                return;
            }
            int Times = (userRecords.size() % onceNum == 0 ? userRecords.size() / onceNum : userRecords.size() / onceNum + 1);
            DSLContext create = DSL.using(DataResourceEnum.DATA_TARGET.getConn());
            for (int i = 0; i < Times; i++) {
                System.out.println(Thread.currentThread().getName() + " 开始从" + i * onceNum + " 插入" + onceNum + "条数据");
                List<UserRecord> list = userRecords.subList(i * onceNum, (i + 1) * onceNum);
                create.batchInsert(list).execute();
                System.out.println(Thread.currentThread().getName() + " 完成插入到" + (i + 1) * onceNum + "条数据");
            }
            LocalDateTime end = LocalDateTime.now();
            Duration duration = Duration.between(now,end);
            System.out.println(Thread.currentThread().getName() + "完成所有任务，总用时：" + duration);
            Thread.interrupted();
        }
    }

    @Data
    class ImportDataForJDBC implements Runnable{

        private List<UserRecord> userRecords;

        private Integer onceNum = 10;

        @Override
        public void run() {
            LocalDateTime now = LocalDateTime.now();
            System.out.println("启动 " + Thread.currentThread().getName());
            if (userRecords == null || userRecords.size() <= 0) {
                Thread.interrupted();
                return;
            }
            int Times = (userRecords.size() % onceNum == 0 ? userRecords.size() / onceNum : userRecords.size() / onceNum + 1);
            for (int i = 0; i < Times; i++) {
                try {
                List<UserRecord> list = userRecords.subList(i * onceNum, (i + 1) * onceNum);
                    Statement statement = DataResourceEnum.DATA_TARGET.getConn().createStatement();
                    getSQLStatement(list, onceNum, statement);
                    statement.executeBatch();
//                    PreparedStatement preparedStatement = DataResourceEnum.DATA_TARGET.getConn().prepareStatement(sqlBuilder.toString());
//                    preparedStatement.execute();
//                    preparedStatement.close();
                    System.out.println(Thread.currentThread().getName() + " 完成插入到" + (i + 1) * onceNum + "条数据");
                    statement.clearBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            LocalDateTime end = LocalDateTime.now();
            Duration duration = Duration.between(now,end);
            System.out.println(Thread.currentThread().getName() + "完成所有任务，总用时：" + duration);
            Thread.interrupted();
        }

        private String getSQLString(List<UserRecord> list, int onceNum){
            StringBuilder sql = new StringBuilder();
            for(int i = 0 ; i < onceNum ; i++){
                sql.append(getCondition(list.get(i)));
                sql.append(getParameter(list.get(i)));
            }
            return sql.toString();
        }

        private void getSQLStatement(List<UserRecord> list, int onceNum, Statement statement) throws SQLException {
            StringBuilder sql = new StringBuilder();
            StringBuilder test = new StringBuilder();
            for(int i = 0 ; i < onceNum ; i++){
                sql.append(getCondition(list.get(i)));
                sql.append(getParameter(list.get(i)));
                test.append(sql);
                statement.addBatch(sql.toString());
                sql.delete(0, sql.length());
            }
            System.out.println(test.toString());
        }

        private String getCondition(UserRecord userRecord){
            if(userRecord == null){
                return "";
            }
            StringBuilder sql = new StringBuilder();
            sql.append("insert user (");
            sql.append(userRecord.getUserOpenid()!=null?"user_openId, ":"");
            sql.append(userRecord.getUserName()!=null?"user_name, ":"");
            sql.append(userRecord.getUserPassword()!=null?"user_password, ":"");
            sql.append(userRecord.getUserPhone()!=null?"user_phone, ":"");
            sql.append(userRecord.getUserAmount()!=null?"user_amount, ":"");
            sql.append(userRecord.getUserBalance()!=null?"user_balance, ":"");
            sql.append(userRecord.getUserPoint()!=null?"user_point, ":"");
            sql.append(userRecord.getUserBirth()!=null?"user_birth, ":"");
            sql.append(userRecord.getUserEmail()!=null?"user_email, ":"");
            sql.append(userRecord.getUserGender()!=null?"user_gender, ":"");
            sql.append(userRecord.getUserAddress()!=null?"user_address, ":"");
            sql.append(userRecord.getUserIsenabled()!=null?"user_isEnabled, ":"");
            sql.append(userRecord.getUserLogindate()!=null?"user_loginDate, ":"");
            sql.append(userRecord.getUserRemarks()!=null?"user_remarks, ":"");
            sql.append(userRecord.getUserUsetotal()!=null?"user_useTotal, ":"");
            sql.append(userRecord.getUserUname()!=null?"user_uname, ":"");
            sql.append(userRecord.getUserQdkey()!=null?"user_qdKey, ":"");
            sql.append(userRecord.getUserCdkey()!=null?"user_cdKey, ":"");
            sql.append(userRecord.getUserSecondcdkey()!=null?"user_secondCdKey, ":"");
            sql.append(userRecord.getUserFromtype()!=null?"user_fromType, ":"");
            sql.append(userRecord.getUserJointime()!=null?"user_joinTime, ":"");
            if(sql.toString().endsWith(", ")){
                sql.replace(sql.length() - 2, sql.length(),"");
            }
            sql.append(") value ");
            return sql.toString();
        }

        private String getParameter(UserRecord userRecord){
            if(userRecord == null){
                return "";
            }
            StringBuilder sql = new StringBuilder();
            sql.append("(");
            sql.append((userRecord.getUserOpenid()!=null?"'" + userRecord.getUserOpenid()+ "', ":""));
            sql.append((userRecord.getUserName()!=null?"'" +userRecord.getUserName()+ "', ":""));
            sql.append((userRecord.getUserPassword()!=null?"'" +userRecord.getUserPassword()+ "', ":""));
            sql.append((userRecord.getUserPhone()!=null?"'" +userRecord.getUserPhone()+ "', ":""));
            sql.append((userRecord.getUserAmount()!=null?"'" +userRecord.getUserAmount()+ "', ":""));
            sql.append((userRecord.getUserBalance()!=null?"'" +userRecord.getUserBalance()+ "', ":""));
            sql.append((userRecord.getUserPoint()!=null?"'" +userRecord.getUserPoint()+ "', ":""));
            sql.append((userRecord.getUserBirth()!=null?"'" +userRecord.getUserBirth()+ "', ":""));
            sql.append((userRecord.getUserEmail()!=null?"'" +userRecord.getUserEmail()+ "', ":""));
            sql.append((userRecord.getUserGender()!=null?"'" +userRecord.getUserGender()+ "', ":""));
            sql.append((userRecord.getUserAddress()!=null?"'" +userRecord.getUserAddress()+ "', ":""));
            sql.append((userRecord.getUserIsenabled()!=null?"'" +userRecord.getUserIsenabled()+ "', ":""));
            sql.append((userRecord.getUserLogindate()!=null?"'" +userRecord.getUserLogindate()+ "', ":""));
            sql.append((userRecord.getUserRemarks()!=null?"'" +userRecord.getUserRemarks()+ "', ":""));
            sql.append((userRecord.getUserUsetotal()!=null?"'" +userRecord.getUserUsetotal()+ "', ":""));
            sql.append((userRecord.getUserUname()!=null?"'" +userRecord.getUserUname()+ "', ":""));
            sql.append((userRecord.getUserQdkey()!=null?"'" +userRecord.getUserQdkey()+ "', ":""));
            sql.append((userRecord.getUserCdkey()!=null?"'" +userRecord.getUserCdkey()+ "', ":""));
            sql.append((userRecord.getUserSecondcdkey()!=null?"'" +userRecord.getUserSecondcdkey()+ "', ":""));
            sql.append((userRecord.getUserFromtype()!=null?"'" +userRecord.getUserFromtype()+ "', ":""));
            sql.append((userRecord.getUserJointime()!=null?"'" +userRecord.getUserJointime()+ "', ":""));
            if(sql.toString().endsWith(", ")){
                sql.replace(sql.length() - 2, sql.length(),"");
            }
            sql.append(");");
            return sql.toString();
        }

    }


}
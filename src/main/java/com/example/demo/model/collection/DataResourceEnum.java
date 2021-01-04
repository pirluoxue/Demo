package com.example.demo.model.collection;

import com.example.demo.util.ConnectionUtil;
import com.example.demo.util.DataResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author chen_bq
 * @description
 * @create: 2019-04-27 14:02
 **/
@Slf4j(topic = "DataResourceEnum")
public enum DataResourceEnum {

//    DATA_RESOURCE(DataResourceUtil.getInstance().getConnection("jdbc1"), "jdbc2"),
    DATA_TARGET(DataResourceUtil.getInstance().getConnection("jdbc2"), "jdbc2");

    private static final Logger logger = LoggerFactory.getLogger(DataResourceEnum.class);

    private ConnectionUtil connectionFactory;
    private String name;

    DataResourceEnum(ConnectionUtil connectionFactory, String name){
        this.name = name;
        this.connectionFactory = connectionFactory;
    }

    public Connection getConn(){
        if (this == null){
            return null;
        }
        try {
            if (this.connectionFactory.getConn() == null || this.connectionFactory.getConn().isClosed()){
                logger.info("重新获取数据库连接");
                this.connectionFactory.createConnection();
            } else if (this.connectionFactory.hadExpiredConn()){
                // 连接还可能存在，但是很久没更新了，所以断开原有连接，重新获取
                logger.info("长期未使用，断开连接");
                this.connectionFactory.getConn().close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // 更新连接时间
        this.connectionFactory.updateLastPacketSent();
        return this.connectionFactory.getConn();
    }


}

package com.example.demo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author chen_bq
 * @description java数据库连接工具类
 * @create: 2019-01-24 14:55
 **/
public class ConnectionUtil {

    private String url;
    private String driver;
    private String userName;
    private String passWord;

    private Connection conn;
    private Long lastPacketSent;

    private static final Long CONNECTION_EXPIRE_TIME = 1 * 60 * 60 * 1000L;

    public ConnectionUtil() {
    }

    public ConnectionUtil(String driver, String url, String userName, String passWord) {
        this.url = url;
        this.driver = driver;
        this.userName = userName;
        this.passWord = passWord;
        this.lastPacketSent = System.currentTimeMillis();
        try {
            // 加载MySql的驱动类
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("找不到驱动程序类 ，加载驱动失败！");
            e.printStackTrace();
        }
        this.createConnection();
    }

    public void createConnection() {
        try {
            this.setConn(DriverManager.getConnection(this.getUrl(),
                    this.getUserName(), this.getPassWord()));
            this.lastPacketSent = System.currentTimeMillis();
        } catch (SQLException se) {
            System.out.println("数据库连接失败！");
            se.printStackTrace();
        }
    }

    /**
     * 关闭数据库链接
     * @return boolean
     */
    public boolean closeConnection() {
        try {
            if (this.getConn() != null) { // 关闭连接对象
                try {
                    this.getConn().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("关闭连接失败!");
            return false;
        }
        return true;
    }

    public Connection getConn() {
        try {
            if (conn == null || conn.isClosed()){
                createConnection();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    public void updateLastPacketSent() {
        this.lastPacketSent = System.currentTimeMillis();
    }

    public Boolean hadExpiredConn(){
        if (lastPacketSent != null && lastPacketSent < System.currentTimeMillis() - CONNECTION_EXPIRE_TIME){
            return true;
        }
        return false;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Long getLastPacketSent() {
        return lastPacketSent;
    }

    public void setLastPacketSent(Long lastPacketSent) {
        this.lastPacketSent = lastPacketSent;
    }
}

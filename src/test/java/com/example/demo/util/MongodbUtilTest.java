package com.example.demo.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MongodbUtilTest {

    @Test
    public void insertTest() {
        try {
            MongoClientOptions.Builder builder = MongoClientOptions.builder(); //可以通过builder做各种详细配置
            MongoClientOptions myOptions = builder.build();
            ArrayList<ServerAddress> serverAddressList = new ArrayList();
            ServerAddress record = new ServerAddress("localhost", 27017); //IP、端口
            serverAddressList.add(record);
            //用户名、默认库名、密码
            MongoCredential credential = MongoCredential.createCredential("root", "testdb", "123456".toCharArray());
            MongoClient mongoClient = new MongoClient(serverAddressList, credential, myOptions);
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("testdb");
            System.out.println("Connect to database successfully");
//            FindIterable<Document> findIterable  = mongoDatabase.getCollection("batch_test").find();
            for (int i = 0; i < 10000; i++) {
                List<Document> list = new ArrayList();
                for (int j = 0; j < 100; j++) {
                    Document document = new Document();
                    document.put("name", "MyName " + i + " " + j);
                    document.put("title", "MyTitle " + i + " " + j);
                    document.put("index_num", i * 100 + j);
                    document.put("common_num", i * 100 + j);
                    document.put("remark", "decription group " + i + " sub-group " + j);
                    document.put("createTime", new Timestamp(System.currentTimeMillis()));
                    list.add(document);
                }
                mongoDatabase.getCollection("batch_test").insertMany(list);
            }
            mongoClient.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }


}
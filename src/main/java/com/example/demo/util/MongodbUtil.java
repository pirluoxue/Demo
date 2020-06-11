package com.example.demo.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

/**
 * @author chen_bq
 * @description mongoDb工具类
 * @create: 2019/8/9 17:37
 **/
public class MongodbUtil {

    public static void main( String args[] ) {
        try {
//            // 连接到 mongodb 服务
//            MongoClient mongoClient = new MongoClient("localhost", 27017);

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
            FindIterable<Document> findIterable  = mongoDatabase.getCollection("testUser").find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            while(mongoCursor.hasNext()){
                System.out.println(mongoCursor.next());
            }

            mongoClient.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

}

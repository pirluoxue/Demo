package com.example.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.common.ConsoleProgressBar;
import com.example.demo.util.mongo.Stalog;
import com.example.demo.util.snowflake.SnowFlake;
import com.google.inject.Stage;
import com.mongodb.*;
import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.LazyBSONList;
import org.bson.conversions.Bson;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.print.Doc;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class MongodbUtilTest {

    @Ignore
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

    @Ignore
    public void insertComplexTest() {
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
            for (int i = 0; i < 500000; i++) {
                List<Document> list = new ArrayList();
                for (int j = 0; j < 1000; j++) {
                    Document document = new Document();
                    document.put("name", "MyName " + i + " " + j);
                    document.put("title", "MyTitle " + i + " " + j);
                    document.put("No", "No." + i);
                    document.put("index_num", i);
                    document.put("common_num", i);
                    document.put("remark", "decription group " + i + " sub-group " + j);
                    document.put("createTime", System.currentTimeMillis());
                    document.put("desc1", UUID.randomUUID());
                    list.add(document);
                }
                mongoDatabase.getCollection("complex3").insertMany(list);
            }
            mongoClient.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void queryArray() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0, j = 0; i < 200; i++) {
            stringBuilder.append("\"No." + (i * 2500 + j) + "\", ");
        }
        String suffix = ", ";
        System.out.println(stringBuilder.replace(stringBuilder.length() - suffix.length(),
            stringBuilder.length(), "").toString());
    }

    @Test
    public void queryTest() {
        MongoClient mongoClient = getMongoClient("testdb");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("testdb");
        Document document = new Document();
        document.put("No", "No.1");
        FindIterable<Document> findIterable = mongoDatabase.getCollection("complex3").find(document);
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while (mongoCursor.hasNext()) {
            System.out.println(mongoCursor.next());
        }
        mongoClient.close();
    }

    @Test
    public void originCountTest() {
        MongoClient mongoClient = getMongoClient("testdb");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("macc");
        Document document = new Document();
        List list = new ArrayList();
        String str = "13881, 13882, 13883, 14525, 14641, 14643, 14644, 15319, 15322, 15323, 15324, 16267, 16269, 16270, 16272, 16371, 16374, 16375, 16494, 16496, 16498, 16581, 16597, 16598, 16599, 16617, 16618, 16619, 16620, 16621, 16622, 16623, 16624, 16625, 16626, 17414, 17417, 17418, 17496, 17497, 17498, 17500, 17553, 17557, 17558, 17559, 17650, 17651, 17652, 17655, 17656, 17657, 17658, 17659, 17660, 17754, 17755, 17756, 17757, 17758, 17759, 17760, 17761, 17762, 17867, 17868, 17869, 17873, 17874, 17875, 17876, 17948, 17949, 17950, 17951, 17955, 17956, 17957, 18306, 18307, 18308, 18309, 18310, 18311, 18312, 18399, 18400, 18401, 18402, 18403, 18404, 18405, 18630, 18631, 18632, 18635, 18636, 18637, 18641, 18642, 18643, 18858, 18859, 18860, 18861, 18862, 18863, 18866, 18868, 18869, 18072, 18671, 18675, 18676, 18677, 18678, 18679, 18680, 18681, 18682, 18683, 18684, 18685, 18686, 18687, 18688, 18689, 18690, 18691, 18692, 18693, 18694, 18695, 18696, 18697, 18698, 18699, 18700, 18701, 18702, 18703, 18704, 18705, 18706, 18707, 18708, 18709, 18710, 18711, 18712, 18713, 18714, 18715, 18716, 18717, 18718, 18719, 18722, 18723, 18724, 18725, 18726, 18727, 18728, 18729, 18730, 18731, 18732, 18733, 18734, 18735, 18736, 18737, 18748, 18753, 18755, 18757, 19132, 19152, 19155, 19156, 19157, 19158, 19159, 19160, 19161, 19162, 19163, 19164, 19165, 19166, 19167, 19168, 19169, 19170, 19171, 19172, 19173, 19174, 19175, 19176, 19177, 19178, 19179, 19180, 19181, 19182, 19183, 19184, 19185, 19186, 19187, 19188, 19189, 19190, 19191, 19192, 19193, 19194, 19195, 19196, 19197, 19198, 19199, 19200, 19201, 19202, 19203, 19204, 19205, 19206, 19207, 19208, 19209, 19210, 19211, 19212";
        String[] strings = str.split(", ");
        for (String num : strings) {
            list.add(Integer.parseInt(num));
        }
        document.put("groupId", new Document("$in", list));
        long beginTime;
        long count;
        long endTime;

        beginTime = System.currentTimeMillis();
        count = mongoDatabase.getCollection("onofflineUserHistory").countDocuments(document);
        endTime = System.currentTimeMillis();
        System.out.println(count);
        System.out.println("mongoDriver执行耗时： " + Duration.ofMillis(endTime - beginTime) + "s");

        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "macc");
        Query query = Query.query(Criteria.where("groupId").in(list));
        beginTime = System.currentTimeMillis();
        count = mongoTemplate.count(query, "onofflineUserHistory");
        endTime = System.currentTimeMillis();
        System.out.println(count);
        System.out.println("mongoTemplate执行耗时(热数据)： " + Duration.ofMillis(endTime - beginTime) + "s");
    }

    private MongoClient getMongoClient(String database) {
        MongoClientOptions.Builder builder = MongoClientOptions.builder(); //可以通过builder做各种详细配置
        MongoClientOptions myOptions = builder.build();
        ArrayList<ServerAddress> serverAddressList = new ArrayList();
        ServerAddress record = new ServerAddress("localhost", 27017); //IP、端口
        serverAddressList.add(record);
        //用户名、默认库名、密码
        MongoCredential credential = MongoCredential.createCredential("root", database, "123456".toCharArray());
        MongoClient mongoClient = new MongoClient(serverAddressList, credential, myOptions);
        return mongoClient;
    }

    @Ignore
    public void currentOpTest() {
        MongoClient mongoClient = getMongoClient("testdb");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
        Document document = mongoDatabase.runCommand(getCurrentOpDocument(3L));
        System.out.println("inprog: " + document.get("inprog"));
        if (document.get("inprog") != null) {
            List inprogs = (List)document.get("inprog");
            Map inprog = (Map)inprogs.get(0);
            if (inprog.get("opid") != null) {
                Long opid = Long.parseLong(inprog.get("opid").toString());
                System.out.println(opid);
                System.out.println(" kill opid " + opid);
                Document killDocument = mongoDatabase.runCommand(getKillOpDocument(opid));
                System.out.println(killDocument);
            }
        }
        mongoClient.close();
    }

    private Document getCurrentOpDocument(Long second) {
        Document currentOp = new Document();
        JSONObject options = new JSONObject();
        options.put("active", true);
        JSONObject secsRunning = new JSONObject();
        secsRunning.put("$gt", second);
        options.put("secs_running", secsRunning);
        currentOp.append("currentOp", options);
        return currentOp;
    }

    private Document getKillOpDocument(long opid) {
        Document document = new Document();
        document.append("killOp", 1);
        document.append("op", opid);
        return document;
    }

    @Ignore
    public void killOpTest() {
        MongoClient mongoClient = getMongoClient("testdb");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
        Document killOp = mongoDatabase.runCommand(getKillOpDocument(38748));
        Document document = mongoDatabase.runCommand(killOp);
        System.out.println(document);
    }

    @Ignore
    public void insertDemo() throws InterruptedException {
        MongoClient mongoClient = getMongoClient("testdb");
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "macc");
        CountDownLatch latch = new CountDownLatch(1);
        ConsoleProgressBar progressBar = new ConsoleProgressBar(20000, latch);
        Thread thread = new Thread(progressBar);
        thread.start();
        SnowFlake snowFlake = new SnowFlake(2, 3);
        for (int i = 0; i < 20000; i++) {
            List<Stalog> list = new ArrayList<>();
            for (int j = 0; j < 1000; j++) {
                Stalog stalog = new Stalog();
                stalog.setId("" + snowFlake.nextId());
                stalog.setMac("1234.1234.1234." + j);
                stalog.setActiveTime(System.currentTimeMillis());
                stalog.setOfflineTime(new Date());
                stalog.setOnlineTime(new Date());
                stalog.setAuthoffTime(new Date());
                stalog.setWifiUpDown(j % 10L);
                stalog.setUtcCode(j % 10);
                stalog.setAuthTime(new Date());
                stalog.setBand("");
                stalog.setBuildingId(i * j);
                stalog.setBuildingName("test BuildingName");
                stalog.setChannel("test ChannelName");
                stalog.setDeviceAliasName("test DeviceAliasName");
                stalog.setDeviceName("test DeviceName");
                stalog.setDhcpOption12("127.0.0.1");
                stalog.setDhcpOption55("127.0.0.1");
                stalog.setDhcpOption60("127.0.0.1");
                stalog.setDownlinkRate(100.0f);
                stalog.setUplinkRate(100.0f);
                stalog.setUpdownlinkRate(100.0f);
                stalog.setGroupId(i);
                stalog.setGroupName(i + "groupName");
                stalog.setTimeDelay(123);
                stalog.setTermidString("TermidString");
                stalog.setTermidVersion("termidVersion");
                stalog.setOsType(i + "");
                stalog.setLogType("system_info");
                stalog.setReasonSource("ReasonSource");
                stalog.setReasonCode(i * j);
                stalog.setProductType("Switch");
                list.add(stalog);
            }
            mongoTemplate.insert(list, "onofflineUserHistory");
            progressBar.setCompleteTask(i + 1);
        }
        mongoClient.close();
        latch.await();
    }

}
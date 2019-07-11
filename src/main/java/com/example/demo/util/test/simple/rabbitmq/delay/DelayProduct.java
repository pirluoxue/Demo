package com.example.demo.util.test.simple.rabbitmq.delay;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.time.LocalDateTime;

/**
 * @Classname Product
 * @Description 延迟队列 生产者
 * @Date 2019-06-20
 * @Created by chen_bq
 */
public class DelayProduct {

    private final static String QUEUE_NAME = "hello queue";
    private final static String QUEUE_EXCHANGE = "hello queue";
//    private final static String QUEUE_EXCHANGE = "checkExchange";
    private final static String DEAD_QUEUE_NAME = "Dead queue";
    private final static String DEAD_QUEUE_EXCHANGE = "Dead queue";
    private final static String ROUTE_KEY = "routeKey.*";

    private final static String EXCHANGE_TYPE = "topic";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        /*测试字段*/
        factory.setVirtualHost("/");
        factory.setUsername("test");
        factory.setPassword("123456");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String routeKey = "routeKey.test";
//        String routeKey = "checkQueue";
        String message = "Hello World!";
        for(int i = 0 ; i < 1000 ; i++){
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                    .contentEncoding("UTF-8")
                    .expiration(String.valueOf(i * 100))
                    .build();
            //参数为：exchange，routekey，basicProperties，message
            channel.basicPublish(QUEUE_EXCHANGE, routeKey, basicProperties, (message + "  " + i).getBytes("UTF-8"));
            System.out.println("P [x] Sent '" + (message + "  " + i) + "'     LocalDateTime: " + LocalDateTime.now());
        }
        channel.close();
        connection.close();
    }


}

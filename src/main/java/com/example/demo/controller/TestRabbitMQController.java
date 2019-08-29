package com.example.demo.controller;

import com.rabbitmq.client.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @Classname TestRabbitMQController
 * @Description 测试发送队列消息的控制类
 * @Date 2019-05-28
 * @Created by chen_bq
 */
@RestController
public class TestRabbitMQController {

    private final static String QUEUE_NAME = "hello";

    @RequestMapping("test/send/rabbitmq")
    public void testSendRabbitMQ() throws IOException, TimeoutException {
        System.out.println("发送rabbitMQ消息");
        ConnectionFactory factory = new ConnectionFactory();
//    factory.setHost("localhost");
        /*测试字段*/
        factory.setVirtualHost("/");
        factory.setUsername("test");
        factory.setPassword("123456");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //申明队列，将数据存储在队列中
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        String message = "Hello World!";
        for(int i = 0 ; i < 10 ; i++){
            channel.basicPublish("", QUEUE_NAME, null, (message + "  " + i).getBytes("UTF-8"));
            System.out.println("P [x] Sent '" + (message + "  " + i) + "'");
        }
        channel.close();
        connection.close();
    }

    @RequestMapping("test/receive/rabbitmq")
    public String testReceiveRabbitMQ() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
//		factory.setHost("localhost");
        factory.setHost("47.96.19.99");
        /*测试字段*/
        factory.setVirtualHost("/");
        factory.setUsername("test");
        factory.setPassword("123456");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //申明队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println("C [*] Waiting for messages. To exit press CTRL+C");
        List<String> list = new ArrayList<>();
        Consumer consumer = new DefaultConsumer(channel) {
            //从队列中“取走”数据。注意！是真正意义上的取走数据
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                list.add(message);
                System.out.println("C [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
        return list.toString();
    }


}

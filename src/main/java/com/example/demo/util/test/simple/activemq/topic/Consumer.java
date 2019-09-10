package com.example.demo.util.test.simple.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQXASslConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;
import java.sql.Timestamp;

/**
 * @author chen_bq
 * @description activeMQ 消费者模拟测试，连接方式也可以使用yml做简单配置。另外接收方式也推荐使用@jmslistener
 * @create: 2019/9/10 13:48
 **/
public class Consumer {

    private final static String TOPIC_NAME = "test_activemq_topic";

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQXASslConnectionFactory();
        connectionFactory.setBrokerURL("tcp://localhost:61616");
        connectionFactory.setUserName("admin");
        connectionFactory.setPassword("admin");
        // 获取jms连接
        Connection connection = connectionFactory.createConnection();
        // 启动连接
        connection.start();
        // 构建会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建队列
        ActiveMQTopic topic = new ActiveMQTopic(TOPIC_NAME);
        // 创建一个消费者
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(message -> {
            if (message != null){
                try {
                    // 根据实际情况的类型进行转化。实体类型可用ActiveMQObjectMessage
                    String msg = ((TextMessage) message).getText();
                    System.out.println(new Timestamp(System.currentTimeMillis()) + " received " + msg);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        // 等同于
        /*consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message != null){
                    try {
                        Object msg = ((ActiveMQObjectMessage) message).getObject();
                        if (msg instanceof String){
                            System.out.println("received " + msg.toString());
                        }
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/

    }
}

package com.example.demo.util.test.simple.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQXASslConnectionFactory;
import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.command.ActiveMQMessage;

import javax.jms.*;
import java.io.IOException;
import java.util.Map;

/**
 * @Author bangqiang_chen
 * @Description 消息状态监听
 * @Date 11:18 2020/11/6
 **/
public class Advisory {

//    private final static String QUEUE_NAME = "test_activemq_queue";
    private final static String QUEUE_NAME = "*";

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
//        Destination destination = session.createQueue(QUEUE_NAME);
        Destination all = session.createQueue("*");
        Destination advisoryDestination = AdvisorySupport.getProducerAdvisoryTopic(all);
        // 创建一个消费者
        MessageConsumer consumer = session.createConsumer(advisoryDestination);
        consumer.setMessageListener(message -> {
            if (message != null){
                ActiveMQMessage activeMQMessage = (ActiveMQMessage) message;
                System.out.println(activeMQMessage);
                try {
                    Map<String, Object> properties = activeMQMessage.getProperties();
                    System.out.println("队列: " + activeMQMessage.getDestination().getQualifiedName() + "数量: " + properties.get("producerCount"));
                    System.out.println("队列: " + activeMQMessage.getDestination().getPhysicalName() + "数量: " + properties.get("producerCount"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}

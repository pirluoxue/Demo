package com.example.demo.util.test.simple.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQXASslConnectionFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

/**
 * @author chen_bq
 * @description activeMQ 生成者模拟测试，连接方式也可以使用yml做简单配置
 * @create: 2019/9/6 15:06
 **/
public class Producter {

    private final static String QUEUE_NAME = "test_activemq_queue";

    public static void main(String[] args) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        ActiveMQConnectionFactory connectionFactory = new ActiveMQXASslConnectionFactory();
        connectionFactory.setBrokerURL("tcp://localhost:61616");
        connectionFactory.setUserName("admin");
        connectionFactory.setPassword("admin");
        // 用jmsTemplate代理发送，也可以用session组装messageProduct
        jmsTemplate.setConnectionFactory(connectionFactory);
        // 优先级
        jmsTemplate.setPriority(1);
        String message = "hello world";
        try {
            // 默认queue方式
            jmsTemplate.convertAndSend(QUEUE_NAME, message);
            System.out.println("send " + message);
        } catch (JmsException e) {
            e.printStackTrace();
        }
    }

}

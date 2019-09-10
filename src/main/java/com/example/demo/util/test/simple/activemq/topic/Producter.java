package com.example.demo.util.test.simple.activemq.topic;

import com.google.common.base.Strings;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQXASslConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

/**
 * @author chen_bq
 * @description activeMQ 生成者模拟测试，连接方式也可以使用yml做简单配置
 * @create: 2019/9/6 15:06
 **/
public class Producter {

    private final static String TOPIC_NAME = "test_activemq_topic";

    /**
     * @Author chen_bq
     * @Description 一个测试用例，注意topic类型的特点就是发布订阅，
     * 相当于“广播” 闲来没事的人听到了就听到了，但是如果这个“人”一时半会儿没空，没听到，那么这个消息就丢失了
     * @Date 2019/9/10 15:20
     */
    public static void main(String[] args) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        ActiveMQConnectionFactory connectionFactory = new ActiveMQXASslConnectionFactory();
        connectionFactory.setBrokerURL("tcp://localhost:61616");
        connectionFactory.setUserName("admin");
        connectionFactory.setPassword("admin");
        jmsTemplate.setConnectionFactory(connectionFactory);
        // 优先级
        jmsTemplate.setPriority(1);
        // 创建topic类型的destination
        ActiveMQTopic activeMQTopic = new ActiveMQTopic(TOPIC_NAME);
        // 设置默认类型，当前设置为topic
        jmsTemplate.setDefaultDestination(activeMQTopic);
        String message = "hello world by topic";
        try {
            // 仅发送模式
//            jmsTemplate.convertAndSend(message);
//            System.out.println("send " + message);
            // 仅发送
            jmsTemplate.send(session -> {
                if (!Strings.isNullOrEmpty(message)){
                    System.out.println("send " + message);
                    return session.createTextMessage(message);
                }
                return null;
            });
            // 带回调的方式，消息会等待
            /*jmsTemplate.sendAndReceive(session -> {
                if (!Strings.isNullOrEmpty(message)){
                    System.out.println("send " + message);
                    return session.createTextMessage(message);
                }
                return null;
            });*/
            // 设置非阻塞，可避免等待
//            jmsTemplate.setExplicitQosEnabled(false);

            // 带回调的方式 等同于
            /*jmsTemplate.sendAndReceive(new MessageCreator(){
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(message);
                }
            });*/
        } catch (JmsException e) {
            e.printStackTrace();
        }
    }

}

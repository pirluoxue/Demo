/**
 * TODO
 * 
 */
package com.example.demo.util.test.simple.rabbitmq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author hushuang
 *
 */
public class P {

  private final static String QUEUE_NAME = "hello";

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
}

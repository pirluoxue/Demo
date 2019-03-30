/**
 * TODO
 * 
 */
package com.example.demo.util.test.simple.rabbitmq.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * ��Ϣ������
 * 
 * @author hushuang
 * 
 */
public class C1 {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		//��������
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println("C [*] Waiting for messages. To exit press CTRL+C");

		Consumer consumer = new DefaultConsumer(channel) {
			//�Ӷ����С�ȡ�ߡ����ݡ�ע�⣡�����������ϵ�ȡ������
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("C [x] Received '" + message + "'");
			}
		};
		channel.basicConsume(QUEUE_NAME, true, consumer);
	}
}

package com.sensor.central;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        factory.setUsername("rabbituser");
        factory.setPassword("W4WNB2PtBsxPU2yg");
        channel.queueDeclare("sensor_queue", false, false, false, null);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(
                    String consumerTag,
                    Envelope envelope,
                    AMQP.BasicProperties properties,
                    byte[] body) throws IOException {

                String message = new String(body, "UTF-8");
                System.out.println(message);
            }
        };
        channel.basicConsume("sensor_queue", true, consumer);
        Thread.currentThread().join();
    }
}
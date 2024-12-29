package com.sensor.central;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class NotificationListener {
    private Channel channel;

    public void listen() throws IOException, TimeoutException, InterruptedException {
        initRabbitMQ();
        initConsumer();
        Thread.currentThread().join();
    }

    private void initRabbitMQ() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        channel = connection.createChannel();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setPort(16672);
        channel.queueDeclare("sensor_queue", false, false, false, null);
    }

    private void initConsumer() throws IOException {
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
    }
}

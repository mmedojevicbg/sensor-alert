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
        String rabbitmqUser = System.getenv("RABBITMQ_USER");
        String rabbitmqPass = System.getenv("RABBITMQ_PASS");
        String rabbitmqHost = System.getenv("RABBITMQ_HOST");
        String rabbitmqPort = System.getenv("RABBITMQ_PORT");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitmqHost != null ? rabbitmqHost : "localhost");
        factory.setUsername(rabbitmqUser != null ? rabbitmqUser : "guest");
        factory.setPassword(rabbitmqPass != null ? rabbitmqPass : "guest");
        factory.setPort(rabbitmqPort != null ? Integer.parseInt(rabbitmqPort) : 6672);
        Connection connection = factory.newConnection();
        channel = connection.createChannel();
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

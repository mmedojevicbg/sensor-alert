package com.sensor.warehouse.sensor.notifier;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQNotifier implements Notifier {
    private Channel channel;

    public RabbitMQNotifier() throws IOException, TimeoutException {
        initRabbitMQ();
    }

    @Override
    public void notify(String message) {
        try {
            channel.basicPublish("", "sensor_queue", null, message.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
}

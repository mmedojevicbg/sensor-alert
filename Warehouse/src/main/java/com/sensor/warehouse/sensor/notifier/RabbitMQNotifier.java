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
}

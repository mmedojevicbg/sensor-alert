package com.sensor.warehouse.sensor;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class SensorController implements SensorListener {
    private final Channel channel;

    public SensorController() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        channel = connection.createChannel();
        factory.setUsername("rabbituser");
        factory.setPassword("W4WNB2PtBsxPU2yg");
        channel.queueDeclare("sensor_queue", false, false, false, null);
    }

    public void run() throws Exception {
        List<SensorConfig> config = new ArrayList<>();
        config.add(new SensorConfig("heat-humidity", 4000, "127.0.0.1", 35));
        config.add(new SensorConfig("heat-humidity", 5000, "127.0.0.1", 50));

        List<AbstractSensor> sensors = new ArrayList<>();
        for(SensorConfig sensorConfig : config) {
            AbstractSensor sensor = SensorFactory.create(sensorConfig.getSensorType(),
                    sensorConfig.getPort(),
                    sensorConfig.getHost());
            sensor.setThreshold(sensorConfig.getThreshold());
            sensor.registerListener(this);
            sensors.add(sensor);
        }

        for(AbstractSensor sensor : sensors) {
            sensor.start();
        }
        for(AbstractSensor sensor : sensors) {
            sensor.join();
        }
    }

    @Override
    public void thresholdExceeded(String id, int value) {
        String message = "ALERT: Sensor threshold exceeded! Sensor ID: " + id + " value: " + value;
        System.out.println(message);
        try {
            channel.basicPublish("", "sensor_queue", null, message.getBytes());
        } catch (Exception e) {

        }
    }

    @Override
    public void thresholdRestored(String id, int value) {
        String message = "Sensor threshold restored Sensor ID: " + id + " value: " + value;
        System.out.println(message);
        try {
            channel.basicPublish("", "sensor_queue", null, message.getBytes());
        } catch (Exception e) {

        }
    }
}

package com.sensor.warehouse.sensor;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sensor.warehouse.sensor.exception.UnknownProcessorException;
import com.sensor.warehouse.sensor.exception.UnknownSensorException;
import com.sensor.warehouse.sensor.sensor.AbstractSensor;
import com.sensor.warehouse.sensor.sensor.SensorFactory;
import com.sensor.warehouse.sensor.processor.AbstractProcessor;
import com.sensor.warehouse.sensor.processor.ProcessorFactory;
import com.sensor.warehouse.sensor.processor.ProcessorSubscriber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class SensorController implements ProcessorSubscriber {
    private final List<SensorConfig> config;
    private Channel channel;
    private List<AbstractSensor> sensors;

    public SensorController(List<SensorConfig> config) {
        this.config = config;
    }

    public void run() throws UnknownProcessorException, IOException, TimeoutException, InterruptedException, UnknownSensorException {
        initRabbitMQ();
        createSensors();
        startSensorThreads();
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

    private void createSensors() throws UnknownProcessorException, UnknownSensorException {
        sensors = new ArrayList<>();
        for(SensorConfig sensorConfig : config) {
            AbstractProcessor sensor = ProcessorFactory.create(sensorConfig.getProcessor());
            sensor.setThreshold(sensorConfig.getThreshold());
            sensor.registerListener(this);
            AbstractSensor listener = SensorFactory.create(sensorConfig, sensor);
            sensors.add(listener);
        }
    }

    private void startSensorThreads() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for(AbstractSensor sensor : sensors) {
            Thread thread = new Thread(() -> {
                sensor.listen();
            });
            threads.add(thread);
            thread.start();
        }
        for(Thread thread : threads) {
            thread.join();
        }
    }

    @Override
    public void thresholdExceeded(String id, int value) {
        String message = "ALERT: Sensor threshold exceeded! Sensor ID: " + id + " value: " + value;
        System.out.println(message);
        try {
            channel.basicPublish("", "sensor_queue", null, message.getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void thresholdRestored(String id, int value) {
        String message = "Sensor threshold restored Sensor ID: " + id + " value: " + value;
        System.out.println(message);
        try {
            channel.basicPublish("", "sensor_queue", null, message.getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

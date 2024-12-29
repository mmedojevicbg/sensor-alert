package com.sensor.warehouse.sensor;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sensor.warehouse.sensor.exception.UnknownSensorTypeException;
import com.sensor.warehouse.sensor.sensor.AbstractSensor;
import com.sensor.warehouse.sensor.sensor.SensorFactory;
import com.sensor.warehouse.sensor.sensor.SensorListener;
import com.sensor.warehouse.sensor.udp.UdpListener;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class SensorController implements SensorListener {
    private Channel channel;
    private List<SensorConfig> config;
    private List<UdpListener> sensors;

    public void run() throws UnknownSensorTypeException, IOException, TimeoutException, InterruptedException {
        initRabbitMQ();
        parseYamlConfig();
        createSensors();
        startSensorThreads();
    }

    private void initRabbitMQ() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        channel = connection.createChannel();
        factory.setUsername("rabbituser");
        factory.setPassword("W4WNB2PtBsxPU2yg");
        channel.queueDeclare("sensor_queue", false, false, false, null);
    }

    private void parseYamlConfig() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("sensors.yaml");
        List<Map<String, Object>> rawConfig = yaml.load(inputStream);
        config = new ArrayList<>();
        for(Map<String, Object> rawConfigItem : rawConfig) {
            String sensorType = (String)rawConfigItem.get("sensorType");
            int port = (int)rawConfigItem.get("port");
            String host = (String)rawConfigItem.get("host");
            int threshold = (int)rawConfigItem.get("threshold");
            config.add(new SensorConfig(sensorType, port, host, threshold));
        }
    }

    private void createSensors() throws UnknownSensorTypeException {
        sensors = new ArrayList<>();
        for(SensorConfig sensorConfig : config) {
            AbstractSensor sensor = SensorFactory.create(sensorConfig.getSensorType());
            sensor.setThreshold(sensorConfig.getThreshold());
            sensor.registerListener(this);
            UdpListener udpListener = new UdpListener(sensorConfig.getHost(), sensorConfig.getPort(), sensor);
            sensors.add(udpListener);
        }
    }

    private void startSensorThreads() throws InterruptedException {
        for(UdpListener sensor : sensors) {
            sensor.start();
        }
        for(UdpListener sensor : sensors) {
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

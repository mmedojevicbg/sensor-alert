package com.sensor.warehouse.sensor;

import com.sensor.warehouse.sensor.exception.UnknownProcessorException;
import com.sensor.warehouse.sensor.exception.UnknownSensorException;
import com.sensor.warehouse.sensor.notifier.Notifier;
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
    private final Notifier notifier;
    private List<AbstractSensor> sensors;

    public SensorController(List<SensorConfig> config, Notifier notifier) {
        this.config = config;
        this.notifier = notifier;
    }

    public void run() throws UnknownProcessorException, IOException, TimeoutException, InterruptedException, UnknownSensorException {
        createSensors();
        startSensorThreads();
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
        notifier.notify(message);
    }

    @Override
    public void thresholdRestored(String id, int value) {
        String message = "Sensor threshold restored Sensor ID: " + id + " value: " + value;
        System.out.println(message);
        notifier.notify(message);
    }
}

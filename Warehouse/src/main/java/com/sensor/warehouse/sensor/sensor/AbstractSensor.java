package com.sensor.warehouse.sensor.sensor;

import com.sensor.warehouse.sensor.exception.SensorMessageParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

abstract public class AbstractSensor implements SensorPublisher {
    private static final int STATUS_OK = 1;
    private static final int STATUS_WARNING = 2;
    private int status;
    private final List<SensorListener> listeners;
    private Optional<Integer> threshold;

    public AbstractSensor() {
        this.threshold = Optional.empty();
        this.listeners = new ArrayList<>();
        this.status = STATUS_OK;
    }

    public SensorResponse processMessage(String message) throws SensorMessageParseException {
        message = message.trim();
        message = message.replace("\n", "");
        SensorResponse sensorResponse = parseInput(message);
        logMessage(sensorResponse);
        notifyListeners(sensorResponse);
        return sensorResponse;
    }

    private void notifyListeners(SensorResponse sensorResponse) {
        if(threshold.isPresent()) {
            if(sensorResponse.getValue() > threshold.get()) {
                status = STATUS_WARNING;
                notifyThresholdExceeded(sensorResponse.getSensorId(), sensorResponse.getValue());
            }
            if(status == STATUS_WARNING && sensorResponse.getValue() <= threshold.get()) {
                status = STATUS_OK;
                notifyThresholdRestored(sensorResponse.getSensorId(), sensorResponse.getValue());
            }
        }
    }

    private void logMessage(SensorResponse sensorResponse) {
        System.out.println(sensorResponse.getSensorId() + " " + sensorResponse.getValue());
    }

    abstract protected SensorResponse parseInput(String input) throws SensorMessageParseException;

    public void setThreshold(int threshold) {
        this.threshold = Optional.of(threshold);
    }

    @Override
    public void registerListener(SensorListener listener) {
        listeners.add(listener);
    }

    private void notifyThresholdExceeded(String id, int value) {
        for(SensorListener listener : listeners) {
            listener.thresholdExceeded(id, value);
        }
    }

    private void notifyThresholdRestored(String id, int value) {
        for(SensorListener listener : listeners) {
            listener.thresholdRestored(id, value);
        }
    }
}

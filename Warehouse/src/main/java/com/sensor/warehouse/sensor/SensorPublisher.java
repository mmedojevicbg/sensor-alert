package com.sensor.warehouse.sensor;

public interface SensorPublisher {
    void registerListener(SensorListener listener);
    void notifyThresholdExceeded(String id, int value);
    void notifyThresholdRestored(String id, int value);
}

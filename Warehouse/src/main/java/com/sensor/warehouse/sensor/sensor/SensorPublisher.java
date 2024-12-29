package com.sensor.warehouse.sensor.sensor;

public interface SensorPublisher {
    void registerListener(SensorListener listener);
}

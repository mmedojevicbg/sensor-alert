package com.sensor.warehouse.sensor;

public interface SensorListener {
    void thresholdExceeded(String id, int value);
    void thresholdRestored(String id, int value);
}

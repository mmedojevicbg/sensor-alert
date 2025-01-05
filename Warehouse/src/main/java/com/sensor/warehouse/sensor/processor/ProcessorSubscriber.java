package com.sensor.warehouse.sensor.processor;

public interface ProcessorSubscriber {
    void thresholdExceeded(String id, int value);
    void thresholdRestored(String id, int value);
}

package com.sensor.warehouse.sensor.processor;

public interface ProcessorPublisher {
    void registerListener(ProcessorSubscriber listener);
}

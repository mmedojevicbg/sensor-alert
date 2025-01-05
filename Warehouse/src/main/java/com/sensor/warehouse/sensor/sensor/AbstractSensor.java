package com.sensor.warehouse.sensor.sensor;

import com.sensor.warehouse.sensor.exception.SensorMessageParseException;
import com.sensor.warehouse.sensor.processor.AbstractProcessor;

public abstract class AbstractSensor {
    private final AbstractProcessor processor;

    public AbstractSensor(AbstractProcessor processor) {
        this.processor = processor;
    }

    protected void passMessageToSensor(String message) throws SensorMessageParseException {
        processor.processMessage(message);
    }

    abstract public void listen();
}

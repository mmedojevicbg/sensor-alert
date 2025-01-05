package com.sensor.warehouse.sensor.listener;

import com.sensor.warehouse.sensor.exception.SensorMessageParseException;
import com.sensor.warehouse.sensor.processor.AbstractProcessor;

public abstract class AbstractListener {
    private final AbstractProcessor sensor;

    public AbstractListener(AbstractProcessor sensor) {
        this.sensor = sensor;
    }

    protected void passMessageToSensor(String message) throws SensorMessageParseException {
        sensor.processMessage(message);
    }

    abstract public void listen();
}

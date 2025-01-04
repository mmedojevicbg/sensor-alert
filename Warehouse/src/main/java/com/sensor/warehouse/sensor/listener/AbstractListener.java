package com.sensor.warehouse.sensor.listener;

import com.sensor.warehouse.sensor.exception.SensorMessageParseException;
import com.sensor.warehouse.sensor.sensor.AbstractSensor;

public abstract class AbstractListener {
    private final AbstractSensor sensor;

    public AbstractListener(AbstractSensor sensor) {
        this.sensor = sensor;
    }

    protected void passMessageToSensor(String message) throws SensorMessageParseException {
        sensor.processMessage(message);
    }

    abstract public void listen();
}

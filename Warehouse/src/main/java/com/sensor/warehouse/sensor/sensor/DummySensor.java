package com.sensor.warehouse.sensor.sensor;

import com.sensor.warehouse.sensor.exception.SensorMessageParseException;
import com.sensor.warehouse.sensor.processor.AbstractProcessor;

public class DummySensor extends AbstractSensor {
    public DummySensor(AbstractProcessor processor) {
        super(processor);
    }

    @Override
    public void listen() {
        try {
            for(int i = 1; i <= 5; i++) {
                passMessageToProcessor("sensor_id=t3; value=" + i * 10);
                Thread.sleep(200);
            }
            passMessageToProcessor("sensor_id=t3; value=10");
        } catch (InterruptedException | SensorMessageParseException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.sensor.warehouse.sensor.sensor;

import com.sensor.warehouse.sensor.exception.UnknownSensorException;
import com.sensor.warehouse.sensor.processor.AbstractProcessor;

public final class SensorFactory {
    private SensorFactory() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    public static AbstractSensor create(String sensor, String host, Integer port, AbstractProcessor processor)
            throws UnknownSensorException {
        return switch (sensor) {
            case "udp" -> new UdpSensor(host, port, processor);
            case "dummy" -> new DummySensor(processor);
            default -> throw new UnknownSensorException("Unknown sensor type");
        };
    }
}

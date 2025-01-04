package com.sensor.warehouse.sensor.listener;

import com.sensor.warehouse.sensor.exception.UnknownSensorTypeException;
import com.sensor.warehouse.sensor.sensor.AbstractSensor;
import com.sensor.warehouse.sensor.sensor.HeatHumiditySensor;
import com.sensor.warehouse.sensor.sensor.WindSensor;

public final class ListenerFactory {
    private ListenerFactory() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    public static AbstractListener create(String listener, String host, Integer port, AbstractSensor sensor)
            throws UnknownSensorTypeException {
        return switch (listener) {
            case "udp" -> new UdpListener(host, port, sensor);
            case "dummy" -> new DummyListener(sensor);
            default -> throw new UnknownSensorTypeException("Unknown sensor type");
        };
    }
}

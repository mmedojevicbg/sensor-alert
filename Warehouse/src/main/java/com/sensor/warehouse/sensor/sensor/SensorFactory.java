package com.sensor.warehouse.sensor.sensor;

import com.sensor.warehouse.sensor.exception.UnknownSensorTypeException;

public final class SensorFactory {
    private SensorFactory() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    public static AbstractSensor create(String sensorType, int port, String host)
            throws UnknownSensorTypeException {
        return switch (sensorType) {
            case "heat-humidity" -> new HeatHumiditySensor(port, host);
            case "wind" -> new WindSensor(port, host);
            default -> throw new UnknownSensorTypeException("Unknown sensor type");
        };
    }
}

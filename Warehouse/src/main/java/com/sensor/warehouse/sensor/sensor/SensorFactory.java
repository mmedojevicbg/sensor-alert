package com.sensor.warehouse.sensor.sensor;

import com.sensor.warehouse.sensor.exception.UnknownSensorTypeException;

public final class SensorFactory {
    private SensorFactory() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    public static AbstractSensor create(String sensorType)
            throws UnknownSensorTypeException {
        return switch (sensorType) {
            case "heat-humidity" -> new HeatHumiditySensor();
            case "wind" -> new WindSensor();
            default -> throw new UnknownSensorTypeException("Unknown sensor type");
        };
    }
}

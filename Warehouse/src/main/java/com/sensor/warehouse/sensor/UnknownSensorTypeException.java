package com.sensor.warehouse.sensor;

public class UnknownSensorTypeException extends Exception {
    public UnknownSensorTypeException(String message) {
        super(message);
    }
}

package com.sensor.warehouse.sensor.sensor;

import com.sensor.warehouse.sensor.exception.SensorMessageParseException;

public class WindSensor extends AbstractSensor {
    protected SensorResponse parseInput(String input) throws SensorMessageParseException {
        String[] parts = input.split(";");
        String sensorId = parts[0];
        String value = parts[1];
        SensorResponse sensorResponse = new SensorResponse();
        sensorResponse.setSensorId(sensorId);
        sensorResponse.setValue(Integer.parseInt(value));
        return sensorResponse;
    }
}

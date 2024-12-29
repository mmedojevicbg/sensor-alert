package com.sensor.warehouse.sensor;

public class WindSensor extends AbstractSensor {

    public WindSensor(int port, String host) {
        super(port, host);
    }

    protected SensorResponse parseInput(String input) {
        String[] parts = input.split(";");
        String sensorId = parts[0];
        String value = parts[1];
        SensorResponse sensorResponse = new SensorResponse();
        sensorResponse.setSensorId(sensorId);
        sensorResponse.setValue(Integer.parseInt(value));
        return sensorResponse;
    }
}

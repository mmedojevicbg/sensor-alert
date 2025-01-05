package com.sensor.warehouse.sensor.processor;

import com.sensor.warehouse.sensor.exception.SensorMessageParseException;

public class WindProcessor extends AbstractProcessor {
    protected ProcessorResponse parseInput(String input) throws SensorMessageParseException {
        String[] parts = input.split(";");
        String sensorId = parts[0];
        String value = parts[1];
        ProcessorResponse processorResponse = new ProcessorResponse();
        processorResponse.setSensorId(sensorId);
        processorResponse.setValue(Integer.parseInt(value));
        return processorResponse;
    }
}

package com.sensor.warehouse.sensor.processor;

import com.sensor.warehouse.sensor.exception.SensorMessageParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeatHumidityProcessor extends AbstractProcessor {
    protected ProcessorResponse parseInput(String input) throws SensorMessageParseException {
        String regex = "sensor_id=([a-zA-Z0-9]+); value=([0-9]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        ProcessorResponse processorResponse = new ProcessorResponse();
        if (matcher.find()) {
            String sensorId = matcher.group(1);
            String value = matcher.group(2);
            processorResponse.setSensorId(sensorId);
            processorResponse.setValue(Integer.parseInt(value));
        } else {
            throw new SensorMessageParseException("Incorrect sensor message format. Correct format is " + regex);
        }
        return processorResponse;
    }
}

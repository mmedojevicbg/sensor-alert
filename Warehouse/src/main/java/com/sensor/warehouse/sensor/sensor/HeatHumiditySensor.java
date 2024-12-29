package com.sensor.warehouse.sensor.sensor;

import com.sensor.warehouse.sensor.exception.SensorMessageParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeatHumiditySensor extends AbstractSensor {

    public HeatHumiditySensor(int port, String host) {
        super(port, host);
    }

    protected SensorResponse parseInput(String input) throws SensorMessageParseException {
        String regex = "sensor_id=([a-zA-Z0-9]+); value=([0-9]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        SensorResponse sensorResponse = new SensorResponse();
        if (matcher.find()) {
            String sensorId = matcher.group(1);
            String value = matcher.group(2);
            sensorResponse.setSensorId(sensorId);
            sensorResponse.setValue(Integer.parseInt(value));
        } else {
            throw new SensorMessageParseException("Incorrect sensor message format. Correct format is " + regex);
        }
        return sensorResponse;
    }
}

package com.sensor.warehouse.sensor.sensor;

import com.sensor.warehouse.sensor.exception.SensorMessageParseException;
import com.sensor.warehouse.sensor.processor.AbstractProcessor;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DummySensor extends AbstractSensor {
    public DummySensor(AbstractProcessor processor) {
        super(processor);
    }

    @Override
    public void listen() {
        Random rand = new Random();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run () {
                try {
                    passMessageToSensor("sensor_id=t3; value=" + rand.nextInt(20, 50));
                } catch (SensorMessageParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 1000, 5000);
    }
}

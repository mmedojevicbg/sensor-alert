package com.sensor.warehouse.sensor.sensor;

import com.sensor.warehouse.sensor.SensorConfig;
import com.sensor.warehouse.sensor.exception.UnknownSensorException;
import com.sensor.warehouse.sensor.processor.AbstractProcessor;

public final class SensorFactory {
    private SensorFactory() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    public static AbstractSensor create(SensorConfig config, AbstractProcessor processor)
            throws UnknownSensorException {
        return switch (config.getSensor()) {
            case "udp" -> new UdpSensor(config.getHost(), config.getPort(), processor);
            case "dummy" -> new DummySensor(processor);
            case "socket" -> new SocketSensor(config.getPort(), processor);
            default -> throw new UnknownSensorException("Unknown sensor type");
        };
    }
}

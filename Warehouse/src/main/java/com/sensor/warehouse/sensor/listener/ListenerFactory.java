package com.sensor.warehouse.sensor.listener;

import com.sensor.warehouse.sensor.exception.UnknownProcessorException;
import com.sensor.warehouse.sensor.processor.AbstractProcessor;

public final class ListenerFactory {
    private ListenerFactory() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    public static AbstractListener create(String listener, String host, Integer port, AbstractProcessor sensor)
            throws UnknownProcessorException {
        return switch (listener) {
            case "udp" -> new UdpListener(host, port, sensor);
            case "dummy" -> new DummyListener(sensor);
            default -> throw new UnknownProcessorException("Unknown sensor type");
        };
    }
}

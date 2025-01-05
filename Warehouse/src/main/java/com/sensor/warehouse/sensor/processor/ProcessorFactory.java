package com.sensor.warehouse.sensor.processor;

import com.sensor.warehouse.sensor.exception.UnknownProcessorException;

public final class ProcessorFactory {
    private ProcessorFactory() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    public static AbstractProcessor create(String processor)
            throws UnknownProcessorException {
        return switch (processor) {
            case "heat-humidity" -> new HeatHumidityProcessor();
            case "wind" -> new WindProcessor();
            default -> throw new UnknownProcessorException("Unknown processor type");
        };
    }
}

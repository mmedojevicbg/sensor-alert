package com.sensor.warehouse.sensor.processor;

import com.sensor.warehouse.sensor.exception.SensorMessageParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

abstract public class AbstractProcessor implements ProcessorPublisher {
    private static final int STATUS_OK = 1;
    private static final int STATUS_WARNING = 2;
    private int status;
    private final List<ProcessorSubscriber> listeners;
    private Optional<Integer> threshold;

    public AbstractProcessor() {
        this.threshold = Optional.empty();
        this.listeners = new ArrayList<>();
        this.status = STATUS_OK;
    }

    public ProcessorResponse processMessage(String message) throws SensorMessageParseException {
        message = sanitizeInput(message);
        ProcessorResponse processorResponse = parseInput(message);
        logMessage(processorResponse);
        notifyListeners(processorResponse);
        return processorResponse;
    }

    private String sanitizeInput(String message) {
        message = message.trim();
        message = message.replace("\n", "");
        return message;
    }

    private void notifyListeners(ProcessorResponse processorResponse) {
        if(threshold.isPresent()) {
            if(processorResponse.getValue() > threshold.get()) {
                status = STATUS_WARNING;
                notifyThresholdExceeded(processorResponse.getSensorId(), processorResponse.getValue());
            }
            if(status == STATUS_WARNING && processorResponse.getValue() <= threshold.get()) {
                status = STATUS_OK;
                notifyThresholdRestored(processorResponse.getSensorId(), processorResponse.getValue());
            }
        }
    }

    private void logMessage(ProcessorResponse processorResponse) {
        System.out.println(processorResponse.getSensorId() + " " + processorResponse.getValue());
    }

    abstract protected ProcessorResponse parseInput(String input) throws SensorMessageParseException;

    public void setThreshold(int threshold) {
        this.threshold = Optional.of(threshold);
    }

    @Override
    public void registerListener(ProcessorSubscriber listener) {
        listeners.add(listener);
    }

    private void notifyThresholdExceeded(String id, int value) {
        for(ProcessorSubscriber listener : listeners) {
            listener.thresholdExceeded(id, value);
        }
    }

    private void notifyThresholdRestored(String id, int value) {
        for(ProcessorSubscriber listener : listeners) {
            listener.thresholdRestored(id, value);
        }
    }
}

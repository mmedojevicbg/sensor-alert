package com.sensor.warehouse.sensor;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

abstract public class AbstractSensor extends Thread implements SensorPublisher {
    private static final int STATUS_OK = 1;
    private static final int STATUS_WARNING = 2;
    private int status;
    private final int port;
    private final String host;
    private final List<SensorListener> listeners;
    private Optional<Integer> threshold;
    private DatagramSocket socket;


    public AbstractSensor(int port, String host) {
        this.port = port;
        this.host = host;
        this.threshold = Optional.empty();
        this.listeners = new ArrayList<>();
        this.status = STATUS_OK;
    }

    public void run() {
        try {
            initSocket();
            listenForMessages();
        } catch (IOException | SensorMessageParseException e) {
            System.out.println("ERROR:" + e.getMessage());
        }
    }

    private void initSocket() throws UnknownHostException, SocketException {
        InetAddress address = InetAddress.getByName(host);
        socket = new DatagramSocket(port, address);
    }

    private void listenForMessages() throws IOException, SensorMessageParseException {
        byte[] buf = new byte[256];
        while (true) {
            try {
                SensorResponse sensorResponse = parseMessage(buf);
                logMessage(sensorResponse);
                notifyListeners(sensorResponse);
            } catch (SensorMessageParseException e) {
                System.out.println("Error:" + e.getMessage());
            }
        }
    }

    private SensorResponse parseMessage(byte[] buf) throws IOException, SensorMessageParseException {
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String received
                = new String(packet.getData(), 0, packet.getLength());
        received = received.trim();
        received = received.replace("\n", "");
        return parseInput(received);
    }

    private void notifyListeners(SensorResponse sensorResponse) {
        if(threshold.isPresent()) {
            if(sensorResponse.getValue() > threshold.get()) {
                status = STATUS_WARNING;
                notifyThresholdExceeded(sensorResponse.getSensorId(), sensorResponse.getValue());
            }
            if(status == STATUS_WARNING && sensorResponse.getValue() <= threshold.get()) {
                status = STATUS_OK;
                notifyThresholdRestored(sensorResponse.getSensorId(), sensorResponse.getValue());
            }
        }
    }

    private void logMessage(SensorResponse sensorResponse) {
        System.out.println(sensorResponse.getSensorId() + " " + sensorResponse.getValue());
    }

    abstract protected SensorResponse parseInput(String input) throws SensorMessageParseException;

    public void setThreshold(int threshold) {
        this.threshold = Optional.of(threshold);
    }

    @Override
    public void registerListener(SensorListener listener) {
        listeners.add(listener);
    }

    @Override
    public void notifyThresholdExceeded(String id, int value) {
        for(SensorListener listener : listeners) {
            listener.thresholdExceeded(id, value);
        }
    }

    @Override
    public void notifyThresholdRestored(String id, int value) {
        for(SensorListener listener : listeners) {
            listener.thresholdRestored(id, value);
        }
    }
}

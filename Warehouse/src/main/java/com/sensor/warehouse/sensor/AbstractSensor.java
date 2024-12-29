package com.sensor.warehouse.sensor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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

    public AbstractSensor(int port, String host) {
        this.port = port;
        this.host = host;
        this.threshold = Optional.empty();
        this.listeners = new ArrayList<>();
        this.status = STATUS_OK;
    }

    public void run() {
        DatagramSocket socket = null;
        DatagramPacket packet = null;
        byte[] buf = new byte[256];
        try {
            InetAddress address = InetAddress.getByName (host);
            socket = new DatagramSocket(port, address);
            while (true) {
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received
                        = new String(packet.getData(), 0, packet.getLength());
                received = received.trim();
                received = received.replace("\n", "");
                SensorResponse sensorResponse = parseInput(received);
                System.out.println(sensorResponse.getSensorId() + " " + sensorResponse.getValue());
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
        } catch (Exception e) {

        }
    }

    abstract protected SensorResponse parseInput(String input);

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

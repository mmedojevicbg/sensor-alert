package com.sensor.warehouse.sensor.listener;

import com.sensor.warehouse.sensor.exception.SensorMessageParseException;
import com.sensor.warehouse.sensor.processor.AbstractProcessor;

import java.io.IOException;
import java.net.*;

public class UdpListener extends AbstractListener {
    private final int port;
    private final String host;
    private DatagramSocket socket;

    public UdpListener(String host, int port, AbstractProcessor sensor) {
        super(sensor);
        this.host = host;
        this.port = port;
    }

    @Override
    public void listen() {
        try {
            initSocket();
            listenForMessages();
        } catch (IOException | SensorMessageParseException e) {
            System.out.println("ERROR:" + e.getMessage());
        }
    }

    private void listenForMessages() throws IOException, SensorMessageParseException {
        byte[] buf = new byte[256];
        while (true) {
            try {
                parseMessage(buf);
            } catch (SensorMessageParseException e) {
                System.out.println("Error:" + e.getMessage());
            }
        }
    }

    private void parseMessage(byte[] buf) throws IOException, SensorMessageParseException {
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String received
                = new String(packet.getData(), 0, packet.getLength());
        passMessageToSensor(received);
    }

    private void initSocket() throws UnknownHostException, SocketException {
        InetAddress address = InetAddress.getByName(host);
        socket = new DatagramSocket(port, address);
    }
}

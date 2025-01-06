package com.sensor.warehouse.sensor.sensor;

import com.sensor.warehouse.sensor.exception.SensorMessageParseException;
import com.sensor.warehouse.sensor.processor.AbstractProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketSensor extends AbstractSensor {
    private final int port;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;

    public SocketSensor(int port, AbstractProcessor processor) {
        super(processor);
        this.port = port;
    }

    @Override
    public void listen() {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        passMessageToProcessor(line);
                    }
                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException | SensorMessageParseException e) {
            throw new RuntimeException(e);
        }
    }
}

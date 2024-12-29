package com.sensor.central;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
       NotificationListener notificationListener = new NotificationListener();
       notificationListener.listen();
    }
}
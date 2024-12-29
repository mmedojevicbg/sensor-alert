package com.sensor.warehouse;

import com.sensor.warehouse.sensor.SensorController;

public class Main {
    public static void main(String[] args) throws Exception {
        SensorController sensorController = new SensorController();
        sensorController.run();
    }
}
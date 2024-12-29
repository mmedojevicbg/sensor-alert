package com.sensor.warehouse.sensor.sensor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SensorResponse {
    private String sensorId;
    private int value;
}

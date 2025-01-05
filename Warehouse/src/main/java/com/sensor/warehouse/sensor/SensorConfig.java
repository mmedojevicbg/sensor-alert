package com.sensor.warehouse.sensor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SensorConfig {
    private String processor;
    private String sensor;
    private Integer port;
    private String host;
    private Integer threshold;
}

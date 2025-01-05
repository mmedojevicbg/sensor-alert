package com.sensor.warehouse;

import com.sensor.warehouse.sensor.SensorConfig;
import com.sensor.warehouse.sensor.SensorController;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        SensorController sensorController = new SensorController(parseYamlConfig());
        sensorController.run();
    }

    private static List<SensorConfig> parseYamlConfig() {
        Yaml yaml = new Yaml();
        InputStream inputStream = Main.class
                .getClassLoader()
                .getResourceAsStream("sensors.yaml");
        List<Map<String, Object>> rawConfig = yaml.load(inputStream);
        List<SensorConfig> config = new ArrayList<>();
        for(Map<String, Object> rawConfigItem : rawConfig) {
            String processor = (String)rawConfigItem.get("processor");
            String sensor = (String)rawConfigItem.get("sensor");
            Integer port = (Integer)rawConfigItem.get("port");
            String host = (String)rawConfigItem.get("host");
            Integer threshold = (Integer)rawConfigItem.get("threshold");
            config.add(new SensorConfig(processor, sensor, port, host, threshold));
        }
        return config;
    }
}
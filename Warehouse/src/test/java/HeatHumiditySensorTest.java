import com.sensor.warehouse.sensor.exception.SensorMessageParseException;
import com.sensor.warehouse.sensor.sensor.AbstractSensor;
import com.sensor.warehouse.sensor.sensor.HeatHumiditySensor;
import com.sensor.warehouse.sensor.sensor.SensorListener;
import com.sensor.warehouse.sensor.sensor.SensorResponse;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HeatHumiditySensorTest {
    AbstractSensor sensor;
    int threshold = 50;

    @BeforeEach
    void init() {
        sensor = new HeatHumiditySensor();
        sensor.setThreshold(threshold);
    }

    @Test
    @Order(1)
    void test_parse() throws SensorMessageParseException {
        SensorResponse sensorResponse = sensor.processMessage("sensor_id=t1; value=34");
        assertEquals("t1", sensorResponse.getSensorId());
        assertEquals(34, sensorResponse.getValue());
    }

    @Test
    @Order(2)
    void test_parseException() {
        assertThrows(SensorMessageParseException.class, () ->
                sensor.processMessage("malformed message 1234"));
    }

    @Test
    @Order(3)
    void test_callback() throws SensorMessageParseException {
        int aboveThreshold = threshold + 5;
        int belowThreshold = threshold - 5;
        Listener listener = new Listener();
        sensor.registerListener(listener);
        assertFalse(listener.thresholdExceeded);
        sensor.processMessage("sensor_id=t1; value=" + aboveThreshold);
        assertTrue(listener.thresholdExceeded);
        sensor.processMessage("sensor_id=t1; value=" + belowThreshold);
        assertFalse(listener.thresholdExceeded);
    }

    class Listener implements SensorListener {
        public boolean thresholdExceeded = false;
        @Override
        public void thresholdExceeded(String id, int value) {
            thresholdExceeded = true;
        }

        @Override
        public void thresholdRestored(String id, int value) {
            thresholdExceeded = false;
        }
    }

}

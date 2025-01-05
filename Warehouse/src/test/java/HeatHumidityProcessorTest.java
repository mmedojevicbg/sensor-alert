import com.sensor.warehouse.sensor.exception.SensorMessageParseException;
import com.sensor.warehouse.sensor.processor.AbstractProcessor;
import com.sensor.warehouse.sensor.processor.HeatHumidityProcessor;
import com.sensor.warehouse.sensor.processor.ProcessorSubscriber;
import com.sensor.warehouse.sensor.processor.ProcessorResponse;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HeatHumidityProcessorTest {
    AbstractProcessor processor;
    int threshold = 50;

    @BeforeEach
    void init() {
        processor = new HeatHumidityProcessor();
        processor.setThreshold(threshold);
    }

    @Test
    @Order(1)
    void test_parse() throws SensorMessageParseException {
        ProcessorResponse processorResponse = processor.processMessage("sensor_id=t1; value=34");
        assertEquals("t1", processorResponse.getSensorId());
        assertEquals(34, processorResponse.getValue());
    }

    @Test
    @Order(2)
    void test_parseException() {
        assertThrows(SensorMessageParseException.class, () ->
                processor.processMessage("malformed message 1234"));
    }

    @Test
    @Order(3)
    void test_callback() throws SensorMessageParseException {
        int aboveThreshold = threshold + 5;
        int belowThreshold = threshold - 5;
        Listener listener = new Listener();
        processor.registerListener(listener);
        assertFalse(listener.thresholdExceeded);
        processor.processMessage("sensor_id=t1; value=" + aboveThreshold);
        assertTrue(listener.thresholdExceeded);
        processor.processMessage("sensor_id=t1; value=" + belowThreshold);
        assertFalse(listener.thresholdExceeded);
    }

    class Listener implements ProcessorSubscriber {
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

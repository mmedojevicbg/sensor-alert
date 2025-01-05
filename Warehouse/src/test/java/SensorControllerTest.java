import com.sensor.warehouse.sensor.SensorConfig;
import com.sensor.warehouse.sensor.SensorController;
import com.sensor.warehouse.sensor.exception.SensorMessageParseException;
import com.sensor.warehouse.sensor.exception.UnknownProcessorException;
import com.sensor.warehouse.sensor.exception.UnknownSensorException;
import com.sensor.warehouse.sensor.notifier.Notifier;
import com.sensor.warehouse.sensor.processor.AbstractProcessor;
import com.sensor.warehouse.sensor.processor.ProcessorResponse;
import com.sensor.warehouse.sensor.sensor.AbstractSensor;
import com.sensor.warehouse.sensor.sensor.DummySensor;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SensorControllerTest {
    TestNotifier notifier;
    SensorController sensorController;

    @BeforeEach
    void init() throws UnknownProcessorException, UnknownSensorException, IOException, InterruptedException, TimeoutException {
        notifier = new TestNotifier();
        List<SensorConfig> config = new ArrayList<>();
        config.add(new SensorConfig("heat-humidity", "dummy", null, null, 20));
        config.add(new SensorConfig("heat-humidity", "dummy", null, null, 30));
        sensorController = new SensorController(config, notifier);
        sensorController.run();
    }

    @Test
    @Order(1)
    void test_message_count() throws SensorMessageParseException {
        assertEquals(7, notifier.messages.size());
    }

    @Test
    @Order(2)
    void test_exceeded_message_count() throws SensorMessageParseException {
        assertEquals(5, notifier.messages.stream().filter(x -> x.contains("exceeded")).count());
    }

    @Test
    @Order(3)
    void test_restored_message_count() throws SensorMessageParseException {
        assertEquals(2, notifier.messages.stream().filter(x -> x.contains("restored")).count());
    }

    class TestNotifier implements Notifier {
        public List<String> messages = new ArrayList<>();
        @Override
        public void notify(String message) {
            messages.add(message);
        }
    }
}

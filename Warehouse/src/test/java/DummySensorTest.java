import com.sensor.warehouse.sensor.exception.SensorMessageParseException;
import com.sensor.warehouse.sensor.processor.AbstractProcessor;
import com.sensor.warehouse.sensor.processor.HeatHumidityProcessor;
import com.sensor.warehouse.sensor.processor.ProcessorResponse;
import com.sensor.warehouse.sensor.processor.ProcessorSubscriber;
import com.sensor.warehouse.sensor.sensor.AbstractSensor;
import com.sensor.warehouse.sensor.sensor.DummySensor;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DummySensorTest {
    AbstractSensor sensor;
    TestProcessor processor;

    @BeforeEach
    void init() {
        processor = new TestProcessor();
        sensor = new DummySensor(processor);
    }

    @Test
    @Order(1)
    void test_listen() throws SensorMessageParseException {
        sensor.listen();
        assertEquals(5, processor.messages.size());
        assertEquals("sensor_id=t3; value=10", processor.messages.get(0));
        assertEquals("sensor_id=t3; value=20", processor.messages.get(1));
        assertEquals("sensor_id=t3; value=30", processor.messages.get(2));
        assertEquals("sensor_id=t3; value=40", processor.messages.get(3));
        assertEquals("sensor_id=t3; value=50", processor.messages.get(4));

    }

    class TestProcessor extends AbstractProcessor {
        public List<String> messages = new ArrayList<>();

        @Override
        protected ProcessorResponse parseInput(String input) throws SensorMessageParseException {
            messages.add(input);
            return new ProcessorResponse("t5", 20);
        }
    }
}

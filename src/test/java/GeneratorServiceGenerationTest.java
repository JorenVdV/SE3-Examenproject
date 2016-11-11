import be.kdg.se3.transportss.generation.entity.FrequencySchema;
import be.kdg.se3.transportss.generation.GeneratorServiceGeneration;
import be.kdg.se3.transportss.simulator.entity.Luggage;
import be.kdg.se3.transportss.simulator.service.GeneratorListener;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Joren Van de Vondel on 11/4/2016.
 */
public class GeneratorServiceGenerationTest {
    private GeneratorServiceGeneration generation;
    private GeneratorListener mockListener1;
    private GeneratorListener mockListener2;

    private final int testcount = 10;

    @Before
    public void setUp() throws Exception {
        List<String> flightIDS = Arrays.asList("1111111", "1111112");
        Map<Integer, List<Integer>> conveyorIDsWithSensorIDs = new HashMap<>();
        conveyorIDsWithSensorIDs.put(11, Arrays.asList(1, 2));
        conveyorIDsWithSensorIDs.put(12, Arrays.asList(3));

        FrequencySchema frequenceSchema = new FrequencySchema(10);
        generation = new GeneratorServiceGeneration(
                flightIDS, conveyorIDsWithSensorIDs, frequenceSchema, LocalDateTime.now(), testcount);
        mockListener1 = mock(GeneratorListener.class);
        mockListener2 = mock(GeneratorListener.class);
    }

    @Test
    public void Test1Listener() throws Exception {
        generation.addGenerationListener(mockListener1);

        Thread tr = new Thread(generation);
        tr.start();
        tr.join();

        verify(mockListener1, times(testcount)).notify(any(Luggage.class));
        verify(mockListener2, never()).notify(any(Luggage.class));
    }

    @Test
    public void Test2Listeners() throws Exception {
        generation.addGenerationListener(mockListener1);
        generation.addGenerationListener(mockListener2);

        Thread tr = new Thread(generation);
        tr.start();
        tr.join();

        verify(mockListener1, times(testcount)).notify(any(Luggage.class));
        verify(mockListener2, times(testcount)).notify(any(Luggage.class));
    }

    @Test
    public void TestWriteFile() throws Exception {
        generation.enableRecording("TestWriteFile.js");

        Thread tr = new Thread(generation);
        tr.start();
        tr.join();

        verify(mockListener1, never()).notify(any(Luggage.class));
        verify(mockListener2, never()).notify(any(Luggage.class));
    }
}

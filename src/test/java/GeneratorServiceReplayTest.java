import be.kdg.se3.transportss.generation.GeneratorServiceReplay;
import be.kdg.se3.transportss.simulator.entity.Luggage;
import be.kdg.se3.transportss.simulator.service.GeneratorService;
import be.kdg.se3.transportss.simulator.service.GeneratorListener;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by Joren Van de Vondel on 11/4/2016.
 */
@SuppressWarnings("Duplicates")
public class GeneratorServiceReplayTest {

    private GeneratorService generation;
    private GeneratorListener mockGenerationListener1;
    private GeneratorListener mockGenerationListener2;

    private final int testcount = 10;

    @Before
    public void setUp() throws Exception {
        generation = new GeneratorServiceReplay("TestWriteFile.js", LocalDateTime.now());
        mockGenerationListener1 = mock(GeneratorListener.class);
        mockGenerationListener2 = mock(GeneratorListener.class);
    }

    @Test
    public void Test1Listener() throws Exception {
        generation.addGenerationListener(mockGenerationListener1);

        Thread tr = new Thread(generation);
        tr.start();
        tr.join();

        verify(mockGenerationListener1, times(testcount)).notify(any(Luggage.class));
        verify(mockGenerationListener2, never()).notify(any(Luggage.class));
    }

    @Test
    public void Test2Listeners() throws Exception {
        generation.addGenerationListener(mockGenerationListener1);
        generation.addGenerationListener(mockGenerationListener2);

        Thread tr = new Thread(generation);
        tr.start();
        tr.join();

        verify(mockGenerationListener1, times(testcount)).notify(any(Luggage.class));
        verify(mockGenerationListener2, times(testcount)).notify(any(Luggage.class));
    }
}

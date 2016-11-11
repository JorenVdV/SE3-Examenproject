import be.kdg.se3.transportss.generation.GeneratorServiceReplay;
import be.kdg.se3.transportss.routing.RouteLuggageStrategyFixed;
import be.kdg.se3.transportss.simulator.Simulator;
import be.kdg.se3.transportss.simulator.entity.NewLuggageMessage;
import be.kdg.se3.transportss.simulator.service.GeneratorService;
import be.kdg.se3.transportss.simulator.service.NewLuggageMessageService;
import be.kdg.se3.transportss.simulator.service.RouteLuggageStrategy;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by Joren Van de Vondel on 11/4/2016.
 */
public class SimpleSimulatorTest {

    private Simulator simulator;
    private RouteLuggageStrategy routeLuggageStrategy;
    private GeneratorService generation;
    private NewLuggageMessageService newLuggageMessageService;

    private final int testcount = 10;

    @Before
    public void setUp() throws Exception {
        LocalDateTime startup = LocalDateTime.now();
        this.routeLuggageStrategy = new RouteLuggageStrategyFixed(1000);
        this.simulator = new Simulator(startup, routeLuggageStrategy);
        this.generation = new GeneratorServiceReplay("TestWriteFile.js",startup);
        this.newLuggageMessageService = mock(NewLuggageMessageService.class);
    }

    @Test
    public void SimpleTest() throws Exception {
        this.simulator.subscribe(this.generation);
        this.simulator.addNewLuggageMessageService(this.newLuggageMessageService);

        Thread tr = new Thread(generation);
        tr.start();
        tr.join();

        verify(newLuggageMessageService, times(testcount)).notify(any(NewLuggageMessage.class));
    }
}

package be.kdg.se3.transportss.generation;

import be.kdg.se3.transportss.simulator.entity.Luggage;
import be.kdg.se3.transportss.simulator.service.GenerationException;
import be.kdg.se3.transportss.simulator.service.GeneratorListener;
import be.kdg.se3.transportss.simulator.service.GeneratorService;
import be.kdg.se3.transportss.utility.JSONSerializer;
import be.kdg.se3.transportss.utility.SerializationException;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Replay implementation of the GeneratorService,
 * reads a file with jsonlist of luggageList and schedules release for them
 *
 * Created by Joren Van de Vondel on 11/1/2016.
 */
public class GeneratorServiceReplay implements GeneratorService {
    private List<Luggage> luggageList;
    private List<GeneratorListener> listeners = new ArrayList<>();
    private final LocalDateTime startup;

    private final Logger logger = LoggerFactory.getLogger(GeneratorServiceReplay.class);

    public GeneratorServiceReplay(String filename, LocalDateTime startup) throws GenerationException {
        logger.info("Initializing");
        Type LuggageList = new TypeToken<List<Luggage>>(){}.getType();
        try {
            this.luggageList = JSONSerializer.ReadFromFile(filename, LuggageList);
            logger.debug("Read "+luggageList.size()+" files from file "+filename);
        }catch (SerializationException se){
            logger.error("Error deserializing objects");
            throw new GenerationException("Could not Construct the Replay generator", se);
        }
        this.startup = startup;
    }

    @Override
    public void addGenerationListener(GeneratorListener iGenerationListener) {
        listeners.add(iGenerationListener);
    }

    @Override
    public void run() throws GenerationException {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(luggageList.size());
        // Schedule each luggage to be 'released' on his timestamp
        luggageList.forEach(l -> executorService.schedule(() -> listeners.forEach(listener -> listener.notify(l)),
                l.getTimestamp(), TimeUnit.MILLISECONDS));

        executorService.shutdown();
        try {
            // wait for all the luggageList to be released an then finish thread
            executorService.awaitTermination(luggageList.stream().mapToLong(l -> l.getTimestamp()).max().getAsLong()+100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
           throw new GenerationException("Generation interrupted", e);
        }
    }
}

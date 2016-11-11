package be.kdg.se3.transportss.generation;

import be.kdg.se3.transportss.generation.entity.FrequencySchema;
import be.kdg.se3.transportss.simulator.entity.Luggage;
import be.kdg.se3.transportss.simulator.service.GenerationException;
import be.kdg.se3.transportss.simulator.service.GeneratorService;
import be.kdg.se3.transportss.simulator.service.GeneratorListener;
import be.kdg.se3.transportss.utility.JSONSerializer;
import be.kdg.se3.transportss.utility.RandomExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generation implementation for the GeneratorService
 *
 * Created by Joren Van de Vondel on 11/1/2016.
 */
public class GeneratorServiceGeneration implements GeneratorService {

    public enum Mode {RECORD, NORECORD}

    //region members
    // listeners
    private List<GeneratorListener> listeners = new ArrayList<>();

    // data for generation
    private int luggageID = 0;
    private final List<String> flightIDs;
    private final Map<Integer, List<Integer>> conveyorIDsWithSensorIDs;
    private final FrequencySchema frequencySchema;
    private final LocalDateTime startup;
    private int leftToGenerate;

    // control
    private boolean stop = false;
    private Mode mode;
    private String filename;
    private List<Luggage> luggageRecorder = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(GeneratorServiceGeneration.class);

    //endregion

    /**
     *
     * @param flightIDs
     * @param conveyorIDsWithSensorIDs
     * @param frequenceSchema
     * @param startup
     * @param leftToGenerate set to -1 for infinite mode
     */
    public GeneratorServiceGeneration(
            List<String> flightIDs,
            Map<Integer, List<Integer>> conveyorIDsWithSensorIDs,
            FrequencySchema frequenceSchema,
            LocalDateTime startup,
            int leftToGenerate) {
        this.flightIDs = flightIDs;
        this.conveyorIDsWithSensorIDs = conveyorIDsWithSensorIDs;
        this.frequencySchema = frequenceSchema;
        this.mode = Mode.NORECORD;
        this.startup = startup;
        this.leftToGenerate = leftToGenerate;
    }

    /**
     * Enables recording to a filename, generator only writes all the objects on shutdown
     * @param filename file to write to (make sure directory exist if one is specified)
     */
    public void enableRecording(String filename){
        logger.debug("Enabled recording to file:"+filename);
        mode = Mode.RECORD;
        this.filename = filename;
        logger.info("Recording enabled");
    }

    @Override
    public void addGenerationListener(GeneratorListener iGenerationListener) {
        this.listeners.add(iGenerationListener);
    }

    /**
     * Only needs to be called when the generator was set with leftToGenerate = -1
     */
    public void stop() {
        logger.info("Manually stopped generation");
        this.stop = true;
    }

    @Override
    public void run() throws GenerationException{
        logger.info("Starting Generation");
        while (!stop && leftToGenerate != 0) {
            if(leftToGenerate>0)leftToGenerate--;
            // generate new luggage object
            String flightid = RandomExtension.getRandomFromList(flightIDs);
            int conveyorid = RandomExtension.getRandomFromList(
                    conveyorIDsWithSensorIDs.keySet().stream().collect(Collectors.toList()));
            int sensorid = RandomExtension.getRandomFromList(
                    conveyorIDsWithSensorIDs.get(conveyorid));
            long timestamp = startup.until(LocalDateTime.now(), ChronoUnit.MILLIS);
            Luggage luggage = new Luggage(luggageID++, flightid, conveyorid, sensorid, timestamp);
            this.logger.debug("Created LuggageObject: "+ luggage);
            if(this.mode==Mode.RECORD)this.luggageRecorder.add(luggage);
            listeners.forEach(l -> l.notify(luggage));
            try {
                Thread.sleep(frequencySchema.getFrequency(LocalDateTime.now().getHour()));
            } catch (InterruptedException e) {
                throw new GenerationException("Generator was interrupted", e);
            }
        }
        if(this.mode==Mode.RECORD){
            JSONSerializer.WriteToFile(luggageRecorder, filename);
            logger.info("Recording written!");
        }
        logger.info("Generation done");
    }
}

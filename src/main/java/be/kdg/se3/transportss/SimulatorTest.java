package be.kdg.se3.transportss;

import be.kdg.se3.transportss.adapters.RabbitMQConfig;
import be.kdg.se3.transportss.adapters.in.ConveyorServiceREST;
import be.kdg.se3.transportss.adapters.in.RouteRoutingMessageServiceRabbitMQ;
import be.kdg.se3.transportss.adapters.out.NewLuggageMessageServiceRabbitMQ;
import be.kdg.se3.transportss.adapters.out.RouteRequestMessageServiceRabbitMQ;
import be.kdg.se3.transportss.generation.entity.FrequencySchema;
import be.kdg.se3.transportss.generation.GeneratorServiceGeneration;
import be.kdg.se3.transportss.routing.RouteLuggageStrategyCalculated;
import be.kdg.se3.transportss.simulator.Simulator;
import be.kdg.se3.transportss.routing.service.ConveyorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains a simple example setup of the simulator and services
 *
 * Created by Joren Van de Vondel on 11/3/2016.
 */
public class SimulatorTest {

    // RabbitMQ parameters
    private static final String USERNAME = "javawriter";
    private static final String PASSWORD = "password";
    private static final String HOST = "84.196.20.176";
    private static final String QUEUE_NEWLUGAGE = "NEWLUGGAGECHANNEL";
    private static final String QUEUE_ROUTEREQUEST = "ROUTEREQUESTCHANNEL";
    private static final String QUEUE_ROUTEROUTING = "ROUTEROUTINGCHANNEL";
    private static final LocalDateTime STARTUP = LocalDateTime.now();
    private static final int NR_OF_LUGGAGES = 100;
    private static final String REPLAY_FILE = "Generated.js";
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulatorTest.class);
    private static final String URL = "www.services4se3.com/conveyorservice/";
    private static final int CONVEYORSERVICE_TIMEOUT = 1000;


    public static void main(String[] args) {
        RabbitMQConfig newLuggageConfig = new RabbitMQConfig(HOST, QUEUE_NEWLUGAGE, USERNAME, PASSWORD, true);
        NewLuggageMessageServiceRabbitMQ newLuggageMessageServiceRabbitMQ = new NewLuggageMessageServiceRabbitMQ(newLuggageConfig);

        RabbitMQConfig routeRequestConfig = new RabbitMQConfig(HOST, QUEUE_ROUTEREQUEST, USERNAME, PASSWORD, true);
        RouteRequestMessageServiceRabbitMQ routeRequestMessageServiceRabbitMQ = new RouteRequestMessageServiceRabbitMQ(routeRequestConfig);

        RabbitMQConfig routeRoutingConfig = new RabbitMQConfig(HOST, QUEUE_ROUTEROUTING, USERNAME, PASSWORD, true);
        RouteRoutingMessageServiceRabbitMQ routeRoutingMessageServiceRabbitMQ = new RouteRoutingMessageServiceRabbitMQ(routeRoutingConfig);

        ConveyorService conveyorServiceProxy = new ConveyorServiceREST(URL, CONVEYORSERVICE_TIMEOUT);
        RouteLuggageStrategyCalculated routeLuggageStrategy = new RouteLuggageStrategyCalculated(conveyorServiceProxy, STARTUP);
        //RouteLuggageStrategyFixed routeLuggageStrategy = new RouteLuggageStrategyFixed(1000);

        //region GeneratorServiceGeneration

        List<String> flightIDS = Arrays.asList("1111111", "1111112");
        Map<Integer, List<Integer>> conveyorIDsWithSensorIDs = new HashMap<>();
        conveyorIDsWithSensorIDs.put(11, Arrays.asList(1, 2));
        conveyorIDsWithSensorIDs.put(12, Arrays.asList(3));
        FrequencySchema frequenceSchema = new FrequencySchema(100);
        GeneratorServiceGeneration generatorService = new GeneratorServiceGeneration(flightIDS, conveyorIDsWithSensorIDs, frequenceSchema, STARTUP, NR_OF_LUGGAGES);
        generatorService.enableRecording(REPLAY_FILE);
        //*/
        //endregion

        //region GeneratorServiceReplay
        /*
        GeneratorServiceReplay generatorServide = new GeneratorServiceReplay(REPLAY_FILE, STARTUP);
        // */
        //endregion

        Simulator simulator = new Simulator(STARTUP, routeLuggageStrategy);


        simulator.subscribe(generatorService);
        simulator.subscribe(routeRoutingMessageServiceRabbitMQ);

        simulator.addNewLuggageMessageService(newLuggageMessageServiceRabbitMQ);
        simulator.addRouteRequestMessageService(routeRequestMessageServiceRabbitMQ);

        Thread generatorServiceThread = new Thread(generatorService);
        generatorServiceThread.start();
        try {
            generatorServiceThread.join();
            while (!simulator.luggageListEmpty()) {
                Thread.sleep(1000);
            }

        } catch (InterruptedException e) {
            LOGGER.error("Thread interupted, now stopping");
        }

        newLuggageMessageServiceRabbitMQ.shutdown();
        routeRequestMessageServiceRabbitMQ.shutdown();
        routeRoutingMessageServiceRabbitMQ.shutdown();

    }

}

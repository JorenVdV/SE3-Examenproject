package be.kdg.se3.transportss.routing;

import be.kdg.se3.transportss.routing.entity.ConveyorTimes;
import be.kdg.se3.transportss.simulator.entity.Luggage;
import be.kdg.se3.transportss.simulator.entity.RouteRoutingMessage;
import be.kdg.se3.transportss.simulator.service.CommunicationException;
import be.kdg.se3.transportss.routing.service.ConveyorService;
import be.kdg.se3.transportss.simulator.service.RouteLuggageStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by Joren Van de Vondel on 11/6/2016.
 */
public class RouteLuggageStrategyCalculated implements RouteLuggageStrategy {
    private final ConveyorService conveyorService;
    private final Logger logger = LoggerFactory.getLogger(RouteLuggageStrategyCalculated.class);
    private final LocalDateTime startup;

    public RouteLuggageStrategyCalculated(ConveyorService conveyorService, LocalDateTime startup) {
        this.conveyorService = conveyorService;
        this.startup = startup;
    }

    @Override
    public Luggage routeLuggage(Luggage luggage, RouteRoutingMessage routeRoutingMessage) {
        logger.info("Calculating new timestamp for luggage, calculated time mode");
        ConveyorTimes conveyorTimes;
        try {
            conveyorTimes = this.conveyorService.getTimeToTravel(luggage.getConveyorID(), luggage.getSensorID(), routeRoutingMessage.getConveyorID());
        }catch (CommunicationException ce){
            logger.warn("Error on retrieving conveyorTimes, luggage was deleted: " + ce.getMessage());
            return null;
        }
        long timeToNextConveyor =  startup.until(LocalDateTime.now(), ChronoUnit.MILLIS) - luggage.getTimestamp();
        if (timeToNextConveyor > conveyorTimes.getTimeToConnector()){
            timeToNextConveyor += conveyorTimes.getTimeToConnector() - (timeToNextConveyor%conveyorTimes.getRoundTripTime());
        }
        timeToNextConveyor += conveyorTimes.getTimeOverConnector();
        return new Luggage(luggage.getLuggageID(),
                luggage.getFlightID(),
                routeRoutingMessage.getConveyorID(),
                luggage.getConveyorID(),
                luggage.getTimestamp()+timeToNextConveyor);
    }
}

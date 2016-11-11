package be.kdg.se3.transportss.routing;

import be.kdg.se3.transportss.simulator.entity.Luggage;
import be.kdg.se3.transportss.simulator.entity.RouteRoutingMessage;
import be.kdg.se3.transportss.simulator.service.RouteLuggageStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Joren Van de Vondel on 11/6/2016.
 */
public class RouteLuggageStrategyFixed implements RouteLuggageStrategy{
    private final long delay;
    private final Logger logger = LoggerFactory.getLogger(RouteLuggageStrategyFixed.class);

    public RouteLuggageStrategyFixed(long delay) {
        this.delay = delay;
    }

    @Override
    public Luggage routeLuggage(Luggage luggage, RouteRoutingMessage routeRoutingMessage) {
        logger.debug("Calculating new timestamp for luggage, fixed time mode");
        return new Luggage(luggage.getLuggageID(),
                luggage.getFlightID(),
                routeRoutingMessage.getConveyorID(),
                luggage.getConveyorID(),
                luggage.getTimestamp()+delay);
    }
}

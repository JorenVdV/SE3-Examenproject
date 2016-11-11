package be.kdg.se3.transportss.simulator.service;

import be.kdg.se3.transportss.simulator.entity.Luggage;
import be.kdg.se3.transportss.simulator.entity.RouteRoutingMessage;

/**
 * Created by Joren Van de Vondel on 11/6/2016.
 */
public interface RouteLuggageStrategy {
    Luggage routeLuggage(Luggage luggage, RouteRoutingMessage routeRoutingMessage);
}

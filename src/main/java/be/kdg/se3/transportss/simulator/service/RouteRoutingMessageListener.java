package be.kdg.se3.transportss.simulator.service;

import be.kdg.se3.transportss.simulator.entity.RouteRoutingMessage;

/**
 * Callback interface for incoming routing messages
 *
 * Created by Joren Van de Vondel on 11/4/2016.
 */
public interface RouteRoutingMessageListener {
    void notify(RouteRoutingMessage routeRoutingMessage);
}

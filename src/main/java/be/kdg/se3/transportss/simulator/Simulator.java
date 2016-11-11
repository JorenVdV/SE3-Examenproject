package be.kdg.se3.transportss.simulator;

import be.kdg.se3.transportss.simulator.entity.Luggage;
import be.kdg.se3.transportss.simulator.entity.NewLuggageMessage;
import be.kdg.se3.transportss.simulator.entity.RouteRequestMessage;
import be.kdg.se3.transportss.simulator.entity.RouteRoutingMessage;
import be.kdg.se3.transportss.simulator.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Simulator used as centerpiece
 * Does not have to be ran in external thread, check luggageListEmpty for finished state
 * <p>
 * Created by Joren Van de Vondel on 11/1/2016.
 */
public class Simulator implements GeneratorListener, RouteRoutingMessageListener {
    //region members
    private final LocalDateTime startup;
    private final RouteLuggageStrategy routeLuggageStrategy;
    private final Logger logger = LoggerFactory.getLogger(Simulator.class);
    private List<NewLuggageMessageService> newLuggageMessageServiceList = new ArrayList<>();
    private List<RouteRequestMessageService> routeRequestMessageServiceList = new ArrayList<>();
    private List<SecurityService> securityServiceList = new ArrayList<>();
    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(8);
    private Map<Integer, Luggage> luggageMap = new ConcurrentHashMap<>();
    private int luggagesInSystem = 0;
    //endregion

    //region funtions
    public Simulator(LocalDateTime startup, RouteLuggageStrategy routeLuggageStrategy) {
        this.startup = startup;
        this.routeLuggageStrategy = routeLuggageStrategy;
    }

    public void subscribe(GeneratorService generation) {
        generation.addGenerationListener(this);
    }

    public void subscribe(RouteRoutingMessageService routeRoutingMessageService) {
        routeRoutingMessageService.Initialize(this);
    }

    public void addNewLuggageMessageService(NewLuggageMessageService newLuggageMessageService) {
        this.newLuggageMessageServiceList.add(newLuggageMessageService);
    }

    public void addRouteRequestMessageService(RouteRequestMessageService routeRequestMessageService) {
        this.routeRequestMessageServiceList.add(routeRequestMessageService);
    }

    public void addSecurityService(SecurityService securityService) {
        this.securityServiceList.add(securityService);
    }

    public boolean luggageListEmpty() {
        return luggagesInSystem <= 0;
    }
    //endregion

    //region Inherited functions
    @Override
    public void notify(RouteRoutingMessage routeRoutingMessage) {
        logger.debug("Simulator got RouteRoutingMessage: " + routeRoutingMessage.toString());
        if (securityServiceList.stream()
                .map(s -> s.isRemoved(routeRoutingMessage.getConveyorID(), routeRoutingMessage.getLuggageID()))
                .collect(Collectors.toList()).contains(true)) {
            logger.info("Security removed luggage:" + routeRoutingMessage.getLuggageID()
                    + " from conveyor:" + routeRoutingMessage.getConveyorID());
            luggageMap.remove(routeRoutingMessage.getLuggageID());
            luggagesInSystem--;
            return;
        }
        // We only need to route if status is UNDERWAY
        switch (routeRoutingMessage.getStatus()) {
            case UNDERWAY:
                if (!luggageMap.containsKey(routeRoutingMessage.getLuggageID())) {
                    logger.error("Could not find luggage with id:" + routeRoutingMessage.getLuggageID());
                    break;
                }
                Luggage luggage = luggageMap.get(routeRoutingMessage.getLuggageID());
                luggageMap.remove(routeRoutingMessage.getLuggageID()); //remove luggage from map
                luggage = routeLuggageStrategy.routeLuggage(luggage, routeRoutingMessage); //get new luggage object from route strategy
                if (luggage == null) {
                    logger.warn("No timing found for " + routeRoutingMessage.getLuggageID()
                            + ", will be removed from the system");
                    luggagesInSystem--;
                    logger.info(luggagesInSystem + " Luggage left in system");

                    break;
                }
                luggageMap.put(luggage.getLuggageID(), luggage);
                final RouteRequestMessage routeRequestMessage = new RouteRequestMessage(luggage);
                long timeToPublish = startup.until(LocalDateTime.now(), ChronoUnit.MILLIS);
                timeToPublish = Math.max(0, luggage.getTimestamp() - timeToPublish);
                logger.debug("Putting new luggage on message queue with delay" + luggage.toString());
                scheduledExecutorService.schedule(() -> routeRequestMessageServiceList.forEach(
                        r -> r.notify(routeRequestMessage)), timeToPublish, TimeUnit.MILLISECONDS);
                break;
            default:
                // remove luggage from system
                logger.debug("Luggage with ID:" + routeRoutingMessage.getLuggageID() +
                        " will be removed from the system with status:" + routeRoutingMessage.getStatus());
                this.luggageMap.remove(routeRoutingMessage.getLuggageID());
                luggagesInSystem--;
                logger.info(luggagesInSystem + " Luggage left in system");
                break;
        }
    }

    @Override
    public void notify(Luggage luggage) {
        if (securityServiceList.stream()
                .map(s -> s.isRemoved(luggage.getConveyorID(), luggage.getLuggageID()))
                .collect(Collectors.toList())
                .contains(true)) {
            logger.info("Security removed luggage:" + luggage.getLuggageID() + " from conveyor:" + luggage.getConveyorID());
            return;
        }
        luggageMap.put(luggage.getLuggageID(), luggage);
        luggagesInSystem++;
        logger.info(luggagesInSystem + " Luggages in system");
        newLuggageMessageServiceList.forEach(nlmsl -> nlmsl.notify(new NewLuggageMessage(luggage)));
    }
    //endregion
}
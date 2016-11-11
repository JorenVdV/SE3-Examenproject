package be.kdg.se3.transportss.routing.service;

import be.kdg.se3.transportss.routing.entity.ConveyorTimes;
import be.kdg.se3.transportss.simulator.service.CommunicationException;

/**
 * Services that provides information on ConveyorTimes used to travel
 *
 * Created by Joren Van de Vondel on 11/3/2016.
 */
public interface ConveyorService {
    ConveyorTimes getTimeToTravel(int CurrentConveyorID, int SensorID, int NextConveyorID) throws CommunicationException;
}

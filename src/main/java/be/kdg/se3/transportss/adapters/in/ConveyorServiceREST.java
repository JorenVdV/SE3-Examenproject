package be.kdg.se3.transportss.adapters.in;

import be.kdg.se3.proxy.ConveyorServiceProxy;
import be.kdg.se3.transportss.adapters.in.entity.Conveyor;
import be.kdg.se3.transportss.routing.entity.ConveyorTimes;
import be.kdg.se3.transportss.routing.service.ConveyorService;
import be.kdg.se3.transportss.simulator.service.CommunicationException;
import be.kdg.se3.transportss.utility.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * ConveyorServiceProxy adapter
 *
 * Created by Joren Van de Vondel on 11/3/2016.
 */
public class ConveyorServiceREST implements ConveyorService{
    private final Map<Integer, Conveyor> conveyorMap = new HashMap<>();
    private final Map<Integer, Long> timeMap = new HashMap<>();
    private final LocalDateTime startUp = LocalDateTime.now();
    private final ConveyorServiceProxy conveyorServiceProxy = new ConveyorServiceProxy();
    private final String url;
    private final long timeOut;
    private final Logger logger = LoggerFactory.getLogger(ConveyorServiceREST.class);



    public ConveyorServiceREST(String url, long timeOut) {
        this.url = url;
        this.timeOut = timeOut;
    }

    /**
     * @param currentConveyorID ID of the current conveyor
     * @param sensorID ID of the previous conveyor/ sensor on which the luggage entered the conveyor
     * @param nextConveyorID ID of the conveyor we wish to reach
     * @return ConveyorTimes object used by simulator to calculate arrival on next conveyor
     * @throws CommunicationException
     */
    @Override
    public ConveyorTimes getTimeToTravel(int currentConveyorID,int sensorID, int nextConveyorID) throws CommunicationException{
        logger.info("Retrieving time to travel information");
        logger.debug("Retrieving time to travel information for conveyorID:"+currentConveyorID);
        Conveyor conveyor;
        if(timeMap.containsKey(currentConveyorID) && timeMap.get(currentConveyorID)<=startUp.until(LocalDateTime.now(), ChronoUnit.MILLIS)){
            conveyor = conveyorMap.get(currentConveyorID);
        }else{
            conveyor = getConveyorObject(currentConveyorID);
            timeMap.put(currentConveyorID, startUp.until(LocalDateTime.now(), ChronoUnit.MILLIS));
            conveyorMap.put(currentConveyorID, conveyor);
        }
        return new ConveyorTimes(
                conveyor.getTimeToConnector(sensorID, nextConveyorID),
                conveyor.getTimeOverConnector(nextConveyorID),
                conveyor.getRoundTripTime());

    }

    private Conveyor getConveyorObject(int currentConveyorID) throws CommunicationException{
        try {
            logger.debug("Conveyor object not cached, retrieving...");
            Conveyor conveyor = JSONSerializer.ObjectFromString(conveyorServiceProxy.get(url+currentConveyorID), Conveyor.class);
            logger.debug("Got conveyor object from proxy");
            return conveyor;
        } catch (IOException e) {
            throw new CommunicationException("ConveyorServiceProxy return IO Exception", e);
        } catch (CommunicationException e){
            throw e;
        }
    }
}

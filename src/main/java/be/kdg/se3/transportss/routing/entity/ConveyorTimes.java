package be.kdg.se3.transportss.routing.entity;

/**
 * Data transfer class used by simulator to calculate time spent travelling between conveyors
 * Serves as an adapter between the information provided by the ConveryorService and information needed by the simulator
 *
 * Created by Joren Van de Vondel on 11/3/2016.
 */
public class ConveyorTimes {
    private final long timeToConnector; // in MILLIS
    private final long timeOverConnector; // in MILLIS
    private final long roundTripTime; // in MILLIS

    public ConveyorTimes(long timeToConnector, long timeOverConnector, long roundTripTime) {
        this.timeToConnector = timeToConnector;
        this.timeOverConnector = timeOverConnector;
        this.roundTripTime = roundTripTime;
    }

    public long getTimeToConnector() {
        return timeToConnector;
    }

    public long getTimeOverConnector() {
        return timeOverConnector;
    }

    public long getRoundTripTime() {
        return roundTripTime;
    }

    @Override
    public String toString() {
        return "ConveyorTimes{" +
                "timeToConnector=" + timeToConnector +
                ", timeOverConnector=" + timeOverConnector +
                ", roundTripTime=" + roundTripTime +
                '}';
    }
}

package be.kdg.se3.transportss.simulator.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object used to notify services of a luggage's arrival at a new sensor
 *
 * Created by Joren Van de Vondel on 11/1/2016.
 */
@XmlRootElement
public class RouteRequestMessage {
    private int lugaggeID;
    private int conveyorID;
    private long timestamp;

    public RouteRequestMessage(){}

    public RouteRequestMessage(int lugaggeID, int conveyorID, long timestamp) {
        this.lugaggeID = lugaggeID;
        this.conveyorID = conveyorID;
        this.timestamp = timestamp;
    }

    public RouteRequestMessage(Luggage luggage) {
        this.lugaggeID = luggage.getLuggageID();
        this.conveyorID = luggage.getConveyorID();
        this.timestamp = luggage.getTimestamp();
    }

    public int getLugaggeID() {
        return lugaggeID;
    }

    public int getConveyorID() {
        return conveyorID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setLugaggeID(int lugaggeID) {
        this.lugaggeID = lugaggeID;
    }

    public void setConveyorID(int conveyorID) {
        this.conveyorID = conveyorID;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public String toString() {
        return "RouteLuggageDTO{" +
                "lugaggeID=" + lugaggeID +
                ", conveyorID=" + conveyorID +
                ", timestamp=" + timestamp +
                '}';
    }
}

package be.kdg.se3.transportss.simulator.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for notifying a new luggage
 *
 * Created by Joren Van de Vondel on 11/3/2016.
 */
@XmlRootElement
public class NewLuggageMessage {
    private int luggageID;
    private String flightID;
    private int conveyorID;
    private int sensorID;
    private long timestamp;

    public NewLuggageMessage (){

    }

    public NewLuggageMessage(int baggageId, String flightId, int conveyorId, int sensorId, int timestamp) {
        this.luggageID = baggageId;
        this.flightID = flightId;
        this.conveyorID = conveyorId;
        this.sensorID = sensorId;
        this.timestamp = timestamp;
    }

    public NewLuggageMessage(Luggage luggage){
        this.luggageID = luggage.getLuggageID();
        this.flightID = luggage.getFlightID();
        this.conveyorID = luggage.getConveyorID();
        this.sensorID = luggage.getSensorID();
        this.timestamp = luggage.getTimestamp();
    }

    public int getLuggageID() {
        return luggageID;
    }

    public void setLuggageID(int luggageID) {
        this.luggageID = luggageID;
    }

    public String getFlightID() {
        return flightID;
    }

    public void setFlightID(String flightID) {
        this.flightID = flightID;
    }

    public int getConveyorID() {
        return conveyorID;
    }

    public void setConveyorID(int conveyorID) {
        this.conveyorID = conveyorID;
    }

    public int getSensorID() {
        return sensorID;
    }

    public void setSensorID(int sensorID) {
        this.sensorID = sensorID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "NewLuggageMessage{" +
                "luggageID=" + luggageID +
                ", flightID='" + flightID + '\'' +
                ", conveyorID=" + conveyorID +
                ", sensorID=" + sensorID +
                ", timestamp=" + timestamp +
                '}';
    }
}

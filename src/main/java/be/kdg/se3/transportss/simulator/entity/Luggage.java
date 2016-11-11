package be.kdg.se3.transportss.simulator.entity;

/**
 * Class used to store all information on a piece of luggage while it is in the simulator
 *
 * Created by Joren Van de Vondel on 11/1/2016.
 */
public class Luggage {
    private final int luggageID;
    private final String flightID;
    private int conveyorID;
    private int sensorID;
    private long timestamp;

    public Luggage(int luggageID, String flightID, int conveyorID, int sensorID, long timestamp) {

        this.luggageID = luggageID;
        this.flightID = flightID;
        this.conveyorID = conveyorID;
        this.sensorID = sensorID;
        this.timestamp = timestamp;
    }

    public Luggage(NewLuggageMessage newLuggageMessage){
        this.luggageID = newLuggageMessage.getLuggageID();
        this.flightID = newLuggageMessage.getFlightID();
        this.conveyorID = newLuggageMessage.getConveyorID();
        this.sensorID = newLuggageMessage.getSensorID();
        this.timestamp = newLuggageMessage.getTimestamp();
    }

    public int getLuggageID() {
        return luggageID;
    }

    public String getFlightID() {
        return flightID;
    }

    public int getConveyorID() {
        return conveyorID;
    }

    public int getSensorID() {
        return sensorID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Luggage{" +
                "luggageID=" + luggageID +
                ", flightID='" + flightID + '\'' +
                ", conveyorID=" + conveyorID +
                ", sensorID=" + sensorID +
                ", timestamp=" + timestamp +
                '}';
    }
}

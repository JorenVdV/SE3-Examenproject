package be.kdg.se3.transportss.adapters.in.entity;

import be.kdg.se3.transportss.simulator.service.CommunicationException;

import java.util.List;

/**
 * A data object to retrieve information from the {@link be.kdg.se3.transportss.adapters.in.ConveyorServiceREST}
 *
 * Created by Joren Van de Vondel on 11/3/2016.
 */
public class Conveyor {
    private int conveyorID;
    private int length;
    private int speed;

    private List<Connector> connectors;
    private List<Segment> segments;

    public Conveyor(int conveyorID, int length, int speed, List<Connector> connectors, List<Segment> segments) {
        this.conveyorID = conveyorID;
        this.length = length;
        this.speed = speed;
        this.connectors = connectors;
        this.segments = segments;
    }

    public int getConveyorID() {
        return conveyorID;
    }

    public void setConveyorID(int conveyorID) {
        this.conveyorID = conveyorID;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public List<Connector> getConnectors() {
        return connectors;
    }

    public void setConnectors(List<Connector> connectors) {
        this.connectors = connectors;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    @Override
    public String toString() {
        return "Conveyor{" +
                "conveyorID=" + conveyorID +
                ", length=" + length +
                ", speed=" + speed +
                ", connectors=" + connectors +
                ", segments=" + segments +
                '}';
    }

    /**
     *
     * @param sensorID the sensorID on which the luggage entered the conveyor
     * @param nextConveyorID the conveyorID of the conveyor we wish te reach
     * @return time spent on conveyor between sensor and connector
     * @throws CommunicationException
     */
    public long getTimeToConnector(int sensorID, int nextConveyorID) throws CommunicationException{
        try {
            return this.segments.stream()
                    .filter(segment -> (segment.getInPoint() == sensorID && segment.getOutPoint() == nextConveyorID))
                    .findAny()
                    .get().getDistance() / this.speed * 1000;
        }catch (Exception e){
            throw new CommunicationException("Could not find the connector for "+nextConveyorID, e);
        }
    }

    /**
     *
     * @param nextConveyorID the conveyor we wish to reach
     * @return Time spent on the connector between the current conveyor and the conveyor we wish to reach
     * @throws CommunicationException
     */
    public long getTimeOverConnector(int nextConveyorID) throws CommunicationException{
        try {
            return this.connectors.stream()
                    .filter(connector -> (connector.getConnectedConveyorID() == nextConveyorID))
                    .mapToInt(connector -> connector.getLength() / connector.getSpeed())
                    .findFirst()
                    .getAsInt() * 1000;
        } catch (Exception e){
            throw new CommunicationException("Could not find the connecter for "+nextConveyorID, e);
        }
    }

    /**
     *
     * @return The time a piece of luggage takes to make a roundtrip on the conveyorbelt
     */
    public long getRoundTripTime() {
        return this.length/this.speed*1000;
    }
}

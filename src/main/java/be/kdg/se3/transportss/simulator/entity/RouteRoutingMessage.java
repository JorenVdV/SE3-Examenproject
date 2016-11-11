package be.kdg.se3.transportss.simulator.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object read by the RouteRoutingService
 *
 * Created by Joren Van de Vondel on 11/3/2016.
 */
@XmlRootElement
public class RouteRoutingMessage {
    public enum Status {
        UNDERWAY,
        ARRIVED,
        UNDELIVERABLE,
        LOST
    }

    private int luggageID;
    private int conveyorID;
    private Status status;

    public RouteRoutingMessage() {}

    public RouteRoutingMessage(int luggageID, int conveyorID, Status status) {
        this.luggageID = luggageID;
        this.conveyorID = conveyorID;
        this.status = status;
    }

    public void setLuggageID(int luggageID) {
        this.luggageID = luggageID;
    }

    public void setConveyorID(int conveyorID) {
        this.conveyorID = conveyorID;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getLuggageID() {
        return luggageID;
    }

    public int getConveyorID() {
        return conveyorID;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "RouteRoutingMessage{" +
                "luggageID=" + luggageID +
                ", conveyorID=" + conveyorID +
                ", status=" + status +
                '}';
    }
}

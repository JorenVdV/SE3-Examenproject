package be.kdg.se3.transportss.adapters.in.entity;

/**
 * A data object to retrieve information from the {@link be.kdg.se3.transportss.adapters.in.ConveyorServiceREST}
 *
 * Created by Joren Van de Vondel on 11/3/2016.
 */
public class Connector {
    private int connectedConveyorID;
    private String type;
    private int length;
    private int speed;

    public Connector() {
        connectedConveyorID=-1;
        type="";
        length=-1;
        speed=-1;
    }

    public Connector(int connectedConveyorID, String type, int length, int speed) {
        this.connectedConveyorID = connectedConveyorID;
        this.type = type;
        this.length = length;
        this.speed = speed;
    }

    public int getConnectedConveyorID() {
        return connectedConveyorID;
    }

    public void setConnectedConveyorID(int connectedConveyorID) {
        this.connectedConveyorID = connectedConveyorID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "Connector{" +
                "connectedConveyorID=" + connectedConveyorID +
                ", type='" + type + '\'' +
                ", length=" + length +
                ", speed=" + speed +
                '}';
    }


}

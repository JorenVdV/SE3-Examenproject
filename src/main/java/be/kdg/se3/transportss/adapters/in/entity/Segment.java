package be.kdg.se3.transportss.adapters.in.entity;

/**
 * A data object to retrieve information from the {@link be.kdg.se3.transportss.adapters.in.ConveyorServiceREST}
 *
 * Created by Joren Van de Vondel on 11/3/2016.
 */
public class Segment {
    private int inPoint;
    private int outPoint;
    private int distance;

    public Segment() {
    }

    public Segment(int inPoint, int outpoint, int distance) {
        this.inPoint = inPoint;
        outPoint = outpoint;
        this.distance = distance;
    }

    public int getInPoint() {
        return inPoint;
    }

    public void setInPoint(int inPoint) {
        this.inPoint = inPoint;
    }

    public int getOutPoint() {
        return outPoint;
    }

    public void setOutPoint(int outPoint) {
        this.outPoint = outPoint;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "inPoint=" + inPoint +
                ", outPoint=" + outPoint +
                ", distance=" + distance +
                '}';
    }
}

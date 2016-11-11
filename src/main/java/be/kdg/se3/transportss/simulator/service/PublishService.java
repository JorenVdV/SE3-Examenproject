package be.kdg.se3.transportss.simulator.service;

/**
 * Created by Joren Van de Vondel on 11/5/2016.
 */
public interface PublishService <T>{
    void notify(T object);
    void shutdown();
}

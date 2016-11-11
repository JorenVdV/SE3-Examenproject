package be.kdg.se3.transportss.simulator.service;

/**
 * Interface for all the GeneratorServices
 *
 * Created by Joren Van de Vondel on 11/1/2016.
 */
public interface GeneratorService extends Runnable{
    void addGenerationListener(GeneratorListener iGenerationListener);
}

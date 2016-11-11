package be.kdg.se3.transportss.simulator.service;

import be.kdg.se3.transportss.simulator.entity.Luggage;

/**
 * Listener for the {@link GeneratorService}
 *
 * Created by Joren Van de Vondel on 11/1/2016.
 */
public interface GeneratorListener {
    void notify(Luggage luggage);
}

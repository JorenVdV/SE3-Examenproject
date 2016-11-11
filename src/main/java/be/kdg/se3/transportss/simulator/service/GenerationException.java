package be.kdg.se3.transportss.simulator.service;

/**
 * Wrapper exception for all Generation-linked errors.
 * This can include interruption of the random generator or io error from replay generation
 *
 *  Created by Joren Van de Vondel on 11/1/2016.
 */
public class GenerationException extends RuntimeException {
    public GenerationException(String message, Exception cause){
        super(message,cause);
    }

}

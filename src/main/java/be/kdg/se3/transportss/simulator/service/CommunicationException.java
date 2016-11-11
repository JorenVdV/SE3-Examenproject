package be.kdg.se3.transportss.simulator.service;

/**
 * Wrapper for anything that goes wrong during communication with internet based services
 *
 * Created by Joren Van de Vondel on 11/3/2016.
 */
public class CommunicationException extends RuntimeException{
    public CommunicationException(String message, Throwable cause){
        super(message, cause);
    }
}

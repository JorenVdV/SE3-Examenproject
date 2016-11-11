package be.kdg.se3.transportss.routing.service;

/**
 * Wrapper for all errors linked to the ConveyorService
 *
 * Created by Joren Van de Vondel on 11/4/2016.
 */
public class ConveyorServiceException extends RuntimeException{
    public  ConveyorServiceException(String message, Throwable cause){
        super (message, cause);
    }
}

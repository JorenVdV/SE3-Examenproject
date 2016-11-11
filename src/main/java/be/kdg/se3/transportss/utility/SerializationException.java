package be.kdg.se3.transportss.utility;

/**
 * Wrapper for exception thrown by the serialisation classes {@link JSONSerializer} and {@link XMLSerializer}
 *
 * Created by Joren Van de Vondel on 11/3/2016.
 */
public class SerializationException extends RuntimeException {
    public SerializationException(String message, Throwable cause ){
        super(message, cause);
    }
}

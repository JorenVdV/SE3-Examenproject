package be.kdg.se3.transportss.adapters;

/**
 * Helper class to contain connection information for RabbitMQ message broker
 *
 * Created by Joren Van de Vondel on 11/1/2016.
 */
public class RabbitMQConfig {
    public final String host;
    public final String queueName;
    public final String username;
    public final String password;
    public final boolean use_password;

    public RabbitMQConfig(String host, String queueName, String username, String password, boolean use_password) {
        this.host = host;
        this.queueName = queueName;
        this.username = username;
        this.password = password;
        this.use_password = use_password;
    }
}

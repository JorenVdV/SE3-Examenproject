package be.kdg.se3.transportss.adapters.out;

import be.kdg.se3.transportss.adapters.RabbitMQConfig;
import be.kdg.se3.transportss.simulator.entity.RouteRequestMessage;
import be.kdg.se3.transportss.simulator.service.CommunicationException;
import be.kdg.se3.transportss.simulator.service.RouteRequestMessageService;
import be.kdg.se3.transportss.utility.XMLSerializer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * {@link RouteRequestMessageService} implementation using RabbitMQ as message broker
 *
 * Created by Joren Van de Vondel on 11/1/2016.
 */
public class RouteRequestMessageServiceRabbitMQ implements RouteRequestMessageService {
    private final RabbitMQConfig rabbitMQConfig;
    private Connection connection;
    private Channel channel;

    private final Logger logger = LoggerFactory.getLogger(NewLuggageMessageServiceRabbitMQ.class);

    public RouteRequestMessageServiceRabbitMQ(RabbitMQConfig rabbitMQConfig) throws CommunicationException{
        try {
            logger.info("Initializing");
            this.rabbitMQConfig = rabbitMQConfig;
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(this.rabbitMQConfig.host);
            if(this.rabbitMQConfig.use_password){
                connectionFactory.setUsername(this.rabbitMQConfig.username);
                connectionFactory.setPassword(this.rabbitMQConfig.password);
            }
            this.connection = connectionFactory.newConnection();
            this.channel = this.connection.createChannel();
            this.channel.queueDeclare(this.rabbitMQConfig.queueName,
                    false, /* non-durable */
                    false, /* non-exclusive */
                    false, /* do not auto delete */
                    null); /* no other construction arguments */
            logger.info("Initializing complete");
        } catch (TimeoutException e) {
            throw new CommunicationException("Error during RabbitMQ channel initialisation", e);
        } catch (IOException e) {
            throw new CommunicationException("Error during RabbitMQ channel initialisation", e);
        }
    }

    /**
     * Publishes a RouteRequestMessage to message broker
     * @param routeRequestMessage message to publish
     * @throws CommunicationException
     */
    @Override
    public void notify(RouteRequestMessage routeRequestMessage) throws CommunicationException{
        try {
            logger.info("Publishing");
            logger.debug("Publishing RouteRequestMessage: "+routeRequestMessage);
            channel.basicPublish("", this.rabbitMQConfig.queueName,
                    null, XMLSerializer.XMLSerialize(routeRequestMessage).getBytes());
            logger.debug("Publishing complete");
        } catch (IOException e) {
            throw new CommunicationException("Error during RabbitMQ message publishing", e);
        }
    }

    /**
     * Shuts down the service
     * @throws CommunicationException
     */
    @Override
    public void shutdown() throws CommunicationException {
        try {
            logger.info("Shutting down service");
            channel.close();
            connection.close();
            logger.info("Shutdown complete");
        } catch (Exception e) {
            throw new CommunicationException("Unable to close connection to RabbitMQ", e);
        }
    }
}

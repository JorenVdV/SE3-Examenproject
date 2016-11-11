package be.kdg.se3.transportss.adapters.in;

import be.kdg.se3.transportss.adapters.RabbitMQConfig;
import be.kdg.se3.transportss.simulator.entity.RouteRoutingMessage;
import be.kdg.se3.transportss.simulator.service.CommunicationException;
import be.kdg.se3.transportss.simulator.service.RouteRoutingMessageListener;
import be.kdg.se3.transportss.simulator.service.RouteRoutingMessageService;
import be.kdg.se3.transportss.utility.XMLSerializer;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Implementation of the {@link RouteRoutingMessageService} using RabbitMQ as message broker
 *
 * Created by Joren Van de Vondel on 11/1/2016.
 */
public class RouteRoutingMessageServiceRabbitMQ implements RouteRoutingMessageService {
    private final RabbitMQConfig rabbitMQConfig;

    private Connection connection;
    private Channel channel;

    private final Logger logger = LoggerFactory.getLogger(RouteRoutingMessageServiceRabbitMQ.class);


    public RouteRoutingMessageServiceRabbitMQ(RabbitMQConfig rabbitMQConfig) {
        this.rabbitMQConfig = rabbitMQConfig;
    }

    /**
     * Initializes the listener for the message broker and links a listener to the service
     * @param routeRoutingMessageListener Listener subscribed to the queue
     * @throws CommunicationException
     */
    @Override
    public void Initialize(RouteRoutingMessageListener routeRoutingMessageListener) throws  CommunicationException{
        try {
            logger.info("Initializing RouteRoutingMessageService");
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(this.rabbitMQConfig.host);
            if (this.rabbitMQConfig.use_password) {
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
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {

                    String message = new String(body, "UTF-8");
                    RouteRoutingMessage routeRoutingMessage = XMLSerializer.XMLDeserialize(message, RouteRoutingMessage.class);
                    logger.debug("Received RouteRoutingMessage:" + routeRoutingMessage);
                    if (routeRoutingMessage != null) routeRoutingMessageListener.notify(routeRoutingMessage);
                }
            };
            channel.basicConsume(this.rabbitMQConfig.queueName, true, consumer);
            logger.info("Succesfully added consumer to channel");

        } catch (Exception e) {
            throw new CommunicationException("Error during RabbitMQ channel initialisation", e);
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

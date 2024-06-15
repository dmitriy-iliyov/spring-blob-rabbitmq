package com.example.rabbitmq_api.rabbitmq;

import com.example.rabbitmq_api.models.MessageCreatingDTO;
import com.example.rabbitmq_api.services.MessageServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@EnableRabbit
@Component
public class RabbitMQConsumer {

    private final MessageServices messageServices;
    private final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @Autowired
    public RabbitMQConsumer(MessageServices messageServices){
        this.messageServices = messageServices;
    }

    @RabbitListener(queues = "postgresQueue")
    public void processPostgresQueue(MessageCreatingDTO message){
        logger.info("Received : " + message);
        messageServices.save(message);
    }

    @RabbitListener(queues = "mongoQueue")
    public void processMongoQueue(MessageCreatingDTO message){
        logger.info("Received : " + message);
        messageServices.save(message);
    }
}

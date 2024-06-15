package com.example.socialnetworkrestapi.rabbitmq;


import com.example.socialnetworkrestapi.models.DTO.MessageCreatingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessageToPostgresQueue(MessageCreatingDTO message){
        rabbitTemplate.setExchange("message-exchanger");
        rabbitTemplate.convertAndSend("postgresQueue", message);
    }
}

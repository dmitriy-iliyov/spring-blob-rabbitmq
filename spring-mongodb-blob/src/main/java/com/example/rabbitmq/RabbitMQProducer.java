package com.example.rabbitmq;


import com.example.models.DTO.MessageCreatingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessageMongoQueue(MessageCreatingDTO message){
        rabbitTemplate.setExchange("message-exchanger");
        rabbitTemplate.convertAndSend("mongoQueue", message);
    }
}

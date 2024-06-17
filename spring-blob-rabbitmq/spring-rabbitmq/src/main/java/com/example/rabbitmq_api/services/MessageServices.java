package com.example.rabbitmq_api.services;

import com.example.rabbitmq_api.models.MessageCreatingDTO;
import com.example.rabbitmq_api.models.MessageEntity;
import com.example.rabbitmq_api.repositorys.MessageRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MessageServices {

    private final MessageRepository messageRepository;
    private final Logger logger = LoggerFactory.getLogger(MessageServices.class);

    @Transactional
    public void save(MessageCreatingDTO messageDTO){
        MessageEntity messageEntity = MessageCreatingDTO.toEntity(messageDTO);
        logger.warn(messageEntity.toString());
        messageRepository.save(messageEntity);
    }
}

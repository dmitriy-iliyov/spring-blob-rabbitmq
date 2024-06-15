package com.example.rabbitmq_api.services;

import com.example.rabbitmq_api.models.MessageCreatingDTO;
import com.example.rabbitmq_api.models.MessageResponseDTO;
import com.example.rabbitmq_api.repositorys.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageServices {

    private final MessageRepository messageRepository;

    @Transactional
    public void save(MessageCreatingDTO messageDTO){
        messageRepository.save(MessageCreatingDTO.toEntity(messageDTO));
    }

    @Transactional
    public List<MessageResponseDTO> findAll(){
        List<MessageResponseDTO> messages = new ArrayList<>();
        messageRepository.findAll().forEach(messageEntity -> messages.add(MessageResponseDTO.toDTO(messageEntity)));
        return messages;
    }
}

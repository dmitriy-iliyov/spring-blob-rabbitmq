package com.example.rabbitmq_api.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class MessageResponseDTO {

    private Long id;
    private String resourceName;
    private String imageURL;
    private Instant uploadTime;

    public static MessageResponseDTO toDTO(MessageEntity messageEntity){
        return new MessageResponseDTO(messageEntity.getId(), messageEntity.getResourceName(),
                messageEntity.getImageURL(), messageEntity.getUploadTime());
    }
}

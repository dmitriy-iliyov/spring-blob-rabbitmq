package com.example.rabbitmq_api.models;

import lombok.Data;

import java.time.Instant;

@Data
public class MessageCreatingDTO {

    private String resourceName;
    private String imageURL;
    private Instant uploadTime;

    public static MessageEntity toEntity(MessageCreatingDTO messageDTO){
        return MessageEntity.builder()
                .resourceName(messageDTO.getResourceName())
                .uploadTime(messageDTO.getUploadTime())
                .imageURL(messageDTO.getImageURL())
                .build();
    }
}

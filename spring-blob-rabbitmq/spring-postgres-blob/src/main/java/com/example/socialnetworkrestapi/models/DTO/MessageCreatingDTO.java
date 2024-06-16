package com.example.socialnetworkrestapi.models.DTO;

import lombok.Data;

import java.time.Instant;

@Data
public class MessageCreatingDTO {

    private String resourceName = "spring-postgres-blob";
    private String imageURL;
    private Instant uploadTime;

    public MessageCreatingDTO(String imageURL, Instant uploadTime){
        this.imageURL = imageURL;
        this.uploadTime = uploadTime;
    }
}

package com.example.models.DTO;

import lombok.Data;

import java.time.Instant;

@Data
public class MessageCreatingDTO {

    private String resourceName = "spring-mongodb-blob";
    private Instant uploadTime;

}

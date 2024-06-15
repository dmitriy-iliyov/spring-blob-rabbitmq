package com.example.socialnetworkrestapi.models.DTO;

import lombok.Data;

import java.time.Instant;

@Data
public class MessageCreatingDTO {

    private String resourceName = "spring-postgres-blob";
    private Instant uploadTime;

}

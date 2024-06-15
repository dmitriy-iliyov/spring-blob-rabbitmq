package com.example.models.DTO.post;

import com.example.models.entitys.CategoryEntity;
import com.example.models.entitys.PostEntity;
import com.example.models.entitys.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class PostCreatingDTO {

    private String topic;
    private String description;
    private Instant createDate;
    private String userID;
    private String categoryID;

    public static PostEntity toEntity(PostCreatingDTO postDTO){
        return PostEntity.builder()
                .topic(postDTO.topic)
                .description(postDTO.description)
                .createDate(Instant.now())
                .userID(postDTO.getUserID())
                .categoryID(postDTO.categoryID)
                .build();
    }
}

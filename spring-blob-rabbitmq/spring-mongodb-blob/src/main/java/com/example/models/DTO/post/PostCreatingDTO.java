package com.example.models.DTO.post;

import com.example.models.entitys.PostEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class PostCreatingDTO {

    private String topic;
    private String description;
    private Instant createDate;
    private String userId;
    private String categoryId;
    private String imageURI;

    public static PostEntity toEntity(PostCreatingDTO postDTO){
        return PostEntity.builder()
                .topic(postDTO.getTopic())
                .description(postDTO.getDescription())
                .createDate(Instant.now())
                .userId(postDTO.getUserId())
                .categoryId(postDTO.getCategoryId())
                .imageURI(postDTO.getImageURI())
                .build();
    }
}

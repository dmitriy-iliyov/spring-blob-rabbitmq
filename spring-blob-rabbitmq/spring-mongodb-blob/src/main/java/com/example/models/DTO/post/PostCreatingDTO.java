package com.example.models.DTO.post;

import com.example.models.entitys.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCreatingDTO {

    private String topic;
    private String description;
    private String imageURI;
    private Instant createDate;
    private String userId;
    private String categoryId;


    public static PostEntity toEntity(PostCreatingDTO postDTO){
        return PostEntity.builder()
                .topic(postDTO.getTopic())
                .description(postDTO.getDescription())
                .imageURI(postDTO.getImageURI())
                .createDate(Instant.now())
                .userId(postDTO.getUserId())
                .categoryId(postDTO.getCategoryId())
                .build();
    }
}

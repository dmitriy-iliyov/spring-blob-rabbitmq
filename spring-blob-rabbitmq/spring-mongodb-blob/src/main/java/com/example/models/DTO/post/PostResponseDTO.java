package com.example.models.DTO.post;

import com.example.models.entitys.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {

    private String id;
    private String topic;
    private String description;
    private Instant createDate;
    private String userId;
    private String categoryId;
    private String imageURI;


    public static PostResponseDTO toDTO(PostEntity postEntity){
        return new PostResponseDTO(
                postEntity.getId(), postEntity.getTopic(), postEntity.getDescription(),
                postEntity.getCreateDate(), postEntity.getUserId(), postEntity.getCategoryId(),
                postEntity.getImageURI());
    }
}

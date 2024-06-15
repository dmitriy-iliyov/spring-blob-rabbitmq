package com.example.socialnetworkrestapi.models.DTO.post;

import com.example.socialnetworkrestapi.models.entitys.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {

    private Long id;
    private String topic;
    private String description;
    private Long userID;
    private Long categoryID;

    public static PostResponseDTO toDTO(PostEntity postEntity){
        return new PostResponseDTO(
                postEntity.getId(), postEntity.getTopic(), postEntity.getDescription(),
                postEntity.getUser().getId(), postEntity.getCategory().getId()
        );
    }
}

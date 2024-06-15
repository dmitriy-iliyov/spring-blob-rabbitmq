package com.example.socialnetworkrestapi.models.DTO.post;

import com.example.socialnetworkrestapi.models.entitys.CategoryEntity;
import com.example.socialnetworkrestapi.models.entitys.PostEntity;
import com.example.socialnetworkrestapi.models.entitys.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostCreatingDTO {

    private String topic;
    private String description;
    private Long userID;
    private Long categoryID;

    public static PostEntity toEntity(PostCreatingDTO postDTO, UserEntity user, CategoryEntity category){
        return new PostEntity(postDTO.getTopic(), postDTO.getDescription(), user, category);
    }
}

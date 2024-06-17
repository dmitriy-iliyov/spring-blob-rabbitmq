package com.example.socialnetworkrestapi.models.DTO.post;

import com.example.socialnetworkrestapi.models.entitys.CategoryEntity;
import com.example.socialnetworkrestapi.models.entitys.PostEntity;
import com.example.socialnetworkrestapi.models.entitys.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostCreatingDTO {

    private String topic;
    private String description;
    private String imageURL;
    private Long userId;
    private Long categoryId;

    public static PostEntity toEntity(PostCreatingDTO postDTO, UserEntity user, CategoryEntity category){
        return PostEntity.builder()
                .topic(postDTO.getTopic())
                .description(postDTO.getDescription())
                .imageURL(postDTO.getImageURL())
                .user(user)
                .category(category)
                .build();
    }
}

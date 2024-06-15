package com.example.models.DTO.user;


import com.example.models.DTO.post.PostResponseDTO;
import com.example.models.Role;
import com.example.models.entitys.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private String id;
    private String name;
    private Role role;
    private String password;
    private String email;
    private Instant createDate;
    private List<PostResponseDTO> posts;

    public static UserResponseDTO toDTO(UserEntity userEntity){
        return new UserResponseDTO(
                userEntity.getId(), userEntity.getName(), userEntity.getRole(), userEntity.getPassword(),
                userEntity.getEmail(), userEntity.getCreateDate(), null);
    }
}

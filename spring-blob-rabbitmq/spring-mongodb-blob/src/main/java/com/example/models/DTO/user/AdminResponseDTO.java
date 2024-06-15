package com.example.models.DTO.user;

import com.example.models.Role;
import com.example.models.entitys.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponseDTO {

    private String id;
    private String name;
    private String password;
    private Role role;

    public static AdminResponseDTO toDTO(UserEntity userEntity){
        return new AdminResponseDTO(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getPassword(),
                userEntity.getRole()
        );
    }
}

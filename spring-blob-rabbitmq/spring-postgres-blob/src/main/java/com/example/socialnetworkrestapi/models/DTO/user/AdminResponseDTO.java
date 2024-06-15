package com.example.socialnetworkrestapi.models.DTO.user;

import com.example.socialnetworkrestapi.models.Role;
import com.example.socialnetworkrestapi.models.entitys.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponseDTO {

    private Long id;
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

package com.example.models.DTO.user;

import com.example.models.Role;
import com.example.models.entitys.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;

@Data
@NoArgsConstructor
public class UserRegistrationDTO {

    private String name;
    private String password;
    private String email;

    public static UserEntity toEntity(UserRegistrationDTO userDTO){
        return UserEntity.builder()
                .name(userDTO.name)
                .password(userDTO.password)
                .email(userDTO.email)
                .role(Role.USER)
                .createDate(Instant.now())
                .build();
    }
}

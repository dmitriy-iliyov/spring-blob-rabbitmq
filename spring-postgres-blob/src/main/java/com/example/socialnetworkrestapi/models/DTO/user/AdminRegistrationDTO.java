package com.example.socialnetworkrestapi.models.DTO.user;

import com.example.socialnetworkrestapi.models.Role;
import com.example.socialnetworkrestapi.models.entitys.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class AdminRegistrationDTO implements RegistrationDTO{

    @NotEmpty(message = "name shouldn't be empty")
    @Size(min = 1, max = 10, message = "name should be less then 10")
    private String name;
    @NotEmpty(message = "password shouldn't be empty")
    private String password;
    private String email = "-";
    private Role role = Role.ADMIN;

    public static UserEntity toEntity(RegistrationDTO adminDTO){
        return UserEntity.builder()
                .name(adminDTO.getName())
                .password(adminDTO.getPassword())
                .role(adminDTO.getRole())
                .createDate(Instant.now())
                .email(adminDTO.getEmail())
                .build();
    }
}

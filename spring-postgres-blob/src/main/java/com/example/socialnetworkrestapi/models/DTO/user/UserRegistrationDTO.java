package com.example.socialnetworkrestapi.models.DTO.user;

import com.example.socialnetworkrestapi.models.Role;
import com.example.socialnetworkrestapi.models.entitys.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class UserRegistrationDTO implements RegistrationDTO{


    @NotEmpty(message = "name shouldn't be empty")
    @Size(min = 1, max = 10, message = "name should be less then 10")
    private String name;
    @NotEmpty(message = "password shouldn't be empty")
    private String password;
    @NotEmpty(message = "email shouldn't be empty")
    @Email(message = "email should be valid")
    private String email;
    private Role role = Role.USER;

    public static UserEntity toEntity(RegistrationDTO userDTO){
        return UserEntity.builder()
                .name(userDTO.getName())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .role(userDTO.getRole())
                .createDate(Instant.now())
                .build();
    }
}

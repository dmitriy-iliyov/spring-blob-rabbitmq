package com.example.socialnetworkrestapi.models.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogInDTO {

    private String name;
    private String password;
}

package com.example.socialnetworkrestapi.models.DTO.user;


import com.example.socialnetworkrestapi.models.Role;

public interface RegistrationDTO {
    void setName(String name);
    String getName();
    void setPassword(String password);
    String getPassword();
    void setEmail(String email);
    String getEmail();
    Role getRole();

}

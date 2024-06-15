package com.example.socialnetworkrestapi.security;

import com.example.socialnetworkrestapi.models.DTO.post.PostResponseDTO;
import com.example.socialnetworkrestapi.models.Role;
import com.example.socialnetworkrestapi.models.entitys.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDetailsImplementation implements UserDetails {

    private Long id;
    private String name;
    private String password;
    private String email;
    private Instant createDate;
    private Role role;
    private List<PostResponseDTO> posts;

    public static UserDetailsImplementation build(UserEntity userEntity){

        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();
        userEntity.getPosts().forEach(postEntity -> postResponseDTOS.add(PostResponseDTO.toDTO(postEntity)));

        return new UserDetailsImplementation(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getPassword(),
                userEntity.getEmail(),
                userEntity.getCreateDate(),
                userEntity.getRole(),
                postResponseDTOS
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

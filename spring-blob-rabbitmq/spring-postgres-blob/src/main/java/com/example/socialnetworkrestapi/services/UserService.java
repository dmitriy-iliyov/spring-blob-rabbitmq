package com.example.socialnetworkrestapi.services;

import com.example.socialnetworkrestapi.models.DTO.user.*;
import com.example.socialnetworkrestapi.models.Role;
import com.example.socialnetworkrestapi.models.entitys.UserEntity;
import com.example.socialnetworkrestapi.repositorys.UserRepository;
import com.example.socialnetworkrestapi.security.UserDetailsImplementation;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
        return UserDetailsImplementation.build(userEntity);
    }

    @Transactional
    public void save(RegistrationDTO userRegistrationDTO, PasswordEncoder passwordEncoder){
        userRegistrationDTO.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        UserEntity userEntity;
        if(userRegistrationDTO.getRole() == Role.ADMIN)
            userEntity = AdminRegistrationDTO.toEntity(userRegistrationDTO);
        else
            userEntity = UserRegistrationDTO.toEntity(userRegistrationDTO);
        userRepository.save(userEntity);
    }

    @Transactional
    public boolean existingByName(String name){
        return userRepository.existsUserEntityByName(name);
    }

    @Transactional
    public Optional<UserEntity> findEntityById(Long id){
        return userRepository.findById(id);
    }

    @Transactional
    public Optional<UserEntity> findEntityByName(String name){
        return userRepository.findByName(name);
    }

    @Transactional
    public Optional<UserResponseDTO> findDtoById(Long id){
        return userRepository.findById(id).map(UserResponseDTO::toDTO);
    }

    @Transactional
    public Optional<UserResponseDTO> findDtoByName(String name){
        return userRepository.findByName(name).map(UserResponseDTO::toDTO);
    }

    @Transactional
    public List<UserResponseDTO> findAllUsers(){
        List<UserResponseDTO> users = new ArrayList<>();
        userRepository.findAllByRole(Role.USER).forEach(userEntity -> users.add(UserResponseDTO.toDTO(userEntity)));
        return  users;
    }

    @Transactional
    public List<AdminResponseDTO> findAllAdmins(){
        List<AdminResponseDTO> admins = new ArrayList<>();
        userRepository.findAllByRole(Role.ADMIN).forEach(userEntity -> admins.add(AdminResponseDTO.toDTO(userEntity)));
        return admins;
    }

    @Transactional
    public void update(UserResponseDTO userResponseDTO, PasswordEncoder passwordEncoder){
        userRepository.findById(userResponseDTO.getId())
                .ifPresent(userEntity -> {
                    userEntity.setName(userResponseDTO.getName());
                    if (!userResponseDTO.getPassword().isEmpty())
                        userEntity.setPassword(passwordEncoder.encode(userResponseDTO.getPassword()));

                    userEntity.setEmail(userResponseDTO.getEmail());
                    userRepository.save(userEntity);
                });
    }

    @Transactional
    public  void deleteByNameAndPassword(String name, String password){
        userRepository.deleteByNameAndPassword(name, password);
    }

    @Transactional
    public void deleteById(Long id){
        userRepository.deleteById(id);
    }
}

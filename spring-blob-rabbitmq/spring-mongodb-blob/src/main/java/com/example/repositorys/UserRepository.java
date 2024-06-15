package com.example.repositorys;

import com.example.models.Role;
import com.example.models.entitys.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByName(String name);

    boolean existsUserEntityByName(String name);

    Iterable<UserEntity> findAllByRole(Role role);

    void deleteByNameAndPassword(String name, String password);
}

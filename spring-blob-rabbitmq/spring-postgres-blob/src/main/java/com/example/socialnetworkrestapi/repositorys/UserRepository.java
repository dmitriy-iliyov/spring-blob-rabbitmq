package com.example.socialnetworkrestapi.repositorys;

import com.example.socialnetworkrestapi.models.Role;
import com.example.socialnetworkrestapi.models.entitys.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByName(String name);

    boolean existsUserEntityByName(String name);

    Iterable<UserEntity> findAllByRole(Role role);

    void deleteByNameAndPassword(String name, String password);
}

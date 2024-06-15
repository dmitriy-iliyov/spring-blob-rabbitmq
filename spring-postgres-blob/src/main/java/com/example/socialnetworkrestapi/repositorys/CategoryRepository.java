package com.example.socialnetworkrestapi.repositorys;

import com.example.socialnetworkrestapi.models.entitys.CategoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByName(String name);
    void deleteByName(String name);
}

package com.example.repositorys;

import com.example.models.entitys.CategoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<CategoryEntity, String> {
    Optional<CategoryEntity> findByName(String name);
    void deleteByName(String name);
}

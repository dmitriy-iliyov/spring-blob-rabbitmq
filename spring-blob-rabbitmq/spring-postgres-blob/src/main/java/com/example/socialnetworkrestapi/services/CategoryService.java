package com.example.socialnetworkrestapi.services;

import com.example.socialnetworkrestapi.models.DTO.category.CategoryResponseDTO;
import com.example.socialnetworkrestapi.models.entitys.CategoryEntity;
import com.example.socialnetworkrestapi.repositorys.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void save(CategoryEntity categoryEntity){
        categoryRepository.save(categoryEntity);
    }

    @Transactional
    public Optional<CategoryEntity> findById(Long id){
        return categoryRepository.findById(id);
    }

    @Transactional
    public Optional<CategoryEntity> findByName(String categoryName){
        return categoryRepository.findByName(categoryName);
    }

    @Transactional
    public List<CategoryResponseDTO> findAll(){
        List<CategoryResponseDTO> categoryDTOS = new ArrayList<>();
        categoryRepository.findAll().forEach(categoryEntity -> categoryDTOS.add(CategoryResponseDTO.toDTO(categoryEntity)));
        return categoryDTOS;
    }

    @Transactional
    public void deleteById(Long id){
        categoryRepository.deleteById(id);
    }

    @Transactional
    public void deleteByName(String categoryName){
        categoryRepository.deleteByName(categoryName);
    }
}

package com.example.socialnetworkrestapi.controllers;


import com.example.socialnetworkrestapi.models.DTO.category.CategoryCreatingDTO;
import com.example.socialnetworkrestapi.models.DTO.category.CategoryResponseDTO;
import com.example.socialnetworkrestapi.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
@PreAuthorize("hasAuthority('ADMIN')")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/new")
    public String addNewCategoryForm(Model model){
        model.addAttribute("category", new CategoryCreatingDTO());
        return "category_register_form";
    }

    @PostMapping("/new")
    public ResponseEntity<String> saveNewCategory(@ModelAttribute CategoryCreatingDTO category){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-info", "Creating category");

        try {
            categoryService.save(CategoryCreatingDTO.toEntity(category));
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .headers(httpHeaders)
                    .body("Category successfully created");
        }catch (DataIntegrityViolationException e){
            System.out.println("EXCEPTION  " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .headers(httpHeaders)
                    .body("Category with this name "+ category.getName() +" is exist");
        }
    }

    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<CategoryResponseDTO> getCategoryByIdOrName(@RequestParam(required = false) Long id,
                                                                     @RequestParam(required = false) String name){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-info", "Getting category");

        if (id != null) {
            Optional<CategoryResponseDTO> categoryOptional = categoryService.findById(id).map(CategoryResponseDTO::toDTO);

            return categoryOptional
                    .map(categoryDTO -> ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(categoryDTO))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).headers(httpHeaders).body(null));
        } else if (name != null) {
            Optional<CategoryResponseDTO> categoryOptional = categoryService.findByName(name).map(CategoryResponseDTO::toDTO);

            return categoryOptional
                    .map(categoryDTO -> ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(categoryDTO))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).headers(httpHeaders).body(null));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(httpHeaders).body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(){

        List<CategoryResponseDTO> categories = categoryService.findAll();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-info", "Getting all category");

        return categories.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).headers(httpHeaders).body(null)
                : ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(categories);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-info", "Deleting category");

        try{
            categoryService.deleteById(id);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .headers(httpHeaders)
                    .body("Category with id " + id + " has been successfully deleted");
        }catch (Exception e){
            System.out.println("EXCEPTION  " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(httpHeaders)
                    .body("Failed to delete admin with id " + id);
        }
    }
}

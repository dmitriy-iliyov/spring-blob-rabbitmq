package com.example.socialnetworkrestapi.models.DTO.category;

import com.example.socialnetworkrestapi.models.entitys.CategoryEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryCreatingDTO {

    @NotEmpty(message = "name shouldn't be empty")
    @Size(min = 1, max = 20, message = "name should be [1:20]")
    private String name;

    public static CategoryEntity toEntity(CategoryCreatingDTO categoryDTO){
        return new CategoryEntity(categoryDTO.getName());
    }
}

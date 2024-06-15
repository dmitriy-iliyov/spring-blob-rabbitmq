package com.example.models.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="categories")
public class CategoryEntity {

    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private List<PostEntity> posts;

    public CategoryEntity(String name){
        this.name = name;
    }
}

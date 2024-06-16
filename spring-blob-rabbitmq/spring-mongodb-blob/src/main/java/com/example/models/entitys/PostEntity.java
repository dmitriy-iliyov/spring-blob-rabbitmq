package com.example.models.entitys;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection="posts")
public class PostEntity {

    @Id
    private String id;
    private String topic;
    private String description;
    @Indexed(name = "create_date")
    private Instant createDate;
    @Indexed(name = "user_id")
    private String userId;
    @Indexed(name = "category_id")
    private String categoryId;
    @Indexed(name = "image_uri")
    private String imageURI;

}

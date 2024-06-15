package com.example.socialnetworkrestapi.models.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="posts")
public class PostEntity {

    @Id
    @SequenceGenerator(name = "post_sequence", sequenceName = "post_sequence", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "post_sequence")
    private Long id;

    @Column(name = "topic", nullable = false, length = 20, columnDefinition = "TEXT")
    private String topic;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private CategoryEntity category;

    public PostEntity(String topic, String description, UserEntity user, CategoryEntity category){
        this.topic = topic;
        this.description = description;
        this.user = user;
        this.category = category;
    }
}

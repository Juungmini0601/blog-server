package org.logly.database.entity;

import jakarta.persistence.*;

import lombok.Getter;

@Getter
@Entity
@Table(name = "post_views")
public class PostViewEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postViewId;

    private Long postId;

    private Long userId;

    // For JPA
    protected PostViewEntity() {}
}

package org.logly.database.entity;

import jakarta.persistence.*;

import lombok.Getter;

@Getter
@Entity
@Table(name = "post_tags")
public class PostTagEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postTagId;

    private Long postId;
    private Long tagId;

    protected PostTagEntity() {}
}

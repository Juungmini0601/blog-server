package org.logly.database.entity;

import jakarta.persistence.*;

import lombok.Getter;

@Getter
@Entity
@Table(name = "post_drafts")
public class PostDraftEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postDraftId;

    private Long userId;
    private String title;
    private String content;

    // For JPA
    protected PostDraftEntity() {}
}

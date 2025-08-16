package org.logly.database.entity;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private Long postId;

    private Long userId;

    private String content;

    @Builder
    public CommentEntity(Long commentId, Long postId, Long userId, String content) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
    }

    protected CommentEntity() {}

    public void setUser(UserEntity userEntity) {
        this.userId = userEntity.getUserId();
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }
}

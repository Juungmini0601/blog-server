package org.logly.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

import org.logly.database.entity.CommentEntity;

@Getter
public class UpdateCommentResponse {
    private Long commentId;
    private Long postId;
    private Long userId;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UpdateCommentResponse() {}

    @Builder
    public UpdateCommentResponse(
            Long commentId,
            Long postId,
            Long userId,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UpdateCommentResponse fromEntity(CommentEntity entity) {
        return UpdateCommentResponse.builder()
                .commentId(entity.getCommentId())
                .postId(entity.getPost().getPostId())
                .userId(entity.getUser().getUserId())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

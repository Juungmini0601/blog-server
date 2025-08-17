package org.logly.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

import org.logly.database.entity.PostLikeEntity;

@Getter
public class CreatePostLikeResponse {
    private Long postLikeId;
    private Long postId;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CreatePostLikeResponse() {}

    @Builder
    public CreatePostLikeResponse(
            Long postLikeId, Long postId, Long userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.postLikeId = postLikeId;
        this.postId = postId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CreatePostLikeResponse fromEntity(PostLikeEntity entity) {
        return builder()
                .postLikeId(entity.getPostLikeId())
                .postId(entity.getPost().getPostId())
                .userId(entity.getUser().getUserId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

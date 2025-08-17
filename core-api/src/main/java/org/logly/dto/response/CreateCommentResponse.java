package org.logly.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.logly.database.entity.CommentEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentResponse {
    private Long commentId;
    private Long postId;
    private Long userId;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CreateCommentResponse fromEntity(CommentEntity entity) {
        return CreateCommentResponse.builder()
                .commentId(entity.getCommentId())
                .postId(entity.getPost().getPostId())
                .userId(entity.getUser().getUserId())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

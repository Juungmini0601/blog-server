package blog.jungmini.me.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

import blog.jungmini.me.database.entity.CommentEntity;

@Getter
public class CreateCommentResponse {
    private Long commentId;
    private Long postId;
    private Long userId;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CreateCommentResponse() {}

    @Builder
    public CreateCommentResponse(
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

    public static CreateCommentResponse fromEntity(CommentEntity entity) {
        return CreateCommentResponse.builder()
                .commentId(entity.getCommentId())
                .postId(entity.getPostId())
                .userId(entity.getUserId())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

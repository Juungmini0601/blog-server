package org.logly.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;

import org.logly.database.entity.CommentEntity;

@Getter
public class CreateCommentRequest {
    @NotNull(message = "게시글 ID는 필수 입력값입니다")
    private Long postId;

    @NotBlank(message = "내용은 필수 입력값입니다")
    private String content;

    public CreateCommentRequest() {}

    @Builder
    public CreateCommentRequest(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }

    public CommentEntity toEntity() {
        return CommentEntity.builder().postId(postId).content(content).build();
    }
}

package org.logly.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

import org.logly.database.entity.CommentEntity;

@Getter
public class UpdateCommentRequest {
    @NotBlank(message = "내용은 필수 입력값입니다")
    private String content;

    public UpdateCommentRequest() {}

    public UpdateCommentRequest(String content) {
        this.content = content;
    }

    public CommentEntity toEntity(Long commentId) {
        return CommentEntity.builder().commentId(commentId).content(content).build();
    }
}

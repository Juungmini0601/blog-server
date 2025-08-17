package org.logly.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {
    @NotNull(message = "게시글 ID는 필수 입력값입니다")
    private Long postId;

    @NotBlank(message = "내용은 필수 입력값입니다")
    private String content;
}

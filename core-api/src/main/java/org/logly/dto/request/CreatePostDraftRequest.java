package org.logly.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostDraftRequest {
    @NotBlank(message = "제목은 필수 입력값입니다")
    @Size(max = 255, message = "제목은 255자를 초과할 수 없습니다")
    private String title;

    @NotBlank(message = "내용은 필수 입력값입니다")
    private String content;

    private String thumbnailUrl;

    @JsonProperty("isPublic")
    private boolean isPublic = true;

    private Long seriesId;

    @JsonProperty("isPublic")
    public boolean isPublic() {
        return isPublic;
    }
}

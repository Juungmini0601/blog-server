package blog.jungmini.me.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;

import blog.jungmini.me.database.entity.PostEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public class UpdatePostRequest {
    @NotBlank(message = "제목은 필수 입력값입니다")
    @Size(max = 255, message = "제목은 255자를 초과할 수 없습니다")
    private String title;

    @NotBlank(message = "내용은 필수 입력값입니다")
    private String content;

    private String thumbnailUrl;

    @JsonProperty("isPublic")
    private boolean isPublic = true;

    private Long seriesId;

    public UpdatePostRequest() {}

    @Builder
    public UpdatePostRequest(String title, String content, String thumbnailUrl, boolean isPublic, Long seriesId) {
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.isPublic = isPublic;
        this.seriesId = seriesId;
    }

    @JsonProperty("isPublic")
    public boolean isPublic() {
        return isPublic;
    }

    public PostEntity toEntity(Long postId) {
        return PostEntity.builder()
                .postId(postId)
                .title(title)
                .content(content)
                .thumbnailUrl(thumbnailUrl)
                .isPublic(isPublic)
                .seriesId(seriesId)
                .viewCount(0L)
                .build();
    }
}

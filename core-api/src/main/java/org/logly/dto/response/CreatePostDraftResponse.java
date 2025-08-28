package org.logly.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

import org.logly.database.entity.PostDraftEntity;

@Getter
public class CreatePostDraftResponse {
    private Long postDraftId;
    private Long userId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private Boolean isPublic;
    private Long seriesId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CreatePostDraftResponse() {}

    @Builder
    public CreatePostDraftResponse(
            Long postDraftId,
            Long userId,
            String title,
            String content,
            String thumbnailUrl,
            Boolean isPublic,
            Long seriesId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.postDraftId = postDraftId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.isPublic = isPublic;
        this.seriesId = seriesId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CreatePostDraftResponse fromEntity(PostDraftEntity entity) {
        return CreatePostDraftResponse.builder()
                .postDraftId(entity.getPostDraftId())
                .userId(entity.getUser().getUserId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .thumbnailUrl(entity.getThumbnailUrl())
                .isPublic(entity.getIsPublic())
                .seriesId(entity.getSeries() != null ? entity.getSeries().getSeriesId() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

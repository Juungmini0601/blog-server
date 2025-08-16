package org.logly.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

import org.logly.database.entity.PostEntity;

@Getter
public class UpdatePostResponse {
    private Long postId;
    private Long userId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private Boolean isPublic;
    private Long viewCount;
    private Long seriesId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public UpdatePostResponse(
            Long postId,
            Long userId,
            String title,
            String content,
            String thumbnailUrl,
            Boolean isPublic,
            Long viewCount,
            Long seriesId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.isPublic = isPublic;
        this.viewCount = viewCount;
        this.seriesId = seriesId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UpdatePostResponse fromEntity(PostEntity entity) {
        return UpdatePostResponse.builder()
                .postId(entity.getPostId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .thumbnailUrl(entity.getThumbnailUrl())
                .isPublic(entity.getIsPublic())
                .viewCount(entity.getViewCount())
                .seriesId(entity.getSeriesId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

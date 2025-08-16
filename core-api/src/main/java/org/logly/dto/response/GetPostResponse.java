package org.logly.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

import org.logly.database.projection.PostDetail;

@Getter
public class GetPostResponse {
    private Long postId;
    private Long userId;
    private String userNickname;
    private String userProfileImageUrl;
    private String userIntroduction;
    private String title;
    private String content;
    private String thumbnailUrl;
    private Boolean isPublic;
    private Long likeCount;
    private Boolean isLiked;
    private Long seriesId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public GetPostResponse(
            Long postId,
            Long userId,
            String userNickname,
            String userProfileImageUrl,
            String userIntroduction,
            String title,
            String content,
            String thumbnailUrl,
            Boolean isPublic,
            Long likeCount,
            Boolean isLiked,
            Long seriesId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.postId = postId;
        this.userId = userId;
        this.userNickname = userNickname;
        this.userProfileImageUrl = userProfileImageUrl;
        this.userIntroduction = userIntroduction;
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.isPublic = isPublic;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.seriesId = seriesId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GetPostResponse fromEntity(PostDetail entity) {
        return GetPostResponse.builder()
                .postId(entity.getPostId())
                .userId(entity.getUserId())
                .userNickname(entity.getUserNickname())
                .userProfileImageUrl(entity.getUserProfileImageUrl())
                .userIntroduction(entity.getUserIntroduction())
                .title(entity.getTitle())
                .content(entity.getContent())
                .thumbnailUrl(entity.getThumbnailUrl())
                .isPublic(entity.getIsPublic())
                .likeCount(entity.getLikeCount())
                .isLiked(entity.getIsLiked())
                .seriesId(entity.getSeriesId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

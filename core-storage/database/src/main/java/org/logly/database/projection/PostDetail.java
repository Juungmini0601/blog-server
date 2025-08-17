package org.logly.database.projection;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.logly.database.entity.PostEntity;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDetail {
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
    private Long seriesId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isLiked;

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public static PostDetail from(PostEntity post) {
        return PostDetail.builder()
                .postId(post.getPostId())
                .userId(post.getUser().getUserId())
                .userNickname(post.getUser().getNickname())
                .userProfileImageUrl(post.getUser().getProfileImageUrl())
                .userIntroduction(post.getUser().getIntroduction())
                .title(post.getTitle())
                .content(post.getContent())
                .thumbnailUrl(post.getThumbnailUrl())
                .isPublic(post.getIsPublic())
                .likeCount(post.getLikeCount())
                .seriesId(post.getSeries() == null ? null : post.getSeries().getSeriesId())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}

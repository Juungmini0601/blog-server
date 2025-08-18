package org.logly.batch.model;

import lombok.Data;

@Data
public class PostData {
    private Long postId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private Boolean isPublic;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private Long seriesId;
    private Long userId;
}

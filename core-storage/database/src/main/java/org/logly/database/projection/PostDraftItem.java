package org.logly.database.projection;

import java.time.LocalDateTime;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDraftItem {
    private Long postDraftId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String nickname;
    private String profileImageUrl;
    private Long seriesId;
    private String seriesName;
}

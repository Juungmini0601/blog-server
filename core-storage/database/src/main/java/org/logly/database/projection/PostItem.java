package org.logly.database.projection;

import java.time.LocalDateTime;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostItem {
    private Long postId;
    private String thumbnailUrl;
    private String title;
    private LocalDateTime createdAt;
    private String content;
    private Long userId;
    private String nickname;
    private String profileImageUrl;
    private Long likeCount;
    private Long commentCount;
}
